package com.flinc.core;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.InputType;
import android.text.TextPaint;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.flin.online.DialogLoginFragment;
import com.flin.online.MenuActivity;
import com.flin.online.Util;
import com.flin.online.jsonenter.PublicInfo;
import com.nvidia.devtech.NvEventQueueActivity;
//import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.io.UnsupportedEncodingException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;


class DialogOnClickOnButtonList implements  View.OnClickListener{

    public DialogManager root;
    public int IDButton = -1;

    public DialogOnClickOnButtonList(DialogManager root, int IDButton){
        this.root = root;
        this.IDButton = IDButton;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < this.root.contentLinearList.getChildCount(); i++) {
            LinearLayout container = (LinearLayout) this.root.contentLinearList.getChildAt(i);

            if(container.getChildCount() >= IDButton){
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), container.getChildAt(IDButton).getBackgroundTintList().getDefaultColor(), 0xFFFFA51E);
                colorAnimator.setDuration(300);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        container.getChildAt(IDButton).setBackgroundTintList(ColorStateList.valueOf((int)animation.getAnimatedValue()));
                    }
                });
                colorAnimator.start();
            }

            this.root.selectedItem = IDButton;

            for (int j = 0; j < container.getChildCount(); j++) {
                if(j != IDButton){
                    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), container.getChildAt(j).getBackgroundTintList().getDefaultColor(), 0x40000000);
                    colorAnimator.setDuration(300);
                    int idJ = j;
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            container.getChildAt(idJ).setBackgroundTintList(ColorStateList.valueOf((int)animation.getAnimatedValue()));
                        }
                    });
                    colorAnimator.start();

                }
            }
        }
    }
}


@RequiresApi(api = Build.VERSION_CODES.M)
public class DialogManager implements View.OnScrollChangeListener, View.OnTouchListener, ViewTreeObserver.OnPreDrawListener{
    public NvEventQueueActivity root;
    public AppCompatActivity activity;
    public ConstraintLayout dialogsView;
    public TextView titleText;
    public FrameLayout scrollBarView;
    public FrameLayout sliderView;
    public ScrollView scrollParentContent;
    public LinearLayout scrollChildContent;
    public CardView scrollParentCard;
    public TextView contentTextView;
    public FrameLayout inputBoxView;
    public TextView inputEditText;
    public TextView button1Text;
    public TextView button2Text;
    public ImageButton button1Image;
    public ImageButton button2Image;
    public FrameLayout button1Frame;
    public FrameLayout button2Frame;
    public ScrollView contentScroll;
    public ScrollView contentScrollList;
    public LinearLayout contentLinearList;
    public LinearLayout contentHeaderList;
    public ConstraintLayout contentView;

    FrameLayout contentButtonAutoLogin;
    ImageButton contentButtonImageAutoLogin;

    FrameLayout contentButtonAdmin; //Кнопка <<

    boolean ignoreNextChangeScroll = false;
    boolean activeDialog = false;

    static final int TYPE_SLIDER_SCROLL_TEXT = 0;
    static final int TYPE_SLIDER_SCROLL_ITEMS = 1;

    int typeSlider = TYPE_SLIDER_SCROLL_TEXT;

    int marginItems = 5; // in DP
    int heightItem = 26; // in DP
    int countItemsVisible = 5;

    int activeCountItems = 0;
    public int selectedItem = -1;

    float lastY = 0;
    int maxLines = 22;

    int offsetPivotOnLeftLine = 0;

    String[] idOfItems;
    int dialogIdActive;
    boolean activeDialogResponse = false;

    int lastTimeClick;
    int clickInSecond = 0;

