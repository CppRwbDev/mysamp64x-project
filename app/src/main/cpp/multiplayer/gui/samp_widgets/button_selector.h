#pragma once

#include "../widget.h"
#include "../widgets/layout.h"
#include "../widgets/button.h"
#include "../widgets/listbox.h"
#include "../../game/pad.h"

class ButtonSelector : public Widget
{
public:
    ButtonSelector();

    struct KeyInfo {
        ePadKeys key;
        std::string name;
    };

    void performLayout() override;
    void draw(ImGuiRenderer* renderer) override;

    void show();
    void hide();

private:
    ListBox* m_listBox;
    Button* m_closeButton;
    std::vector<KeyInfo> m_availableKeys;

    class KeyItem : public ListBoxItem
    {
    public:
        KeyItem(const std::string& name, ePadKeys key);
        void performLayout() override;
        void draw(ImGuiRenderer* renderer) override;

        ePadKeys getKey() const { return m_key; }

    private:
        Label* m_nameLabel;
        Button* m_addButton;
        Button* m_removeButton;
        ePadKeys m_key;
    };
};
