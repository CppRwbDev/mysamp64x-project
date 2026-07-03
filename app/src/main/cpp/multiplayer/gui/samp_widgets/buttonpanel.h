#pragma once

#include "../../game/pad.h"

class ButtonPanel : public Layout
{
public:
    ButtonPanel();
    ~ButtonPanel();

    void AddCustomButton(ePadKeys key, const std::string& name);
    void RemoveCustomButton(ePadKeys key, const std::string& name);
    void SaveCustomButtons();
    void LoadCustomButtons();

private:
    OButton* m_bToggle;

    struct CustomButton {
        ePadKeys key;
        CButton* button;
    };
    std::vector<CustomButton> m_customButtons;
};
