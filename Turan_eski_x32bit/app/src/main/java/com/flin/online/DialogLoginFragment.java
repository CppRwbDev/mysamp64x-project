package com.flin.online;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.flin.online.adapter.AdapterListBasic;
import com.flin.online.model.Servers;
import com.flin.core.DialogClientSettings;
import com.flinc.core.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import static com.flin.online.MenuActivity.sharedPassword;
import static com.flin.online.MenuActivity.sharedNickName;


public class DialogLoginFragment extends DialogFragment {

    private View root_view;
    private String NickPlayer;
    private String PasswordPlayer;
    private String PasswordAdmPlayer;
    Context appThis;
    int position;

    public static String[] NickName = new String[5];
    public static String[] pass = new String[5];
    public static String[] passADM = new String[5];
    public static int [] servers = new int[5];
    public static String [] googleCode = new String[5];


    public DialogLoginFragment() {

    }


    public DialogLoginFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        root_view = inflater.inflate(R.layout.dialog_autologin, container, false);
           ((FloatingActionButton) root_view.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final EditText editText_NickName = (EditText) root_view.findViewById(R.id.editText_NickName);
        final EditText editText_Password = (EditText) root_view.findViewById(R.id.editText_Password);
        final EditText editText_AdmPassword = (EditText) root_view.findViewById(R.id.editText_AdmPassword);
        MaterialButtonToggleGroup materialButtonToggleGroup = root_view.findViewById(R.id.toggleGroup);


        //ЗАКРЫЛБАГ
      /*  if(getSlotNick(position))  { //Проверка на пустоту заполняемости
            editText_NickName.setText(getNickList(position));
            editText_Password.setText(getPass(position));
            editText_AdmPassword.setText(getPassAdm(position));

            switch (getServer(position)){
                case 0: materialButtonToggleGroup.check(R.id.button3); break;
                case 1: materialButtonToggleGroup.check(R.id.button1); break;
                case 2: materialButtonToggleGroup.check(R.id.button2); break;
            }
        }
       */



        //Нажатие кноки
        MaterialRippleLayout btn_save_settings_auto_login = (MaterialRippleLayout) root_view.findViewById(R.id.bt_create_account);
        btn_save_settings_auto_login.setClickable(true);
        btn_save_settings_auto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NickPlayer = editText_NickName.getText().toString().trim();
                PasswordPlayer = editText_Password.getText().toString().trim();
                PasswordAdmPlayer = editText_AdmPassword.getText().toString().trim();
                if (PasswordPlayer.length() == 0) {
                    Toast.makeText(getContext(), "Вы не ввели пароль!", Toast.LENGTH_SHORT).show();
                }
                else if(NickPlayer.length() == 0) {
                    Toast.makeText(getContext(), "Вы не ввели ник!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        byte[] data = PasswordPlayer.getBytes("UTF-8");
                        String base1 = Base64.encodeToString(data, Base64.DEFAULT);
                        byte[] data2 = PasswordAdmPlayer.getBytes("UTF-8");
                        String base2 = Base64.encodeToString(data2, Base64.DEFAULT);
                        //showCustomDialog(getActivity(), NickPlayer, base1, base2);
                        int buttonId = materialButtonToggleGroup.getCheckedButtonId();
                        if(buttonId == R.id.button1) {
                            ListActivityServers.items.set(position, new Servers(NickPlayer,"Первый сервер", 1));
                            SavePreferences(position, 1, NickPlayer, base1, base2);
                        }
                        if(buttonId == R.id.button2) {
                            ListActivityServers.items.set(position, new Servers(NickPlayer,"Второй сервер", 2));
                            SavePreferences(position, 2, NickPlayer, base1, base2);
                        }
                        if(buttonId == R.id.button3) {
                            ListActivityServers.items.set(position, new Servers(NickPlayer,"Тестовый сервер", 0));
                            SavePreferences(position, 0, NickPlayer, base1, base2);
                        }
                        ListActivityServers.mAdapter.notifyDataSetChanged();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
            }
        });
        return root_view;
    }


    public static void showCustomDialog(Context appThis, String nickPlayer, String pass, String passAdm) {
        final Dialog dialog = new Dialog(appThis);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SavePreferences(1, "DDSSDFG",pass);
                //SavePreferences("DDSSDFGS",passAdm);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static boolean getSlotNick(int id){
        if(getNickList(id).equals("none") ){
            return false;
        }
        return true;
    }

    public static boolean getNick(String nick, int server){
      /*  for (int i = 0; i < 5; i++){
            int s = 0;
            if(getNickList(i).equals(nick)) {
                s++;
                //System.out.println("MIHAIL nick name совпал Nick проверки["+i+"]:"+ nick);
            }
            if(getServer(i) == server) s++;
            if(s == 2) {
                return true;
            }
        }*/
        return false;
    }

    public static String getNickLoginPass(String nick, int server){
        String p = null;
        for (int i = 0; i < 5; i++){
            int s = 0;
            if(getNickList(i).equals(nick)) {
                s++;
                //System.out.println("MIHAIL nick name совпал Nick проверки["+i+"]:"+ nick);
            }
            if(getServer(i) == server) s++;
            if(s == 2) {
                byte[] decodeValuePass = Base64.decode(pass[i], Base64.DEFAULT);
                p = new String(decodeValuePass);
                //System.out.println("MIHAIL decodeValuePass"+p);
                break;
            }
        }
        return p;
    }
    public static String getNickLoginPassAdm(String nick, int server){
        String p = null;
        for (int i = 0; i < 5; i++){
            int s = 0;
            if(getNickList(i).equals(nick)) {
                s++;
                //System.out.println("MIHAIL nick name совпал Nick проверки["+i+"]:"+ nick);
            }
            if(getServer(i) == server) s++;
            if(s == 2) {
                byte[] decodeValuePass = Base64.decode(passADM[i], Base64.DEFAULT);
                p = new String(decodeValuePass);
                //System.out.println("MIHAIL decodeValuePass Adm"+p);
                break;
            }
        }
        return p;
    }
    public static String getNickLoginGoogle(String nick, int server){
        String p = null;
        for (int i = 0; i < 5; i++){
            int s = 0;
            if(getNickList(i).equals(nick)) {
                s++;
                //System.out.println("MIHAIL nick name совпал Nick проверки["+i+"]:"+ nick);
            }
            if(getServer(i) == server) s++;
            if(s == 2) {
                byte[] decodeValuePass = Base64.decode(googleCode[i], Base64.DEFAULT);
                p = new String(decodeValuePass);
                //System.out.println("MIHAIL decodeValuePass Google"+p);
                break;
            }
        }
        return p;
    }


    public static String getNickList(int id){
        return NickName[id];
    }

    public static String getPass(int id){
        byte[] decodeValuePass = Base64.decode(pass[id], Base64.DEFAULT);
        String p = new String(decodeValuePass);
        return p;
    }
    public static String getPassAdm(int id){
        byte[] decodeValuePass = Base64.decode(passADM[id], Base64.DEFAULT);
        String p = new String(decodeValuePass);
        return p;
    }

    public static int getServer(int id){
        return servers[id];
    }

    public static String getServerStr(int id){
        String nameServer = null;
        switch (servers[id]){
            case 0: {
                nameServer = "Тестовый сервер";
                break;
            }
            case 1: {
                nameServer = "Первый сервер";
                break;
            }
            case 2:{
                nameServer = "Второй сервер";
                break;
            }
        }
        return nameServer;
    }

    public static void LoadPreferences() {
        System.out.println("MIHAIL LoadPreferences");
        servers[0] = sharedNickName.getInt("IUHW", -1);
        servers[1] = sharedNickName.getInt("IUSJD", -1);
        servers[2] = sharedNickName.getInt("WUFWUFH", -1);
        servers[3] = sharedNickName.getInt("FAWFSS", -1);
        servers[4] = sharedNickName.getInt("AWFAFV", -1);

        NickName[0] = sharedNickName.getString("IUHWAIDHUAWIUD", "none");
        NickName[1] = sharedNickName.getString("AWDZIJZCOJ", "none");
        NickName[2] = sharedNickName.getString("SAOSAHUAZHCZ", "none");
        NickName[3] = sharedNickName.getString("SUIHIHSIZHVV", "none");
        NickName[4] = sharedNickName.getString("AIOFJAJWOFJIOOFA", "none");

        googleCode[0] = sharedNickName.getString("DGESJIIOJGIDS", "none");
        googleCode[1] = sharedNickName.getString("SEEEEFUIGVHEHGF", "none");
        googleCode[2] = sharedNickName.getString("WAIOJFIOAWJIOFA", "none");
        googleCode[3] = sharedNickName.getString("IEJOSIOFSJOIESEF", "none");
        googleCode[4] = sharedNickName.getString("JKSUHUIHEUFIHSIU", "none");

        pass[0] = sharedPassword.getString("DDSODSOK", "none");
        pass[1] = sharedPassword.getString("DXXMZKSOK", "none");
        pass[2] = sharedPassword.getString("KDKSLSKMCKLS", "none");
        pass[3] = sharedPassword.getString("DSSSSDSOKSDOSO", "none");
        pass[4] = sharedPassword.getString("DSSSSDSOKSDOSO", "none");

        passADM[0] = sharedPassword.getString("DSAIJSASIS", "none");
        passADM[1] = sharedPassword.getString("DSJISKASIS", "none");
        passADM[2] = sharedPassword.getString("KOSPSXDPAQAA", "none");
        passADM[3] = sharedPassword.getString("UHUIHIHUIHUHDSAA", "none");
        passADM[4] = sharedPassword.getString("AUWDAWUAIDWUHA", "none");
        //System.out.println("MIHAIL LoadPreferences syc");
     //   if(getNick("Mihail_Dimkov", 1)) System.out.println("MIHAIL найден никккк");
      //  if(getNick("Mihail_Dimssskov", 1)) System.out.println("MIHAIL НЕЕ найден никккк");
    }



    public static void SavePreferencesGoogle(int server, String nick, String googleText) {
        SharedPreferences.Editor editorTwo = sharedNickName.edit();
        int id = -1;
        for (int i = 0; i < 5; i++){
            int s = 0;
            if(getNickList(i).equals(nick)) {
                s++;
                //System.out.println("MIHAIL nick name совпал Nick проверки["+i+"]:"+ nick);
            }
            if(getServer(i) == server) s++;
            if(s == 2) {
                id = i;
                break;
            }
        }
        if(id == -1) return;
        try {
            byte[] data = googleText.getBytes("UTF-8");
            googleText = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        googleCode[id] = googleText;
        switch (id){
            case 0: {
                editorTwo.putString("DGESJIIOJGIDS", googleText);
                break;
            }
            case 1: {
                editorTwo.putString("SEEEEFUIGVHEHGF", googleText);
                break;
            }
            case 2: {
                editorTwo.putString("WAIOJFIOAWJIOFA", googleText);
                break;
            }
            case 3: {
                editorTwo.putString("IEJOSIOFSJOIESEF", googleText);
                break;
            }
            case 4: {
                editorTwo.putString("JKSUHUIHEUFIHSIU", googleText);
                break;
            }
        }
        editorTwo.commit();
    }
    public static void SavePreferences(int id,  int server, String nick, String passs, String passsADM) {
        SharedPreferences.Editor editor = sharedPassword.edit();
        SharedPreferences.Editor editorTwo = sharedNickName.edit();

        NickName[id] = nick;
        pass[id] = passs;
        passADM[id] = passsADM;
        servers[id] = server;

        switch (id){
            case 0: {
                editorTwo.putInt("IUHW", server);
                editorTwo.putString("IUHWAIDHUAWIUD", nick);
                editor.putString("DDSODSOK", passs);
                editor.putString("DSAIJSASIS", passsADM);
                editor.putString("GGIEGJGESGEEG", passsADM);
                break;
            }
            case 1: {
                editorTwo.putInt("IUSJD", server);
                editorTwo.putString("AWDZIJZCOJ", nick);
                editor.putString("DXXMZKSOK", passs);
                editor.putString("DSJISKASIS", passsADM);
                break;
            }
            case 2: {
                editorTwo.putInt("WUFWUFH", server);
                editorTwo.putString("SAOSAHUAZHCZ", nick);
                editor.putString("KDKSLSKMCKLS", passs);
                editor.putString("KOSPSXDPAQAA", passsADM);
                break;
            }
            case 3: {
                editorTwo.putInt("FAWFSS", server);
                editorTwo.putString("SUIHIHSIZHVV", nick);
                editor.putString("DSSSSDSOKSDOSO", passs);
                editor.putString("UHUIHIHUIHUHDSAA", passsADM);
                break;
            }
            case 4: {
                editorTwo.putInt("AWFAFV", server);
                editorTwo.putString("AIOFJAJWOFJIOOFA", nick);
                editor.putString("DSSSSDSOKSDOSO", passs);
                editor.putString("AUWDAWUAIDWUHA", passsADM);
                break;
            }
        }
        editor.commit();
        editorTwo.commit();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LoadPreferences();

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}