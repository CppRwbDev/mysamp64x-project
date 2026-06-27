package com.flinc.core;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class KeyboardInputTextView extends androidx.appcompat.widget.AppCompatEditText {
    public KeyboardInputTextView(Context context) {
        super(context);
    }

    public KeyboardInputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardInputTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }

    boolean canPaste()
    {
        return false;
    }
}
