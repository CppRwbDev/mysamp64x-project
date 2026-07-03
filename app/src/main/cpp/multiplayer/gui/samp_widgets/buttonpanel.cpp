#include "../../main.h"
#include "../gui.h"
#include "../../game/game.h"
#include "../../net/netgame.h"
#include "../../net/localplayer.h"
#include "../../net/netgame.h"
#include "../../vendor/SimpleIni/SimpleIni.h"

extern UI* pUI;
extern CNetGame* pNetGame;
extern CGame *pGame;

bool bNeedEnterVehicle = false;
bool OpenButton = true;
int Tab = 0;

ButtonPanel::ButtonPanel()
        : Layout(Orientation::HORIZONTAL)
{
    m_bToggle = new OButton(">>", UISettings::fontSize() / 2);
    m_bToggle->setFixedSize(ImVec2(UISettings::fontSize() * 1.5f, UISettings::fontSize() * 1.2f));

    m_bToggle->setCallback([this]() {
        OpenButton = !OpenButton;
        m_bToggle->setCaption(OpenButton ? "<<" : ">>");

        for (auto& cb : m_customButtons) {
            cb.button->setVisible(OpenButton);
        }

        this->performLayout();
    });

    this->addChild(m_bToggle);

    LoadCustomButtons();

    // Re-sync initial state
    m_bToggle->setCaption(OpenButton ? "<<" : ">>");
    for (auto& cb : m_customButtons) {
        cb.button->setVisible(OpenButton);
    }
}

ButtonPanel::~ButtonPanel()
{
    SaveCustomButtons();
}

void ButtonPanel::AddCustomButton(ePadKeys key, const std::string& name)
{
    // Check if already exists (check both key AND name)
    for (auto& cb : m_customButtons) {
        if (cb.key == key && cb.button->caption() == name) return;
    }

    CButton* btn = new CButton(name, UISettings::fontSize() / 2);
    btn->setFixedSize(ImVec2(UISettings::fontSize() * 1.5f, UISettings::fontSize() * 1.2f));
    btn->setCallback([key]() {
        LocalPlayerKeys.bKeys[key] = true;
    });

    btn->setVisible(OpenButton);
    this->addChild(btn);
    m_customButtons.push_back({key, btn});
    this->performLayout();
    SaveCustomButtons();
}

void ButtonPanel::RemoveCustomButton(ePadKeys key, const std::string& name)
{
    for (auto it = m_customButtons.begin(); it != m_customButtons.end(); ++it) {
        if (it->key == key && it->button->caption() == name) {
            this->removeChild(it->button);
            m_customButtons.erase(it);
            this->performLayout();
            SaveCustomButtons();
            return;
        }
    }
}

void ButtonPanel::SaveCustomButtons()
{
    char buff[0x7F];
    sprintf(buff, "%sSAMP/custom_buttons.ini", g_pszStorage);

    CSimpleIniA ini;
    ini.SetUnicode(true);

    ini.SetLongValue("buttons", "count", (long)m_customButtons.size());
    for (size_t i = 0; i < m_customButtons.size(); ++i) {
        char key_name[32];
        sprintf(key_name, "key_%zu", i);
        ini.SetLongValue("buttons", key_name, (long)m_customButtons[i].key);

        char label_name[32];
        sprintf(label_name, "label_%zu", i);
        ini.SetValue("buttons", label_name, m_customButtons[i].button->caption().c_str());
    }

    ini.SaveFile(buff);
}

void ButtonPanel::LoadCustomButtons()
{
    char buff[0x7F];
    sprintf(buff, "%sSAMP/custom_buttons.ini", g_pszStorage);

    CSimpleIniA ini;
    ini.SetUnicode(true);
    if (ini.LoadFile(buff) < 0) return;

    long count = ini.GetLongValue("buttons", "count", 0);
    for (long i = 0; i < count; ++i) {
        char key_name[32];
        sprintf(key_name, "key_%ld", i);
        ePadKeys key = (ePadKeys)ini.GetLongValue("buttons", key_name, -1);

        char label_name[32];
        sprintf(label_name, "label_%ld", i);
        const char* label = ini.GetValue("buttons", label_name, "");

        if (key != -1 && strlen(label) > 0) {
            CButton* btn = new CButton(label, UISettings::fontSize() / 2);
            btn->setFixedSize(ImVec2(UISettings::fontSize() * 1.5f, UISettings::fontSize() * 1.2f));
            btn->setCallback([key]() {
                LocalPlayerKeys.bKeys[key] = true;
            });
            btn->setVisible(false);
            this->addChild(btn);
            m_customButtons.push_back({key, btn});
        }
    }
    this->performLayout();
}
