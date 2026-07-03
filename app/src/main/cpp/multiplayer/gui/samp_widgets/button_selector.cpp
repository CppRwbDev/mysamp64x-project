#include "../../main.h"
#include "../gui.h"
#include "button_selector.h"
#include "../../game/game.h"
#include "../../net/netgame.h"

extern UI* pUI;

ButtonSelector::ButtonSelector()
{
    m_availableKeys = {
        { KEY_FIRE, "CTRL" },
        { KEY_JUMP, "SHIFT" },
        { KEY_CROUCH, "C" },
        { KEY_WALK, "ALT" },
        { KEY_ACTION, "TAB" },
        { KEY_CTRL_BACK, "H" },
        { KEY_YES, "Y" },
        { KEY_NO, "N" },
        { KEY_SECONDARY_ATTACK, "ENTER" },
        { KEY_SECONDARY_ATTACK, "F" },
        { KEY_SUBMISSION, "2" },
        { KEY_ANALOG_UP, "NUM8" },
        { KEY_ANALOG_DOWN, "NUM2" },
        { KEY_ANALOG_LEFT, "NUM4" },
        { KEY_ANALOG_RIGHT, "NUM6" }
    };

    m_listBox = new ListBox();
    m_listBox->setItemSize(ImVec2(UISettings::fontSize() * 10, UISettings::fontSize() * 1.5f));
    this->addChild(m_listBox);

    m_closeButton = new Button("X", UISettings::fontSize() / 2);
    m_closeButton->setCallback([this]() {
        hide();
    });
    this->addChild(m_closeButton);

    for (const auto& info : m_availableKeys)
    {
        m_listBox->addItem(new KeyItem(info.name, info.key));
    }

    setVisible(false);
}

void ButtonSelector::performLayout()
{
    float padding = UISettings::padding();

    m_closeButton->setFixedSize(ImVec2(UISettings::fontSize(), UISettings::fontSize()));
    m_closeButton->performLayout();
    m_closeButton->setPosition(ImVec2(width() - m_closeButton->width() - padding/2, padding/2));

    m_listBox->setFixedSize(ImVec2(width(), height() - m_closeButton->height() - padding * 2));
    m_listBox->setItemSize(ImVec2(width(), UISettings::fontSize() * 1.5f));
    m_listBox->performLayout();
    m_listBox->setPosition(ImVec2(0, m_closeButton->height() + padding));

    // Position in center of screen
    if (parent())
        setPosition((parent()->size() - size()) / 2);
}

void ButtonSelector::draw(ImGuiRenderer* renderer)
{
    if (!visible()) return;

    renderer->drawRect(absolutePosition(), absolutePosition() + size(), ImColor(0, 0, 0, 200), true);
    Widget::draw(renderer);
}

void ButtonSelector::show()
{
    setVisible(true);
}

void ButtonSelector::hide()
{
    setVisible(false);
}

/* KeyItem */

ButtonSelector::KeyItem::KeyItem(const std::string& name, ePadKeys key)
    : ListBoxItem(true), m_key(key)
{
    m_nameLabel = new Label(name, ImColor(1.0f, 1.0f, 1.0f), false, UISettings::fontSize() / 2);
    this->addChild(m_nameLabel);

    m_addButton = new Button("+", UISettings::fontSize() / 2);
    m_addButton->setCallback([this]() {
        if (pUI && pUI->buttonpanel()) {
            pUI->buttonpanel()->AddCustomButton(m_key, m_nameLabel->text());
        }
    });
    this->addChild(m_addButton);

    m_removeButton = new Button("-", UISettings::fontSize() / 2);
    m_removeButton->setCallback([this]() {
        if (pUI && pUI->buttonpanel()) {
            pUI->buttonpanel()->RemoveCustomButton(m_key, m_nameLabel->text());
        }
    });
    this->addChild(m_removeButton);
}

void ButtonSelector::KeyItem::performLayout()
{
    float padding = UISettings::padding();
    m_nameLabel->performLayout();
    m_nameLabel->setPosition(ImVec2(padding, (height() - m_nameLabel->height()) / 2));

    float btnSize = height() * 0.8f;
    m_addButton->setFixedSize(ImVec2(btnSize, btnSize));
    m_addButton->performLayout();

    m_removeButton->setFixedSize(ImVec2(btnSize, btnSize));
    m_removeButton->performLayout();

    m_removeButton->setPosition(ImVec2(width() - m_removeButton->width() - padding, (height() - m_removeButton->height()) / 2));
    m_addButton->setPosition(ImVec2(m_removeButton->position().x - m_addButton->width() - padding/2, (height() - m_addButton->height()) / 2));
}

void ButtonSelector::KeyItem::draw(ImGuiRenderer* renderer)
{
    ListBoxItem::draw(renderer);
}
