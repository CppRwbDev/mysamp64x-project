#pragma once

#define DEBUG_GUI 0

#include "../vendor/encoding/encoding.h"

#include "imguiwrapper.h"
#include "uisettings.h"

#include "widget.h"
#include "widgets/layout.h"
#include "widgets/label.h"
#include "widgets/button.h"
#include "widgets/image.h"
#include "widgets/progressbar.h"
#include "widgets/scrollpanel.h"
#include "widgets/listbox.h"
#include "widgets/editbox.h"

#include "samp_widgets/keyboard.h"
#include "samp_widgets/splashscreen.h"
#include "samp_widgets/chat.h"
#include "samp_widgets/spawn.h"
#include "samp_widgets/buttonpanel.h"
#include "samp_widgets/playertablist.h"
#include "samp_widgets/voicebutton.h"

#include "samp_widgets/button_selector.h"
#include "samp_widgets/dialogs/dialog.h"

#include "../vendor/raknet/SingleProducerConsumer.h"

class CJavaWrapper;

#pragma pack(push, 1)
struct BUFFERED_COMMAND_TEXTDRAW
{
    uint16_t textdrawId;
};
#pragma pack(pop)

class Chat;
class UI : public Widget, public ImGuiWrapper
{
public:
    UI(const ImVec2& display_size, const std::string& font_path);

    bool initialize() override;
    void render() override;
    void shutdown() override;

    SplashScreen* splashscreen() const { return m_splashScreen; }
    Chat* chat() const { return m_chat; }
    Spawn* spawn() const { return m_spawn; }
    Keyboard* keyboard() const { return m_keyboard; }
    Dialog* dialog() const { return m_dialog; }
    ButtonPanel* buttonpanel() const { return m_buttonPanel; }
    ButtonSelector* buttonselector() const { return m_buttonSelector; }
    PlayerTabList* playertablist() const { return m_playerTabList; }
    VoiceButton* voicebutton() const { return m_voiceButton; }

    void SetJavaWrapper(CJavaWrapper* wrapper) { m_pJavaWrapper = wrapper; }

    static const ImColor fixcolor(uint32_t color) {
        return ImColor(
                (int)((color & 0xFF000000) >> 24),
                (int)((color & 0x00FF0000) >> 16),
                (int)((color & 0x0000FF00) >> 8));
    }

    float ScaleX(float x) {
        return x * displaySize().x * (1.0f / 1920.0f);
    }

    float ScaleY(float y) {
        return y * displaySize().y * (1.0f / 1080.0f);
    }

    float GetFontSize() {
        return UISettings::fontSize();
    }

    int GetEat() { return m_iEat; }
    int GetDrink() { return m_iDrink; }
    int GetBankMoney() { return m_iBankMoney; }

    void SetEat(float eat) { m_iEat = (int)eat; }
    void SetDrink(float drink) { m_iDrink = (int)drink; }
    void SetBankMoney(uint32_t bank) { m_iBankMoney = (int)bank; }
    void SetFuel(float fuel) { m_fFuel = fuel; }

    void PushToBufferedQueueTextDrawPressed(uint16_t id);

    virtual void touchEvent(const ImVec2& pos, TouchType type) override;

    bool OnTouchEvent(int type, bool multi, int x, int y);

    void renderDebug();
    void ShowSpeed();

protected:
    void drawList() override;

private:
    Keyboard* m_keyboard;
    SplashScreen* m_splashScreen;
    Chat* m_chat;
    Spawn* m_spawn;
    Dialog* m_dialog;
    ButtonPanel* m_buttonPanel;
    ButtonSelector* m_buttonSelector;
    PlayerTabList* m_playerTabList;
    VoiceButton* m_voiceButton;

    CJavaWrapper* m_pJavaWrapper;

    int m_iEat;
    int m_iDrink;
    int m_iBankMoney;
    float m_fFuel;

    bool m_bNeedClearMousePos = false;

    DataStructures::SingleProducerConsumer<BUFFERED_COMMAND_TEXTDRAW> m_BufferedCommandTextdraws;
};
