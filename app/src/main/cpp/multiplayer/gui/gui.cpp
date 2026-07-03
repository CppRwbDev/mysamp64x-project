#include "../main.h"
#include "../game/game.h"
#include "../net/netgame.h"
#include "gui.h"
#include "../playertags.h"
#include "../net/playerbubblepool.h"
#include "vendor/str_obfuscator/str_obfuscator.hpp"
#include "../game/Mobile/MobileMenu/MobileMenu.h"
// voice
#include "../voice_new/Plugin.h"
#include "../voice_new/MicroIcon.h"
#include "../voice_new/SpeakerList.h"
#include "../voice_new/Network.h"

#include "../gui/samp_widgets/voicebutton.h"
#include "game/Textures/TextureDatabaseRuntime.h"
#include "game/Streaming.h"
#include "game/Pools.h"
#include "game/CGPS.hpp"

#include "../java/jniutil.h"

extern GPS* pGPS;
extern CNetGame* pNetGame;
extern CPlayerTags* pPlayerTags;
extern CJavaWrapper* pJavaWrapper;
extern UI* pUI;
extern CMobileMenu* CMobileMenu;
extern CSettings* pSettings;
extern CGame* pGame;

static bool bAFKInitialized = false;

UI::UI(const ImVec2& display_size, const std::string& font_path)
        : Widget(), ImGuiWrapper(display_size, font_path), m_pJavaWrapper(nullptr)
{
    m_iEat = 0;
    m_iDrink = 0;
    m_iBankMoney = 0;
    m_fFuel = 0.0f;
    UISettings::Initialize(display_size);
    this->setFixedSize(display_size);
}

bool UI::initialize()
{
    if (!ImGuiWrapper::initialize()) return false;

    m_splashScreen = new SplashScreen();
    this->addChild(m_splashScreen);
    m_splashScreen->setFixedSize(size());
    m_splashScreen->setPosition(ImVec2(0.0f, 0.0f));
    m_splashScreen->setVisible(true);

    m_chat = new Chat();
    this->addChild(m_chat);
    m_chat->setFixedSize(UISettings::chatSize());
    m_chat->setPosition(UISettings::chatPos());
    m_chat->setItemSize(UISettings::chatItemSize());
    m_chat->setVisible(false);

    m_buttonPanel = new ButtonPanel();
    this->addChild(m_buttonPanel);
    m_buttonPanel->setFixedSize(UISettings::buttonPanelSize());
    m_buttonPanel->setPosition(UISettings::buttonPanelPos());
    m_buttonPanel->setVisible(false);

    m_buttonSelector = new ButtonSelector();
    this->addChild(m_buttonSelector);
    m_buttonSelector->setFixedSize(ImVec2(UISettings::fontSize() * 10, UISettings::fontSize() * 15));
    m_buttonSelector->setVisible(false);

    m_voiceButton = new VoiceButton();
    this->addChild(m_voiceButton);
    m_voiceButton->setFixedSize(UISettings::buttonVoiceSize());
    m_voiceButton->setPosition(UISettings::buttonVoicePos());
    m_voiceButton->setVisible(false);

    m_spawn = new Spawn();
    this->addChild(m_spawn);
    m_spawn->setFixedSize(UISettings::spawnSize());
    m_spawn->setPosition(UISettings::spawnPos());
    m_spawn->setVisible(false);

    m_dialog = new Dialog();
    this->addChild(m_dialog);
    m_dialog->setVisible(false);
    m_dialog->setMinSize(UISettings::dialogMinSize());
    m_dialog->setMaxSize(UISettings::dialogMaxSize());

    m_keyboard = new Keyboard();
    this->addChild(m_keyboard);
    m_keyboard->setFixedSize(UISettings::keyboardSize());
    m_keyboard->setPosition(UISettings::keyboardPos());
    m_keyboard->setVisible(false);

    m_playerTabList = new PlayerTabList();
    //this->addChild(m_playerTabList);

    return true;
}