    public DialogManager(NvEventQueueActivity root){

        View inflatedView = root.getLayoutInflater().inflate(R.layout.activity_dialog_all, null, false);
        dialogsView = (ConstraintLayout)inflatedView.findViewById(R.id.allDialogs);

        FrameLayout scrollBarView = dialogsView.findViewById(R.id.contentScrollBar);
        FrameLayout sliderView = dialogsView.findViewById(R.id.contentScrollBarSlider);
        CardView scrollParentCard = dialogsView.findViewById(R.id.contentCardScrollText);
        ScrollView scrollParentContent = dialogsView.findViewById(R.id.contentScroll);
        LinearLayout scrollChildContent = dialogsView.findViewById(R.id.contentTextScroll);
        ConstraintLayout contentView = dialogsView.findViewById(R.id.content);
        ConstraintLayout titleView = dialogsView.findViewById(R.id.title);
        FrameLayout inputBoxView = dialogsView.findViewById(R.id.contentInputBox);
        int[] location = new int[2];
        inputBoxView.getLocationOnScreen(location);
        System.out.println("MIHAIL Location: X:"+location[0]+" Y:"+location[1]);

        TextView contentTextView = (TextView) dialogsView.findViewById(R.id.contentText);
        TextView titleText = (TextView) dialogsView.findViewById(R.id.titleText);
        TextView inputEditText = (TextView) dialogsView.findViewById(R.id.contentInputBoxText);
        TextView button1Text = (TextView) dialogsView.findViewById(R.id.contentButtonTextOne);
        TextView button2Text = (TextView) dialogsView.findViewById(R.id.contentButtonTextTwo);
        ImageButton button1Image = (ImageButton) dialogsView.findViewById(R.id.contentButtonImageOne);
        ImageButton button2Image = (ImageButton) dialogsView.findViewById(R.id.contentButtonImageTwo);
        FrameLayout button1Frame = (FrameLayout) dialogsView.findViewById(R.id.contentButtonLayoutOne);
        FrameLayout button2Frame = (FrameLayout) dialogsView.findViewById(R.id.contentButtonLayoutTwo);
        ScrollView contentScroll = (ScrollView) dialogsView.findViewById(R.id.contentScroll);
        ScrollView contentScrollList = (ScrollView) dialogsView.findViewById(R.id.contentScrollList);
        LinearLayout contentLinearList = (LinearLayout) dialogsView.findViewById(R.id.contentLinearList);
        LinearLayout contentHeaderList = (LinearLayout) dialogsView.findViewById(R.id.contentHeaderList);

        TextView contentButtonTextAutoLogin = dialogsView.findViewById(R.id.contentButtonTextAutoLogin);
        contentButtonAutoLogin = dialogsView.findViewById(R.id.contentButtonAutoLogin);
        contentButtonImageAutoLogin = (ImageButton) dialogsView.findViewById(R.id.contentButtonImageAutoLogin);

        //Кнопка <<
        contentButtonAdmin = (FrameLayout) dialogsView.findViewById(R.id.contentButtonAdmin);
        TextView contentButtonTextImageAdmin = dialogsView.findViewById(R.id.contentButtonTextImageAdmin);
        contentButtonTextImageAdmin.setText("<<");

        ImageButton contentButtonImageAdmin = (ImageButton) dialogsView.findViewById(R.id.contentButtonImageAdmin);
        contentButtonImageAdmin.setOnClickListener(new View.OnClickListener() { //5001
            @Override
            public void onClick(View v) {
               // inputEditText.setText("");
                //onClose();
                buildDialog(5045, "{ffb94f}Список быстрой отправки", "{ffb94f}[1]{ffffff} - Уважаемый игрок, слежу за нарушителем.\n{ffb94f}[2]{ffffff} - Уважаемый игрок, иду помогать.\n{ffb94f}[3]{ffffff} - Уважаемый игрок, сформулируйте вопрос/жалобу.", "Отправить", "Отмена", 2);
            }
        });


        this.root = root;
        this.activity = (AppCompatActivity) root;
        this.scrollBarView = scrollBarView;
        this.sliderView = sliderView;
        this.scrollParentContent = scrollParentContent;
        this.scrollChildContent = scrollChildContent;
        this.scrollParentCard = scrollParentCard;
        this.contentTextView = contentTextView;
        this.titleText = titleText;
        this.inputBoxView = inputBoxView;
        this.inputEditText = inputEditText;
        this.button1Text = button1Text;
        this.button2Text = button2Text;
        this.button1Image = button1Image;
        this.button2Image = button2Image;
        this.button1Frame = button1Frame;
        this.button2Frame = button2Frame;
        this.contentScroll = contentScroll;
        this.contentScrollList = contentScrollList;
        this.contentLinearList = contentLinearList;
        this.contentHeaderList = contentHeaderList;
        this.contentView = contentView;

        Animation animPressed = AnimationUtils.loadAnimation(root, R.anim.flin_dialog_animation_pressed);
        Animation animUnPressed = AnimationUtils.loadAnimation(root, R.anim.flin_dialog_animation_unpressed);
        /*if(PublicInfo.getSelectServerConnectNick != null ) {
            if (DialogLoginFragment.getNick(PublicInfo.getSelectServerConnectNick, PublicInfo.selectServerConnect)) {
                contentButtonAutoLogin.setVisibility(View.INVISIBLE);
                contentButtonTextAutoLogin.setVisibility(View.INVISIBLE);
                contentButtonImageAutoLogin.setVisibility(View.INVISIBLE);
                //System.out.println("MIHAIL вход не найден!");
            }
        }*/



        contentButtonImageAutoLogin.setOnClickListener(new View.OnClickListener() { //5001
            @Override
            public void onClick(View v) {
              /*  if(dialogIdActive == PublicInfo.dialogID_pass) {
                    try {
                        String pass = DialogLoginFragment.getNickLoginPass(PublicInfo.getSelectServerConnectNick, PublicInfo.selectServerConnect);
                        if(pass.length() == 0) {
                            contentButtonAutoLogin.setVisibility(View.GONE);
                        }
                        else root.responseDialog(PublicInfo.dialogID_pass, 1, -1, pass.getBytes("windows-1251"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }*/
               /* if(dialogIdActive == PublicInfo.dialogID_google) {
                    try {
                        Provider[] secureRandomProviders = Security.getProviders("SecureRandom.SHA1PRNG");
                        System.setProperty("com.warrenstrange.googleauth.rng.algorithmProvider",secureRandomProviders[0].getName());
                        GoogleAuthenticator gAuth = new GoogleAuthenticator();
                        int code = gAuth.getTotpPassword(DialogLoginFragment.getNickLoginGoogle(PublicInfo.getSelectServerConnectNick, PublicInfo.selectServerConnect));
                        String dd = String.valueOf(code);
                        root.responseDialog(PublicInfo.dialogID_google, 1, -1, dd.getBytes("windows-1251"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }*/
               /* if(dialogIdActive == PublicInfo.dialogID_admPass) {
                    try {
                        String pass = DialogLoginFragment.getNickLoginPassAdm(PublicInfo.getSelectServerConnectNick, PublicInfo.selectServerConnect);
                        root.responseDialog(PublicInfo.dialogID_admPass, 1, -1, pass.getBytes("windows-1251"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }*/
                inputEditText.setText("");
                onClose();
            }
        });


        button1Image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button1Frame.animate().scaleX(0.85f).scaleY(0.85f).setDuration(150);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button1Frame.animate().scaleX(1f).scaleY(1f).setDuration(150);
                }
                return false;
            }
        });

        button2Image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    button2Frame.animate().scaleX(0.85f).scaleY(0.85f).setDuration(150);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    button2Frame.animate().scaleX(1f).scaleY(1f).setDuration(150);
                }
                return false;
            }
        });


        ViewTreeObserver vto = scrollParentCard.getViewTreeObserver();
        vto.addOnPreDrawListener(this);
        scrollBarView.setOnTouchListener(this);
        scrollParentContent.setOnScrollChangeListener(this);
        contentScrollList.setOnScrollChangeListener(this);

        vto = contentScrollList.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
             @Override
             public boolean onPreDraw() {
                 for (int i = 0; i < contentLinearList.getChildCount(); i++) {
                     LinearLayout childListItem = (LinearLayout) contentLinearList.getChildAt(i);
                     TextView childListHeader = (TextView) contentHeaderList.getChildAt(i);

                     if(i == contentLinearList.getChildCount()-1){
                         if(childListHeader == null || childListItem == null){
                             continue;
                         }
                         LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childListItem.getLayoutParams();
                         params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                         childListItem.setLayoutParams(params);

                         params = (LinearLayout.LayoutParams) childListHeader.getLayoutParams();
                         params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         childListHeader.setLayoutParams(params);
                         continue;
                     }

                     final int finalWidthListItem = childListItem.getMeasuredWidth();
                     final int finalWidthListHeader = childListHeader.getMeasuredWidth();
                     if(finalWidthListHeader == finalWidthListItem){
                         continue;
                     }
                     if(finalWidthListHeader > finalWidthListItem){
                         LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childListItem.getLayoutParams();
                         params.width = finalWidthListHeader;
                         childListItem.setLayoutParams(params);
                     }else{
                         LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) childListItem.getLayoutParams();
                         params2.width = LinearLayout.LayoutParams.MATCH_PARENT;
                         childListItem.setLayoutParams(params2);

                         LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childListHeader.getLayoutParams();
                         params.width = finalWidthListItem;
                         childListHeader.setLayoutParams(params);

                    }
                 }
                 return true;
             }
        });

        vto = contentLinearList.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                final int finalWidthScroll = contentLinearList.getMeasuredWidth();
                final int finalWidthHeader = contentHeaderList.getMeasuredWidth();
                if(contentHeaderList.getVisibility() == View.GONE){
                    return true;
                }
                if(finalWidthHeader == finalWidthScroll){
                    return true;
                }
                if(finalWidthHeader > finalWidthScroll){
                    contentLinearList.setMinimumWidth(finalWidthHeader);
                }else{
                    contentHeaderList.setMinimumWidth(finalWidthScroll);
                    contentLinearList.setMinimumWidth((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            350,
                            activity.getResources().getDisplayMetrics()
                    ));
                }
                return true;
            }
        });

        vto = contentView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int finalWidth = contentView.getMeasuredWidth();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) titleView.getLayoutParams();
                params.width = finalWidth;
                titleView.setLayoutParams(params);
                return true;
            }
        });

        ViewTreeObserver vtoCard = scrollParentCard.getViewTreeObserver();
        vtoCard.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int finalWidth = scrollParentCard.getMeasuredWidth();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) inputBoxView.getLayoutParams();
                params.width = finalWidth;
                inputBoxView.setLayoutParams(params);
                return true;
            }
        });

    }

    public void setMaxLines(int lines){
        this.maxLines = lines;
    }

    public ConstraintLayout getView(){
        return this.dialogsView;
    }

    public String[] parserColorTextArray(String[] allText){

        String[] outText = new String[allText.length];
        String activeColor = "FFFFFF";
        for (int i = 0; i < allText.length; i++) {

            String textChunk = allText[i];

            String[] textChunkSplited = textChunk.split("\\{");

            String outTextChunk = "";
            for (int j = 0; j < textChunkSplited.length; j++) {
                if(j == 0){
                    outTextChunk += "<font color='#" + activeColor + "'>" + textChunkSplited[j].replace(">","&#62;").replace("<","&#60;") + "</font>";
                    continue;
                }
                if(textChunkSplited[j].length() < 7){
                    outTextChunk += "<font color='#" + activeColor + "'>" + textChunkSplited[j].replace(">","&#62;").replace("<","&#60;") + "</font>";
                    continue;
                }
                String color = textChunkSplited[j].substring(0, 6);
                String ident = textChunkSplited[j].substring(6, 7);
                String contentText = textChunkSplited[j].length() > 7 ? textChunkSplited[j].substring(7) : "";

                if (!ident.equals("}")) {
                    outTextChunk += "<font color='#" + activeColor + "'>" + textChunkSplited[j].replace(">","&#62;").replace("<","&#60;") + "</font>";
                    continue;
                }
                activeColor = color;
                outTextChunk += "<font color='#" + color + "'>" + contentText + "</font>";
            }

            outText[i] = outTextChunk;
        }
        return outText;

    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = activity.getApplicationContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = activity.getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void SetVisibleDialog(int active){
        activeDialog = active == 1 ? true : false;

        if(activeDialog == true){
            dialogsView.setVisibility(View.VISIBLE);
        }else{
            dialogsView.setVisibility(View.INVISIBLE);

            contentButtonAutoLogin.setVisibility(View.INVISIBLE);
        }
    }

    public void onResponse(int response){
        if(!activeDialogResponse){
            return;
        }
        int lastTime = ((int)System.currentTimeMillis() / 1000);
        if(lastTimeClick == lastTime){
            lastTimeClick = lastTime;
            if(clickInSecond >= 2){
                System.out.println("click > 2....");
                return;
            }
            clickInSecond += 1;
        }else{
            lastTimeClick = lastTime;
            clickInSecond = 0;
        }
        if(dialogIdActive == 10000 && PublicInfo.goggleCodeInit == 1 && response == 0){
            inputEditText.setText("");
            //onClose();
            //buildDialog(10000, "{ffb94f} Успешно", "Ты добавил Google Authenticator в менеджер паролей.\nПри следующей авторизации тебе только нужно нажать 'Ввод' когда будет запрошен Google код.", "Закрыть", "", 0);
            MenuActivity.showCustomDialog(root, "Ты добавил Google Authenticator в менеджер паролей.\nПри следующей авторизации тебе только нужно нажать 'Ввод' когда будет запрошен Google код.", "Закрыть");
       //     System.out.println("MIHAIL ТЫ НАЖАЛ ДОБАВИТЬ В МЕНЕДЖЕР");
            DialogLoginFragment.SavePreferencesGoogle(PublicInfo.selectServerConnect,PublicInfo.getSelectServerConnectNick, PublicInfo.goggleCode);
            return;
        }

        activeDialogResponse = false;
        try {
            String text = "";
            if(selectedItem != -1){
                text = idOfItems[selectedItem];
            }else{
                text = inputEditText.getText().toString();
            }
            root.responseDialog(dialogIdActive, response, selectedItem, text.getBytes("windows-1251"));
       //     System.out.println("MIHAIL DIALOG: dialogIdActive: "+dialogIdActive+ "response: "+response+" selectedItem: "+ selectedItem+ "text: "+text);
            inputEditText.setText("");
            onClose();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
    }

    public void buildDialog(int dialogId, String title, String content, String button1, String button2, int typeDialog){
        selectedItem = -1;
        dialogIdActive = dialogId;
        activeDialogResponse = true;


        if(dialogId == 10000 && title.equals("{ffb94f}ОЧЕНЬ ВАЖНО!")) { //Проверка на добавление Google authenticator
            PublicInfo.goggleCode = Util.maxWordDeleteColor(content);
            button2 = "Добавить в менеджер";
            PublicInfo.goggleCodeInit = 1;
        }

        if(dialogId == 5044) {
            contentButtonAdmin.setVisibility(View.INVISIBLE);
        }

        if(typeDialog == 0 || typeDialog == 3 || typeDialog == 1) {
            if(content.length() <= 80 && (content.length() - content.replace("\n","").length()) <= 1) {
                this.contentTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                this.contentTextView.setTextSize(12);
            }else{
                this.contentTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                this.contentTextView.setTextSize(9);
            }

            this.contentView.setPadding(this.contentView.getPaddingLeft()
                    ,(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            15,
                            activity.getResources().getDisplayMetrics()
                    ),this.contentView.getPaddingRight(),0);
            contentScroll.setVisibility(View.VISIBLE);
            contentHeaderList.setVisibility(View.GONE);
            contentScrollList.setVisibility(View.GONE);
            typeSlider = TYPE_SLIDER_SCROLL_TEXT;
            inputBoxView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root.mKeyboardStandardManager.keyboardTextInput.setText(inputEditText.getText().toString());
                    root.mKeyboardStandardManager.setTextLastOnType(inputEditText.getText().toString(), 1);
                    root.mKeyboardStandardManager.setCallableEnter(new Runnable() {
                        @Override
                        public void run() {
                            inputEditText.setText(root.mKeyboardStandardManager.keyboardTextInput.getText().toString());
                            root.mKeyboardStandardManager.setVisible(0, 1, typeDialog == 3);
                        }
                    });
                    root.mKeyboardStandardManager.setCallableClose(new Runnable() {
                        @Override
                        public void run() {
                            root.mKeyboardStandardManager.setVisible(0, 1, typeDialog == 3);
                        }
                    });
                    root.mKeyboardStandardManager.setVisible(1, 1, typeDialog == 3);
                }
            });


            String convertedContent = "";
            String contentSplited[] = content.split("\\{");

            for (int i = 0; i < contentSplited.length; i++) {
                if (i == 0) {
                    convertedContent = contentSplited[i].replace(">","&#62;").replace("<","&#60;");
                    continue;
                }

                if(contentSplited[i].length() < 7){
                    convertedContent += "{" + contentSplited[i].replace(">","&#62;").replace("<","&#60;");
                    continue;
                }

                String color = contentSplited[i].substring(0, 6);
                String ident = contentSplited[i].substring(6, 7);
                String contentText = contentSplited[i].length() > 7 ? contentSplited[i].substring(7) : "";

                if (!ident.equals("}")) {
                    convertedContent += "{" + contentSplited[i].replace(">","&#62;").replace("<","&#60;");
                    continue;
                }

                convertedContent += "<font color='#" + color + "'>" + contentText.replace(">","&#62;").replace("<","&#60;") + "</font>";

            }
            contentTextView.setText(Html.fromHtml(convertedContent.replace("\n", "<br>").replace("\t", "&nbsp;")));
            button1Text.setText(button1);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrollParentCard.getLayoutParams();
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            scrollParentCard.setLayoutParams(params);


            String convertedTitle = "";

            String titleSplited[] = title.split("\\{");
            if (titleSplited.length > 0) {
                for (int i = 0; i < titleSplited.length; i++) {
                    if (i == 0) {
                        convertedTitle = "<font color='#FF9900'>" + titleSplited[i].replace(">","&#62;").replace("<","&#60;") + "</span>";
                        continue;
                    }

                    if(titleSplited[i].length() < 7){
                        convertedTitle += "{" + titleSplited[i].replace(">","&#62;").replace("<","&#60;");
                        continue;
                    }

                    String color = titleSplited[i].substring(0, 6);
                    String ident = titleSplited[i].substring(6, 7);
                    String contentText = titleSplited[i].length() > 7 ? titleSplited[i].substring(7) : "";

                    if (!ident.equals("}")) {
                        convertedTitle += "{" + titleSplited[i].replace(">","&#62;").replace("<","&#60;");
                        continue;
                    }

                    convertedTitle += "<font color='#" + color + "'>" + contentText.replace(">","&#62;").replace("<","&#60;") + "</font>";

                }

                Shader textShader = new LinearGradient(0, 0, 0, 0,
                        new int[]{
                                Color.parseColor("#FED605"),
                                Color.parseColor("#FF9900"),
                        }, null, Shader.TileMode.CLAMP);
                titleText.getPaint().setShader(textShader);
            } else {
                TextPaint paint = titleText.getPaint();
                float width = paint.measureText((String) (titleSplited[0]));

                Shader textShader = new LinearGradient(0, 0, width, ((TextView) dialogsView.findViewById(R.id.titleText)).getTextSize(),
                        new int[]{
                                Color.parseColor("#FED605"),
                                Color.parseColor("#FF9900"),
                        }, null, Shader.TileMode.CLAMP);
                titleText.getPaint().setShader(textShader);

                convertedTitle = titleSplited[0];
            }

            titleText.setText(Html.fromHtml(convertedTitle));

            button1Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onResponse(1);
                }
            });
            if (!button2.isEmpty()) {
                button2Frame.setVisibility(View.VISIBLE);
                button2Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onResponse(1);
                    }
                });
                button1Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onResponse(0);
                    }
                });
                button2Text.setText(button1);
                button1Text.setText(button2);
            } else {
                button2Frame.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout) dialogsView.findViewById(R.id.content));
                constraintSet.setHorizontalBias(R.id.contentButtonLayoutOne, 0.5f);
                constraintSet.applyTo((ConstraintLayout) dialogsView.findViewById(R.id.content));
            }

            if (typeDialog == 0) {
                setMaxLines(22);
                inputBoxView.setVisibility(View.GONE);
            } else {
                setMaxLines(16);
                inputBoxView.setVisibility(View.VISIBLE);

                if (typeDialog == 1) {
                    inputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                } else if (typeDialog == 3) {
                    inputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                if(dialogId == PublicInfo.dialogID_pass || dialogId == PublicInfo.dialogID_admPass || dialogId == PublicInfo.dialogID_google ) { //Вход по паролю
                    if(DialogLoginFragment.getNick(PublicInfo.getSelectServerConnectNick, PublicInfo.selectServerConnect))  {
                        //System.out.println("MIHAIL вход доступен!!!");
                        contentButtonAutoLogin.setVisibility(View.VISIBLE);
                    }

                }
            }
        }
        if(typeDialog == 5 || typeDialog == 2 || typeDialog == 4){
            typeSlider = TYPE_SLIDER_SCROLL_ITEMS;
            this.sliderView.setY(0);
            this.contentScrollList.scrollTo(0,0);

            this.contentView.setPadding(this.contentView.getPaddingLeft()
            ,(int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5,
                    activity.getResources().getDisplayMetrics()
            ),this.contentView.getPaddingRight(),0);

            contentLinearList.setMinimumWidth((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    350,
                    activity.getResources().getDisplayMetrics()
            ));
            contentHeaderList.setMinimumWidth(0);

            CardView.LayoutParams paramsContent = (CardView.LayoutParams) this.contentScrollList.getLayoutParams();
            paramsContent.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            paramsContent.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            this.contentScrollList.setLayoutParams(paramsContent);

            ConstraintLayout.LayoutParams paramsHeaders = (ConstraintLayout.LayoutParams) this.contentHeaderList.getLayoutParams();
            paramsHeaders.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            this.contentHeaderList.setLayoutParams(paramsHeaders);


            inputBoxView.setVisibility(View.GONE);
            contentScroll.setVisibility(View.GONE);
            contentScrollList.setVisibility(View.VISIBLE);
            if(typeDialog == 5){
                contentHeaderList.setVisibility(View.VISIBLE);
            }else{
                contentHeaderList.setVisibility(View.GONE);
            }

            String[] headerList = content.split("\n")[0].split("\t");
            String[] List = content.split("\n");
            java.util.List<String> listArr = new ArrayList<String>(Arrays.asList(List));
            if(typeDialog == 5){
                listArr.remove(0);
            }
            List = listArr.toArray(new String[listArr.size()]);

            int countButtons = 0;
            int countHeaders = 0;
            activeCountItems = List.length;
            String[][] ListStringsOfLines = new String[1][List.length];
            int countMaxOneParametr = 0;
            if(typeDialog == 5 || typeDialog == 4) {
                for (int i = 0; i < List.length; i++) {
                    if(List[i].isEmpty()){
                        activeCountItems -= 1;
                        continue;
                    }
                    String[] stringOfButtons = List[i].split("\t");
                    int count = stringOfButtons.length;

                    if(count > countMaxOneParametr){
                        countMaxOneParametr = count;
                    }

                }

                ListStringsOfLines = new String[countMaxOneParametr][activeCountItems];

                int offset = 0;
                for (int i = 0; i < List.length; i++) {
                    if(List[i].isEmpty()){
                        offset += 1;
                        continue;
                    }
                    String[] stringOfButtons = List[i].split("\t");
                    int count = stringOfButtons.length;
                    for (int j = 0; j < count; j++) {
                        ListStringsOfLines[j][i-offset] = stringOfButtons[j];
                    }
                }
            }
            if(typeDialog == 2){
                countMaxOneParametr = 1;
                ListStringsOfLines = new String[countMaxOneParametr][List.length];

                for (int i = 0; i < List.length; i++) {
                    if(List[i].isEmpty()){
                        continue;
                    }
                    ListStringsOfLines[0][i] = List[i];
                }
            }

            for (int i = 0; i < contentLinearList.getChildCount(); i++) {
                if (contentLinearList.getChildAt(i) instanceof LinearLayout) {
                    contentLinearList.removeViewAt(i);
                    i--;
                }
            }

            for (int i = 0; i < contentHeaderList.getChildCount(); i++) {
                if (contentHeaderList.getChildAt(i) instanceof TextView) {
                    contentHeaderList.removeViewAt(i);
                    i--;
                }
            }
            idOfItems = new String[List.length];
            if(ListStringsOfLines.length > 0){
                if(ListStringsOfLines[0].length > 0){
                    selectedItem = 0;
                }
            }
            for (int i = 0; i < ListStringsOfLines.length; i++) {
                LinearLayout container = new LinearLayout(this.activity);
                container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                container.setOrientation(LinearLayout.VERTICAL);
                if(i == ListStringsOfLines.length-1){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getLayoutParams();
                    params.weight = 1.0f;
                    container.setLayoutParams(params);
                }
                for (int j = 0; j < ListStringsOfLines[i].length; j++) {

                    TextView but = new TextView(this.activity);
                    but.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            26,
                            activity.getResources().getDisplayMetrics()
                    )));
                    Typeface typeface = ResourcesCompat.getFont(this.activity, R.font.gilroybold);
                    but.setTypeface(typeface);

                    String textNotInColor = "";
                    if(ListStringsOfLines[i][j] != null){

                        String[] textButtonSplited = ListStringsOfLines[i][j].split("\\{");
                        String textButtonParsered = "";


                        for (int z = 0; z < textButtonSplited.length; z++) {
                            if (z == 0) {
                                textButtonParsered = "<font color='#FFFFFF'>" + textButtonSplited[z].replace(">","&#62;").replace("<","&#60;") + "</font>";
                                textNotInColor += textButtonSplited[z];
                                continue;
                            }

                            if(textButtonSplited[z].length() < 7){
                                textButtonParsered += "{" + textButtonSplited[z].replace(">","&#62;").replace("<","&#60;");
                                textNotInColor += "{" + textButtonSplited[z];
                                continue;
                            }

                            String color = textButtonSplited[z].substring(0, 6);
                            String ident = textButtonSplited[z].substring(6, 7);
                            String contentText = textButtonSplited[z].length() > 7 ? textButtonSplited[z].substring(7) : "";

                            if (!ident.equals("}")) {
                                textButtonParsered += "{" + textButtonSplited[z].replace(">","&#62;").replace("<","&#60;");
                                textNotInColor += "{" + textButtonSplited[z];
                                continue;
                            }
                            textNotInColor += contentText;
                            textButtonParsered += "<font color='#" + color + "'>" + contentText.replace(">","&#62;").replace("<","&#60;") + "</font>";

                        }
                        but.setText(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+textButtonParsered));
                    }else{
                        but.setText(Html.fromHtml(""));
                    }

                    if(i == 0){
                        idOfItems[j] = textNotInColor;
                    }
                    but.setTextColor(0xFFFFFFFF);
                    but.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                    but.setGravity(Gravity.CENTER_VERTICAL);

                    int IDButton = j;
                    String[][] tempListStringsOfLines = ListStringsOfLines;
                    but.setOnTouchListener(new View.OnTouchListener() {

                        private GestureDetector gestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener(){
                            @Override
                            public boolean onDoubleTap(MotionEvent e) {
                                selectedItem = IDButton;
                                onResponse(1);
                                return super.onDoubleTap(e);
                            }
                        });

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            gestureDetector.onTouchEvent(event);
                            return false;
                        }
                    });

                    but.setOnClickListener(new DialogOnClickOnButtonList(this, j));
                    LinearLayout.LayoutParams paramsBut = (LinearLayout.LayoutParams) but.getLayoutParams();
                    if(j != ListStringsOfLines[i].length-1){
                        paramsBut.setMargins(0,0,0,(int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                marginItems,
                                activity.getResources().getDisplayMetrics()
                        ));
                    }
                    if(ListStringsOfLines.length == 1){
                        but.setBackgroundResource(R.drawable.flin_dialog_list_item_background_full);
                    }else{
                        if(i == 0){
                            but.setBackgroundResource(R.drawable.flin_dialog_list_item_background_left);

                        }else if(i == ListStringsOfLines.length-1){
                            but.setBackgroundResource(R.drawable.flin_dialog_list_item_background_right);
                        }else{
                            but.setBackgroundResource(R.drawable.flin_dialog_list_item_background_center);
                        }
                    }

                    but.setBackgroundTintList(ColorStateList.valueOf(0x40000000));

                    if(j == 0){
                        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), 0x40000000, 0xFFFFA51E);
                        colorAnimator.setDuration(300);
                        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                but.setBackgroundTintList(ColorStateList.valueOf((int)animation.getAnimatedValue()));
                            }
                        });
                        colorAnimator.start();
                    }
                    container.addView(but);

                }
                if(headerList.length-1 >= i) {
                    TextView headerOfContainer = new TextView(this.activity);
                    Typeface typeface = ResourcesCompat.getFont(this.activity, R.font.gilroybold);
                    headerOfContainer.setTypeface(typeface);

                    String[] textHeaderSplited = headerList[i].split("\\{");
                    String textHeaderParsered = "";

                    for (int z = 0; z < textHeaderSplited.length; z++) {
                        if (z == 0) {
                            textHeaderParsered = "<font color='#ABABAB'>" + textHeaderSplited[z].replace(">","&#62;").replace("<","&#60;") + "</font>";
                            continue;
                        }

                        if(textHeaderSplited[z].length() < 7){
                            textHeaderParsered += "{" + textHeaderSplited[z].replace(">","&#62;").replace("<","&#60;");
                            continue;
                        }

                        String color = textHeaderSplited[z].substring(0, 6);
                        String ident = textHeaderSplited[z].substring(6, 7);
                        String contentText = textHeaderSplited[z].length() > 7 ? textHeaderSplited[z].substring(7) : "";


                        if (!ident.equals("}")) {
                            textHeaderParsered += "{" + textHeaderSplited[z].replace(">","&#62;").replace("<","&#60;");
                            continue;
                        }

                        textHeaderParsered += "<font color='#" + color + "'>" + contentText.replace(">","&#62;").replace("<","&#60;") + "</font>";

                    }


                    headerOfContainer.setText(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;" + textHeaderParsered));
                    headerOfContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    headerOfContainer.setTextColor(0xFFABABAB);
                    headerOfContainer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    headerOfContainer.setGravity(Gravity.CENTER_VERTICAL);
                    contentHeaderList.addView(headerOfContainer);
                }
                contentLinearList.addView(container);
            }

            ConstraintLayout.LayoutParams paramsHeader = (ConstraintLayout.LayoutParams) contentHeaderList.getLayoutParams();
            paramsHeader.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            contentHeaderList.setLayoutParams(paramsHeader);

            CardView.LayoutParams paramsScroll = (CardView.LayoutParams) contentScrollList.getLayoutParams();
            paramsScroll.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            contentScrollList.setLayoutParams(paramsScroll);

            button1Text.setText(button1);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrollParentCard.getLayoutParams();
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            scrollParentCard.setLayoutParams(params);


            String convertedTitle = "";

            String titleSplited[] = title.split("\\{");
            if (titleSplited.length > 0) {
                for (int i = 0; i < titleSplited.length; i++) {
                    if (i == 0) {
                        convertedTitle = "<font color='#FF9900'>" + titleSplited[i].replace(">","&#62;").replace("<","&#60;") + "</font>";
                        continue;
                    }

                    if(titleSplited[i].length() < 7){
                        convertedTitle += "{" + titleSplited[i].replace(">","&#62;").replace("<","&#60;");
                        continue;
                    }

                    String color = titleSplited[i].substring(0, 6);
                    String ident = titleSplited[i].substring(6, 7);
                    String contentText = titleSplited[i].length() > 7 ? titleSplited[i].substring(7) : "";

                    if (!ident.equals("}")) {
                        convertedTitle += "{" + titleSplited[i].replace(">","&#62;").replace("<","&#60;");
                        continue;
                    }

                    convertedTitle += "<font color='#" + color + "'>" + contentText.replace(">","&#62;").replace("<","&#60;") + "</font>";

                }

                Shader textShader = new LinearGradient(0, 0, 0, 0,
                        new int[]{
                                Color.parseColor("#FED605"),
                                Color.parseColor("#FF9900"),
                        }, null, Shader.TileMode.CLAMP);
                titleText.getPaint().setShader(textShader);
            } else {
                TextPaint paint = titleText.getPaint();
                float width = paint.measureText((String) (titleSplited[0]));

                Shader textShader = new LinearGradient(0, 0, width, ((TextView) dialogsView.findViewById(R.id.titleText)).getTextSize(),
                        new int[]{
                                Color.parseColor("#FED605"),
                                Color.parseColor("#FF9900"),
                        }, null, Shader.TileMode.CLAMP);
                titleText.getPaint().setShader(textShader);

                convertedTitle = titleSplited[0];
            }

            titleText.setText(Html.fromHtml(convertedTitle));

            button1Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onResponse(1);
                }
            });
            if (!button2.isEmpty()) {
                button2Frame.setVisibility(View.VISIBLE);
                button2Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onResponse(1);
                    }
                });
                button1Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onResponse(0);
                    }
                });
                button2Text.setText(button1);
                button1Text.setText(button2);
            } else {
                button2Frame.setVisibility(View.GONE);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout) dialogsView.findViewById(R.id.content));
                constraintSet.setHorizontalBias(R.id.contentButtonLayoutOne, 0.5f);
                constraintSet.applyTo((ConstraintLayout) dialogsView.findViewById(R.id.content));
            }

        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.lastY = event.getY();
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            ScrollView scrollParent = scrollParentContent;
            LinearLayout scrollChild = scrollChildContent;
            if(typeSlider == TYPE_SLIDER_SCROLL_TEXT){
                scrollParent = scrollParentContent;
                scrollChild = scrollChildContent;
            }else if(typeSlider == TYPE_SLIDER_SCROLL_ITEMS){
                scrollParent = contentScrollList;
                scrollChild = contentLinearList;
            }
            if(sliderView.getY() + event.getY()-this.lastY <= 0){
                sliderView.setY(0);
                scrollParent.scrollTo(0, (int)((float)(scrollChild.getHeight()-scrollParent.getHeight())*((sliderView.getY() + event.getY()-this.lastY)/(scrollBarView.getHeight() - sliderView.getHeight()))));
                ignoreNextChangeScroll = true;
                return true;
            }
            if(sliderView.getY() + event.getY()-this.lastY >= scrollBarView.getHeight() - sliderView.getHeight()){
                sliderView.setY(scrollBarView.getHeight() - sliderView.getHeight());
                scrollParent.scrollTo(0, (int)((float)(scrollChild.getHeight()-scrollParent.getHeight())*((sliderView.getY() + event.getY()-this.lastY)/(scrollBarView.getHeight() - sliderView.getHeight()))));
                ignoreNextChangeScroll = true;
                return true;
            }
            sliderView.setY(sliderView.getY() + event.getY()-this.lastY);
            scrollParent.scrollTo(0, (int)((float)(scrollChild.getHeight()-scrollParent.getHeight())*((sliderView.getY() + event.getY()-this.lastY)/(scrollBarView.getHeight() - sliderView.getHeight()))));
            ignoreNextChangeScroll = true;
            //this.scrollParentContent.scrollTo(0, (int)((float)sliderView.getY() + (event.getY() - this.lastY)));
            this.lastY = event.getY();
            /*
            if(event.getY() >= sliderView.getY() + sliderView.getHeight()){
                System.out.println("onTocsafasafs 1");
                sliderView.setY(event.getY()-sliderView.getHeight());
                this.scrollParentContent.scrollTo(0, (int) event.getY()-sliderView.getHeight());
                //sliderView.setY(scrollBarView.getHeight()-sliderView.getHeight());
                //this.scrollParentContent.scrollTo(0, scrollBarView.getHeight()-sliderView.getHeight());
                return true;
            }
            if(event.getY() <= sliderView.getY()){
                sliderView.setY(event.getY());
                this.scrollParentContent.scrollTo(0, (int) event.getY());
            }
            if(event.getY() <= 0){
                System.out.println("onTocsafasafs 2");
                sliderView.setY(0);
                this.scrollParentContent.scrollTo(0, 0);
                return true;
            }*/
            //System.out.println("onTocsafasafs 3");
            //sliderView.setY(event.getY());
            //this.scrollParentContent.scrollTo(0, (int) event.getY());
            return true;
        }
        return false;
    }

    public void onClose(){
        SetVisibleDialog(0);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.scrollParentCard.getLayoutParams();
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        this.scrollParentCard.setLayoutParams(params);
        this.sliderView.setY(0);
        this.scrollParentContent.scrollTo(0,0);
        this.contentScrollList.scrollTo(0,0);

        selectedItem = -1;
        activeCountItems = 0;
    }


    @Override
    public boolean onPreDraw() {
        final int finalHeight = this.scrollParentContent.getMeasuredHeight();
        final int finalWidth = this.scrollParentContent.getMeasuredWidth();

        final int finalHeightContentItems = this.contentScrollList.getMeasuredHeight();
        final int finalWidthContentItems = this.contentScrollList.getMeasuredWidth();

        if(typeSlider == TYPE_SLIDER_SCROLL_TEXT){
            int mathHeight = contentTextView.getHeight() / contentTextView.getText().toString().split("\n").length;
            if(finalHeight > mathHeight * maxLines){
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.scrollParentCard.getLayoutParams();
                params.height = mathHeight * maxLines;
                this.scrollParentCard.setLayoutParams(params);

                ConstraintLayout.LayoutParams paramsScrollBar = (ConstraintLayout.LayoutParams) this.scrollBarView.getLayoutParams();
                paramsScrollBar.height = mathHeight * maxLines;
                this.scrollBarView.setLayoutParams(paramsScrollBar);

                FrameLayout.LayoutParams paramsSlider = (FrameLayout.LayoutParams) this.sliderView.getLayoutParams();
                paramsSlider.height = (int)((float)paramsScrollBar.height*(maxLines-2)/(this.contentTextView.getText().toString().split("\n").length+1));
                this.sliderView.setLayoutParams(paramsSlider);
                this.sliderView.setY(0);
                this.scrollParentContent.scrollTo(0,0);
                this.contentScrollList.scrollTo(0,0);


                this.scrollBarView.setVisibility(View.VISIBLE);

                this.contentView.setPadding((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2,
                        activity.getResources().getDisplayMetrics()
                ),this.contentView.getPaddingTop(),(int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20,
                        activity.getResources().getDisplayMetrics()
                ),0);


            }else{
                if(finalHeight != mathHeight * maxLines || this.contentScroll.getVisibility() == View.GONE){
                    this.scrollBarView.setVisibility(View.GONE);

                    this.contentView.setPadding((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20,
                            activity.getResources().getDisplayMetrics()
                    ),this.contentView.getPaddingTop(),(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20,
                            activity.getResources().getDisplayMetrics()
                    ),0);

                    ConstraintLayout.LayoutParams paramsHeader = (ConstraintLayout.LayoutParams) this.contentHeaderList.getLayoutParams();
                    paramsHeader.setMargins((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            0,
                            activity.getResources().getDisplayMetrics()
                    ),0,0,0);


                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.scrollParentCard.getLayoutParams();
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                    this.scrollParentCard.setLayoutParams(params);
                    this.scrollParentContent.scrollTo(0,0);
                }

            }
        }
        if(typeSlider == TYPE_SLIDER_SCROLL_ITEMS){
            if(finalHeightContentItems > (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    (heightItem * countItemsVisible + (marginItems * (countItemsVisible-1))),
                    activity.getResources().getDisplayMetrics()
            )){
                CardView.LayoutParams params = (CardView.LayoutParams) this.contentScrollList.getLayoutParams();
                params.height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        (heightItem * countItemsVisible + (marginItems * (countItemsVisible-1))),
                        activity.getResources().getDisplayMetrics()
                );
                this.contentScrollList.setLayoutParams(params);

                ConstraintLayout.LayoutParams paramsScrollBar = (ConstraintLayout.LayoutParams) this.scrollBarView.getLayoutParams();
                paramsScrollBar.height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        (heightItem * countItemsVisible + (marginItems * (countItemsVisible-1))),
                        activity.getResources().getDisplayMetrics()
                );
                this.scrollBarView.setLayoutParams(paramsScrollBar);

                FrameLayout.LayoutParams paramsSlider = (FrameLayout.LayoutParams) this.sliderView.getLayoutParams();
                paramsSlider.height = (int)((float)paramsScrollBar.height*(countItemsVisible)/(activeCountItems));
                this.sliderView.setLayoutParams(paramsSlider);
                this.sliderView.setY(0);
                this.scrollParentContent.scrollTo(0,0);
                this.contentScrollList.scrollTo(0,0);


                this.scrollBarView.setVisibility(View.VISIBLE);

                this.contentView.setPadding((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2,
                        activity.getResources().getDisplayMetrics()
                ),this.contentView.getPaddingTop(),(int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20,
                        activity.getResources().getDisplayMetrics()
                ),0);


            }else{
                if(finalHeightContentItems != (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        (heightItem * countItemsVisible + (marginItems * (countItemsVisible-1))),
                        activity.getResources().getDisplayMetrics()
                ) || this.contentScrollList.getVisibility() == View.GONE){
                    this.scrollBarView.setVisibility(View.GONE);

                    this.contentView.setPadding((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20,
                            activity.getResources().getDisplayMetrics()
                    ),this.contentView.getPaddingTop(),(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20,
                            activity.getResources().getDisplayMetrics()
                    ),0);

                    ConstraintLayout.LayoutParams paramsHeader = (ConstraintLayout.LayoutParams) this.contentHeaderList.getLayoutParams();
                    paramsHeader.setMargins((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            0,
                            activity.getResources().getDisplayMetrics()
                    ),0,0,0);



                    CardView.LayoutParams params = (CardView.LayoutParams) this.contentScrollList.getLayoutParams();
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                    this.contentScrollList.setLayoutParams(params);
                    this.contentScrollList.scrollTo(0,0);
                }
            }
        }
        return true;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(ignoreNextChangeScroll){
            ignoreNextChangeScroll = false;
            return;
        }
        ScrollView scrollParent = scrollParentContent;
        LinearLayout scrollChild = scrollChildContent;
        if(typeSlider == TYPE_SLIDER_SCROLL_TEXT){
            scrollParent = scrollParentContent;
            scrollChild = scrollChildContent;
        }else if(typeSlider == TYPE_SLIDER_SCROLL_ITEMS){
            scrollParent = contentScrollList;
            scrollChild = contentLinearList;
        }
        sliderView.setY((float)(scrollBarView.getHeight() - sliderView.getHeight())*((float) scrollY/(scrollChild.getHeight()-scrollParent.getHeight())));
    }
}