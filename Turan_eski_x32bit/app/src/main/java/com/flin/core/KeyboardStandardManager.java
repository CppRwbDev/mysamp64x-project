package com.flinc.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.InputType;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nvidia.devtech.NvEventQueueActivity;

public class KeyboardStandardManager implements View.OnClickListener {

    NvEventQueueActivity root;
    AppCompatActivity activity;

    ConstraintLayout keyboardStandardView;

    LinearLayout keyboardCharsLineUp;
    LinearLayout keyboardCharsLineMiddle;
    LinearLayout keyboardCharsLineDown;

    KeyboardButtonShiftView ButtonSymbolShift;
    KeyboardButtonBackspaceView ButtonSymbolBackspace;
    Button ButtonSymbolLang;
    KeyboardButtonSpaceView ButtonSymbolSpace;
    Button keyboardHistoryButtonUp;
    Button keyboardHistoryButtonDown;
    KeyboardButtonEnterView ButtonSymbolEnter;
    FrameLayout visibleZone;

    public KeyboardInputTextView keyboardTextInput;

    public static final int KEYBOARD_LANG_ENG = 0;
    public static final int KEYBOARD_LANG_RUS = 1;
    public static final int KEYBOARD_LANG_SPEC = 2;

    int selectedLang;

    boolean activeShift;
    boolean activePasswordSecurity;
    int useType = -1;

    Runnable callableEnter;
    Runnable callableClose;

    String keyboardsCharLang[][] = {
            {

            },
            {

            },
            {

            }
    };

    Button keyboardSpecSymbols[] = {
            null, null, null, null
    };


    final int TYPE_USE_KEYBOARD_CHAT = 0;
    final int TYPE_USE_KEYBOARD_DIALOG = 1;
    final int MAX_TYPES_OF_USE_KEYBOARD = 2;

    String HistoryTexts[] = new String[30];
    String LastTexts[] = new String[MAX_TYPES_OF_USE_KEYBOARD];
    int idSelectedHistoryTexts = -1;