void UI::render()
{
    ImGuiWrapper::render();

    ShowSpeed();

    if (m_bNeedClearMousePos) {
        ImGuiIO& io = ImGui::GetIO();
        io.MousePos = ImVec2(-1, -1);
        m_bNeedClearMousePos = false;
    }

    if (m_playerTabList && m_playerTabList->visible())
        m_playerTabList->Tick();
}

void UI::shutdown()
{
    ImGuiWrapper::shutdown();
}

void UI::drawList()
{
    if (!visible()) return;

    if (pPlayerTags) pPlayerTags->Render(renderer());
    if (pNetGame && pNetGame->GetTextLabelPool()) pNetGame->GetTextLabelPool()->Render(renderer());
    if (pNetGame && pNetGame->GetPlayerBubblePool()) pNetGame->GetPlayerBubblePool()->Render(renderer());

    if (pGPS) pGPS->DoPathDraw();

    draw(renderer());
}

void UI::touchEvent(const ImVec2& pos, TouchType type)
{
    if(!visible()) return;

    if (m_keyboard->visible() && m_keyboard->contains(pos))
    {
        m_keyboard->touchEvent(pos, type);
        return;
    }

    if (m_dialog->visible() && m_dialog->contains(pos))
    {
        m_dialog->touchEvent(pos, type);
        return;
    }

    if (m_buttonSelector->visible() && m_buttonSelector->contains(pos))
    {
        m_buttonSelector->touchEvent(pos, type);
        return;
    }

    if (m_buttonPanel->visible() && m_buttonPanel->contains(pos))
    {
        m_buttonPanel->touchEvent(pos, type);
        return;
    }

    Widget::touchEvent(pos, type);
}

enum eTouchType
{
    TOUCH_POP = 1,
    TOUCH_PUSH = 2,
    TOUCH_MOVE = 3
};

bool UI::OnTouchEvent(int type, bool multi, int x, int y)
{
    ImGuiIO& io = ImGui::GetIO();

    if (multi) return false;

    if (type == TOUCH_PUSH)
    {
        io.MousePos = ImVec2(x, y);
        io.MouseDown[0] = true;
        touchEvent(io.MousePos, TouchType::push);
    }
    else if (type == TOUCH_POP)
    {
        io.MouseDown[0] = false;
        touchEvent(io.MousePos, TouchType::pop);
        m_bNeedClearMousePos = true;
    }
    else if (type == TOUCH_MOVE)
    {
        io.MousePos = ImVec2(x, y);
        touchEvent(io.MousePos, TouchType::move);
    }

    if (m_keyboard->visible() && m_keyboard->contains(io.MousePos)) return true;
    if (m_dialog->visible() && m_dialog->contains(io.MousePos)) return true;
    if (m_buttonSelector->visible() && m_buttonSelector->contains(io.MousePos)) return true;
    if (m_buttonPanel->visible() && m_buttonPanel->contains(io.MousePos)) return true;

    return false;
}

void UI::renderDebug()
{
}

void UI::PushToBufferedQueueTextDrawPressed(uint16_t id)
{
    BUFFERED_COMMAND_TEXTDRAW* bct = m_BufferedCommandTextdraws.WriteLock();
    if (bct) {
        bct->textdrawId = id;
        m_BufferedCommandTextdraws.WriteUnlock();
    }
}

void UI::ShowSpeed() {
    if (!pJavaWrapper) return;
    if (pGame && pGame->FindPlayerPed() && pGame->FindPlayerPed()->IsInVehicle()) {
        CVehicle* pVehicle = pGame->FindPlayerPed()->GetCurrentVehicle();
        if (pVehicle) {
            pJavaWrapper->UpdateSpeedInfo(
                (int)pVehicle->GetSpeed(),
                (int)m_fFuel, // fuel
                (int)pVehicle->GetHealth(),
                0, // mileage
                1, // engine
                1, // light
                0, // belt
                0  // lock
            );
        }
    } else {
        pJavaWrapper->HideSpeed();
    }
}