    public KeyboardStandardManager(NvEventQueueActivity root) {
        this.root = root;
        this.activity = (AppCompatActivity) root;

        View inflatedViewkeyboardStandard = activity.getLayoutInflater().inflate(R.layout.activity_keyboard_standard, null, false);
        keyboardStandardView = (ConstraintLayout) inflatedViewkeyboardStandard.findViewById(R.id.keyboardStandard);

        keyboardCharsLineUp = (LinearLayout) keyboardStandardView.findViewById(R.id.contentButtonsListLineUp);
        keyboardCharsLineMiddle = (LinearLayout) keyboardStandardView.findViewById(R.id.contentButtonsListLineMiddle);
        keyboardCharsLineDown = (LinearLayout) keyboardStandardView.findViewById(R.id.contentButtonsListLineDown);

        ButtonSymbolShift = (KeyboardButtonShiftView) keyboardStandardView.findViewById(R.id.contentButtonSymbolShift);
        ButtonSymbolBackspace = (KeyboardButtonBackspaceView) keyboardStandardView.findViewById(R.id.contentButtonSymbolBackspace);
        ButtonSymbolSpace = (KeyboardButtonSpaceView) keyboardStandardView.findViewById(R.id.contentButtonSymbolSpace);
        ButtonSymbolLang = (Button) keyboardStandardView.findViewById(R.id.contentButtonSymbolSpecLang);
        ButtonSymbolEnter = (KeyboardButtonEnterView) keyboardStandardView.findViewById(R.id.contentButtonSymbolEnter);
        visibleZone = (FrameLayout) keyboardStandardView.findViewById(R.id.visibleZone);


        keyboardTextInput = (KeyboardInputTextView) keyboardStandardView.findViewById(R.id.contentText);

        keyboardSpecSymbols[0] = (Button) keyboardStandardView.findViewById(R.id.contentButtonSymbolSpec1);
        keyboardSpecSymbols[1] = (Button) keyboardStandardView.findViewById(R.id.contentButtonSymbolSpec2);
        keyboardSpecSymbols[2] = (Button) keyboardStandardView.findViewById(R.id.contentButtonSymbolSpec3);
        keyboardSpecSymbols[3] = (Button) keyboardStandardView.findViewById(R.id.contentButtonSymbolSpec4);

        keyboardHistoryButtonUp = (Button) keyboardStandardView.findViewById(R.id.buttonHistoryUP);
        keyboardHistoryButtonDown = (Button) keyboardStandardView.findViewById(R.id.buttonHistoryDown);

        ButtonSymbolEnter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity) return false;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonSymbolEnter.setBackgroundColor(0xFFF59132);
                    ButtonSymbolEnter.invalidate();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonSymbolEnter.setBackgroundColor(Color.TRANSPARENT);
                    ButtonSymbolEnter.invalidate();
                }
                return false;
            }
        });

        ButtonSymbolBackspace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity) return false;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonSymbolBackspace.setBackgroundColor(0xFFF59132);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonSymbolBackspace.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

        ButtonSymbolSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity) return false;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ButtonSymbolSpace.myColor = 0xFFF59132;
                    ButtonSymbolSpace.invalidate();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ButtonSymbolSpace.myColor = 0xFF86888A;
                    ButtonSymbolSpace.invalidate();
                }
                return false;
            }
        });

        keyboardHistoryButtonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity) return false;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(0xFFF59132);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.setBackgroundColor(0x1AFFFFFF);
                }
                return false;
            }
        });

        keyboardHistoryButtonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity) return false;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(0xFFF59132);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.setBackgroundColor(0x1AFFFFFF);
                }
                return false;
            }
        });

        keyboardTextInput.setFocusable(true);
        keyboardTextInput.setFocusableInTouchMode(true);

        keyboardHistoryButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newSelectedIndexHistory = idSelectedHistoryTexts+1;
                if(newSelectedIndexHistory >= HistoryTexts.length){
                    return;
                }
                if(HistoryTexts[newSelectedIndexHistory].length() == 0){
                    return;
                }
                idSelectedHistoryTexts = newSelectedIndexHistory;
                keyboardTextInput.setText(HistoryTexts[newSelectedIndexHistory]);
                keyboardTextInput.setSelection(keyboardTextInput.getText().toString().length());
                keyboardTextInput.requestFocus();
            }
        });
        keyboardHistoryButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newSelectedIndexHistory = idSelectedHistoryTexts-1;
                if(newSelectedIndexHistory < -1){
                    return;
                }
                if(newSelectedIndexHistory == -1){
                    keyboardTextInput.setText("");
                    idSelectedHistoryTexts = newSelectedIndexHistory;
                    return;
                }
                if(HistoryTexts[newSelectedIndexHistory].length() == 0){
                    return;
                }
                idSelectedHistoryTexts = newSelectedIndexHistory;
                keyboardTextInput.setText(HistoryTexts[newSelectedIndexHistory]);
                keyboardTextInput.setSelection(keyboardTextInput.getText().toString().length());
                keyboardTextInput.requestFocus();

            }
        });


        for (int i = 0; i < LastTexts.length; i++) {
            LastTexts[i] = "";
        }
        for (int i = 0; i < HistoryTexts.length; i++) {
            HistoryTexts[i] = "";
        }

        for (int i = 0; i < keyboardSpecSymbols.length; i++) {
            keyboardSpecSymbols[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(activePasswordSecurity) return false;
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        v.setBackgroundColor(0xFFF59132);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return false;
                }
            });
            keyboardSpecSymbols[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String oldText = keyboardTextInput.getText().toString();
                    int selectionCursorStart = keyboardTextInput.getSelectionStart();
                    if(selectionCursorStart - keyboardTextInput.getSelectionEnd() != 0){
                        String startString = oldText.substring(0, selectionCursorStart);
                        String endString = oldText.substring(keyboardTextInput.getSelectionEnd(), oldText.length());
                        keyboardTextInput.setText(startString + ((TextView) v).getText() + endString);
                        keyboardTextInput.setSelection(selectionCursorStart+1);
                        return;
                    }

                    if (!keyboardTextInput.isFocused()) {
                        selectionCursorStart = keyboardTextInput.getText().toString().length();
                    }

                    String textPart1 = oldText.substring(0, selectionCursorStart);
                    String textPart2 = oldText.substring(selectionCursorStart);


                    keyboardTextInput.setText(textPart1 + ((TextView) v).getText() + textPart2);
                    keyboardTextInput.setSelection(selectionCursorStart + 1);
                }
            });
        }

        keyboardsCharLang[KEYBOARD_LANG_ENG] = new String[]{
                "qwertyuiop", "asdfghjkl", "zxcvbnm"
        };

        keyboardsCharLang[KEYBOARD_LANG_RUS] = new String[]{
                "йцукенгшщзх", "фывапролджэ", "ячсмитьбю"
        };


        keyboardsCharLang[KEYBOARD_LANG_SPEC] = new String[]{
                "1234567890", "@#$%\"*()-_", ".:;+=<>[]"
        };

        ButtonSymbolShift.setOnClickListener(this);
        ButtonSymbolBackspace.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            keyboardTextInput.setShowSoftInputOnFocus(false);
        } else {
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(keyboardTextInput.getWindowToken(), 0);
        }


        keyboardTextInput.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        ButtonSymbolSpace.setOnClickListener(this);
        ButtonSymbolLang.setOnClickListener(this);
        ButtonSymbolLang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity) return false;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(0xFFF59132);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        ButtonSymbolEnter.setOnClickListener(this);

    }

    public void setCallableEnter(Runnable callable) {
        this.callableEnter = callable;
    }

    public void setCallableClose(Runnable callable) {
        this.callableClose = callable;
        if(callable != null){
            visibleZone.setOnTouchListener(null);
            visibleZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callableClose == null){
                        return;
                    }
                    callableClose.run();
                    return;
                }
            });
        }else{
            visibleZone.setOnClickListener(null);
            visibleZone.setOnTouchListener(root);
        }

    }

    public void setVisible(int active, int type, boolean isPassword) {
        activePasswordSecurity = isPassword;
        useType = type;
        if(isPassword){
            keyboardTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            keyboardTextInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        }
        if (active == 1) {
            keyboardTextInput.setText(LastTexts[type]);
            keyboardStandardView.setVisibility(View.VISIBLE);
            keyboardTextInput.setSelection(keyboardTextInput.getText().toString().length());
            keyboardTextInput.requestFocus();
        } else {
            keyboardStandardView.setVisibility(View.INVISIBLE);
            LastTexts[type] = keyboardTextInput.getText().toString();
        }
    }

    public void setTextLastOnType(String text, int type){
        LastTexts[type] = text;
    }

    public int getFreeSlotOfHistory(){
        for (int i = 0; i < HistoryTexts.length; i++) {
            if (HistoryTexts[i].length() == 0) {
                return i;
            }
        }

        return -1;
    }

    public void addHistoryText(String text){
        offsetSLotsInHistory();
        int freeSlotInHistory = getFreeSlotOfHistory();

        if(freeSlotInHistory == -1){
            deleteLastSlotInHistory();
            freeSlotInHistory = getFreeSlotOfHistory();
        }
        HistoryTexts[freeSlotInHistory] = text;
    }

    public void offsetSLotsInHistory(){
        String newHistoryTexts[] = new String[HistoryTexts.length];
        for (int i = 0; i < newHistoryTexts.length; i++) {
            newHistoryTexts[i] = "";
        }
        for (int i = 0; i < HistoryTexts.length; i++) {

            if(i == HistoryTexts.length-1){
                continue;
            }
            newHistoryTexts[i + 1] = HistoryTexts[i];
        }
        HistoryTexts = newHistoryTexts;
    }

    public void deleteLastSlotInHistory(){
        for (int i = 1; i < HistoryTexts.length; i++) {
            HistoryTexts[i-1] = HistoryTexts[i];
        }
        HistoryTexts[HistoryTexts.length-1] = "";
    }

    public void selectLang(int lang) {
        selectedLang = lang;
        if (lang == KEYBOARD_LANG_SPEC) {
            for (int i = 0; i < keyboardCharsLineUp.getChildCount(); i++) {
                if (keyboardCharsLineUp.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineUp.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardCharsLineMiddle.getChildCount(); i++) {
                if (keyboardCharsLineMiddle.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineMiddle.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardCharsLineDown.getChildCount(); i++) {
                if (keyboardCharsLineDown.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineDown.removeViewAt(i);
                    i--;
                }
            }

            for (int i = 0; i < keyboardsCharLang[KEYBOARD_LANG_SPEC].length; i++) {
                String charsOnLine = keyboardsCharLang[KEYBOARD_LANG_SPEC][i];

                for (int j = 0; j < charsOnLine.length(); j++) {
                    if (i == 0) {
                        keyboardCharsLineUp.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()));
                    }
                    if (i == 1) {
                        keyboardCharsLineMiddle.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()));
                    }
                    if (i == 2) {
                        keyboardCharsLineDown.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()), keyboardCharsLineDown.getChildCount() - 1);
                    }
                }
                keyboardCharsLineUp.setWeightSum(keyboardCharsLineUp.getChildCount());
                keyboardCharsLineMiddle.setWeightSum(keyboardCharsLineMiddle.getChildCount());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) keyboardCharsLineMiddle.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                keyboardCharsLineMiddle.setLayoutParams(params);
                keyboardCharsLineDown.setWeightSum(keyboardCharsLineDown.getChildCount() - 2 + (1f * 1));


                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ButtonSymbolShift.getLayoutParams();
                params1.weight = 1f;
                ButtonSymbolShift.setLayoutParams(params1);
                ButtonSymbolShift.setVisibility(View.GONE);

                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ButtonSymbolBackspace.getLayoutParams();
                params2.weight = 1f;
                ButtonSymbolBackspace.setLayoutParams(params2);
            }
        }
        if (lang == KEYBOARD_LANG_RUS) {
            for (int i = 0; i < keyboardCharsLineUp.getChildCount(); i++) {
                if (keyboardCharsLineUp.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineUp.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardCharsLineMiddle.getChildCount(); i++) {
                if (keyboardCharsLineMiddle.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineMiddle.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardCharsLineDown.getChildCount(); i++) {
                if (keyboardCharsLineDown.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineDown.removeViewAt(i);
                    i--;
                }
            }

            for (int i = 0; i < keyboardsCharLang[KEYBOARD_LANG_RUS].length; i++) {
                String charsOnLine = keyboardsCharLang[KEYBOARD_LANG_RUS][i];

                for (int j = 0; j < charsOnLine.length(); j++) {
                    if (i == 0) {
                        keyboardCharsLineUp.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()));
                    }
                    if (i == 1) {
                        keyboardCharsLineMiddle.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()));
                    }
                    if (i == 2) {
                        keyboardCharsLineDown.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()), keyboardCharsLineDown.getChildCount() - 1);
                    }
                }
                keyboardCharsLineUp.setWeightSum(keyboardCharsLineUp.getChildCount());
                keyboardCharsLineMiddle.setWeightSum(keyboardCharsLineMiddle.getChildCount());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) keyboardCharsLineMiddle.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                keyboardCharsLineMiddle.setLayoutParams(params);
                keyboardCharsLineDown.setWeightSum(keyboardCharsLineDown.getChildCount() - 2 + (1f * 2));


                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ButtonSymbolShift.getLayoutParams();
                params1.weight = 1f;
                ButtonSymbolShift.setLayoutParams(params1);
                ButtonSymbolShift.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ButtonSymbolBackspace.getLayoutParams();
                params2.weight = 1f;
                ButtonSymbolBackspace.setLayoutParams(params2);
            }
        }
        if (lang == KEYBOARD_LANG_ENG) {
            for (int i = 0; i < keyboardCharsLineUp.getChildCount(); i++) {
                if (keyboardCharsLineUp.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineUp.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardCharsLineMiddle.getChildCount(); i++) {
                if (keyboardCharsLineMiddle.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineMiddle.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardCharsLineDown.getChildCount(); i++) {
                if (keyboardCharsLineDown.getChildAt(i) instanceof TextView) {
                    keyboardCharsLineDown.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < keyboardsCharLang[KEYBOARD_LANG_ENG].length; i++) {
                String charsOnLine = keyboardsCharLang[KEYBOARD_LANG_ENG][i];

                for (int j = 0; j < charsOnLine.length(); j++) {
                    if (i == 0) {
                        keyboardCharsLineUp.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()));
                    }
                    if (i == 1) {
                        keyboardCharsLineMiddle.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()));
                    }
                    if (i == 2) {
                        keyboardCharsLineDown.addView(createCharButton(new Character(charsOnLine.charAt(j)).toString()), keyboardCharsLineDown.getChildCount() - 1);
                    }
                }
                keyboardCharsLineUp.setWeightSum(keyboardCharsLineUp.getChildCount());
                keyboardCharsLineMiddle.setWeightSum(keyboardCharsLineMiddle.getChildCount());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) keyboardCharsLineMiddle.getLayoutParams();
                params.setMargins((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        40,
                        activity.getResources().getDisplayMetrics()
                ), 0, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        40,
                        activity.getResources().getDisplayMetrics()
                ), 0);
                keyboardCharsLineMiddle.setLayoutParams(params);
                keyboardCharsLineDown.setWeightSum(keyboardCharsLineDown.getChildCount() - 2 + (1.4f * 2));


                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ButtonSymbolShift.getLayoutParams();
                params1.weight = 1.4f;
                ButtonSymbolShift.setLayoutParams(params1);
                ButtonSymbolShift.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ButtonSymbolBackspace.getLayoutParams();
                params2.weight = 1.4f;
                ButtonSymbolBackspace.setLayoutParams(params2);
            }
        }
    }

    public TextView createCharButton(String symb) {
        TextView button = new TextView(activity);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = 0;
        params.weight = 1.0f;

        button.setLayoutParams(params);

        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        button.setGravity(Gravity.CENTER);
        button.setText(activeShift ? symb.toUpperCase() : symb.toLowerCase());

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(activePasswordSecurity){
                    return false;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button.setBackgroundColor(0xFFF59132);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldText = keyboardTextInput.getText().toString();
                int selectionCursorStart = keyboardTextInput.getSelectionStart();
                if(selectionCursorStart - keyboardTextInput.getSelectionEnd() != 0){
                    String startString = oldText.substring(0, selectionCursorStart);
                    String endString = oldText.substring(keyboardTextInput.getSelectionEnd(), oldText.length());
                    keyboardTextInput.setText(startString + ((TextView) v).getText() + endString);
                    keyboardTextInput.setSelection(selectionCursorStart+1);
                    return;
                }

                if (!keyboardTextInput.isFocused()) {
                    selectionCursorStart = keyboardTextInput.getText().toString().length();
                }

                String textPart1 = oldText.substring(0, selectionCursorStart);
                String textPart2 = oldText.substring(selectionCursorStart);


                keyboardTextInput.setText(textPart1 + ((TextView) v).getText() + textPart2);
                keyboardTextInput.setSelection(selectionCursorStart + 1);
            }
        });

        return button;
    }

    public ConstraintLayout getView() {
        return keyboardStandardView;
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == ButtonSymbolBackspace.getId()) {
            String oldText = keyboardTextInput.getText().toString();
            int selectionCursorStart = keyboardTextInput.getSelectionStart();
            if(selectionCursorStart - keyboardTextInput.getSelectionEnd() != 0){
                String startString = oldText.substring(0, selectionCursorStart);
                String endString = oldText.substring(keyboardTextInput.getSelectionEnd(), oldText.length());
                keyboardTextInput.setText(startString + endString);
                keyboardTextInput.setSelection(selectionCursorStart);
                return;
            }

            if (!keyboardTextInput.isFocused()) {
                selectionCursorStart = keyboardTextInput.getText().toString().length();
            }
            if (selectionCursorStart == 0) {
                return;
            }

            String textPart1 = oldText.substring(0, selectionCursorStart - 1);
            String textPart2 = oldText.substring(selectionCursorStart);


            keyboardTextInput.setText(textPart1 + textPart2);
            keyboardTextInput.setSelection(selectionCursorStart - 1);
        }else if (v.getId() == ButtonSymbolSpace.getId()) {
            String oldText = keyboardTextInput.getText().toString();
            int selectionCursorStart = keyboardTextInput.getSelectionStart();
            if(selectionCursorStart - keyboardTextInput.getSelectionEnd() != 0){
                String startString = oldText.substring(0, selectionCursorStart);
                String endString = oldText.substring(keyboardTextInput.getSelectionEnd(), oldText.length());
                keyboardTextInput.setText(startString + " " + endString);
                keyboardTextInput.setSelection(selectionCursorStart+1);
                return;
            }

            if (!keyboardTextInput.isFocused()) {
                selectionCursorStart = keyboardTextInput.getText().toString().length();
            }

            String textPart1 = oldText.substring(0, selectionCursorStart);
            String textPart2 = oldText.substring(selectionCursorStart);


            keyboardTextInput.setText(textPart1 + " " + textPart2);
            keyboardTextInput.setSelection(selectionCursorStart + 1);
        }else if (v.getId() == ButtonSymbolShift.getId()) {

            if (activeShift) {
                activeShift = false;
                ((KeyboardButtonShiftView) v).setColorArrow(0xFF86888A);

                for (int i = 0; i < keyboardCharsLineUp.getChildCount(); i++) {
                    if (keyboardCharsLineUp.getChildAt(i) instanceof TextView) {
                        TextView symbView = ((TextView) keyboardCharsLineUp.getChildAt(i));
                        symbView.setText(symbView.getText().toString().toLowerCase());
                    }
                }
                for (int i = 0; i < keyboardCharsLineMiddle.getChildCount(); i++) {
                    if (keyboardCharsLineMiddle.getChildAt(i) instanceof TextView) {
                        TextView symbView = ((TextView) keyboardCharsLineMiddle.getChildAt(i));
                        symbView.setText(symbView.getText().toString().toLowerCase());
                    }
                }
                for (int i = 0; i < keyboardCharsLineDown.getChildCount(); i++) {
                    if (keyboardCharsLineDown.getChildAt(i) instanceof TextView) {
                        TextView symbView = ((TextView) keyboardCharsLineDown.getChildAt(i));
                        symbView.setText(symbView.getText().toString().toLowerCase());
                    }
                }
            } else {
                activeShift = true;
                ((KeyboardButtonShiftView) v).setColorArrow(0xFFF59132);


                for (int i = 0; i < keyboardCharsLineUp.getChildCount(); i++) {
                    if (keyboardCharsLineUp.getChildAt(i) instanceof TextView) {
                        TextView symbView = ((TextView) keyboardCharsLineUp.getChildAt(i));
                        symbView.setText(symbView.getText().toString().toUpperCase());
                    }
                }
                for (int i = 0; i < keyboardCharsLineMiddle.getChildCount(); i++) {
                    if (keyboardCharsLineMiddle.getChildAt(i) instanceof TextView) {
                        TextView symbView = ((TextView) keyboardCharsLineMiddle.getChildAt(i));
                        symbView.setText(symbView.getText().toString().toUpperCase());
                    }
                }
                for (int i = 0; i < keyboardCharsLineDown.getChildCount(); i++) {
                    if (keyboardCharsLineDown.getChildAt(i) instanceof TextView) {
                        TextView symbView = ((TextView) keyboardCharsLineDown.getChildAt(i));
                        symbView.setText(symbView.getText().toString().toUpperCase());
                    }
                }

            }
        }else if (v.getId() == ButtonSymbolLang.getId()) {

            if (this.selectedLang == KEYBOARD_LANG_ENG) {
                this.selectLang(KEYBOARD_LANG_RUS);
                return;
            }
            if (this.selectedLang == KEYBOARD_LANG_RUS) {
                this.selectLang(KEYBOARD_LANG_SPEC);
                return;
            }
            if (this.selectedLang == KEYBOARD_LANG_SPEC) {
                this.selectLang(KEYBOARD_LANG_ENG);
                return;
            }
        }else if (v.getId() == ButtonSymbolEnter.getId()) {
            if(keyboardTextInput.getText().toString().length() > 0 && useType == TYPE_USE_KEYBOARD_CHAT){
                addHistoryText(keyboardTextInput.getText().toString());
            }
            idSelectedHistoryTexts = -1;

            //this.root.OnInputEnd(keyboardTextInput.getText().toString());
            if(callableEnter != null){
                callableEnter.run();
                setCallableEnter(null);
            }else{
                keyboardStandardView.setVisibility(View.INVISIBLE);
            }

            keyboardTextInput.setText("");
            if(useType != -1){
                LastTexts[useType] = "";
            }

            //keyboardStandartView.setVisibility(View.INVISIBLE);

        }
    }
}
