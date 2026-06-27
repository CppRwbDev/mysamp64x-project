package com.flin.online.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.SetingsActivity;
import com.flin.online.test;
import com.flinc.core.R;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ChangeFontDialog extends DialogFragment {

  private ListView listView;

  private String pathSettings;
  private TextView textViewNameFont;
  private String pathGame;
  MoreListAdapter listAdapter;
  String[] myArrayFont;
  ArrayList<String> arrayListItem;
  Context contextLocal;
  public ChangeFontDialog(Context context, String pathGameDir, String pathSettingsDir, TextView textViewFont) {
    // Required empty public constructor
    contextLocal = context;
    pathGame = pathGameDir;
    pathSettings = pathSettingsDir;
    textViewNameFont = textViewFont;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
   // System.out.println("MIHAIL ChangeFontDialog PathSettings: "+pathGame+pathSettings);
    arrayListItem = new ArrayList<>();
    return inflater.inflate(R.layout.fragment_change_font_dialog, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


    listView = view.findViewById(R.id.font_blank_list);

    String[] myArrayFont = {"Arial", "Courier New", "Callibri", "Engravers MT", "Fixedsys", "Comic Sans MS", "Georgia", "Impact", "Lucida Console", "Lucida Sans Unicode", "Palatino Linotype", "Tahoma", "Times New Roman", "Trebuchet MS", "Verdana", "Monotype Corsiva", "Franklin Gothic Medium", "Segoe UI", "Segoe Script", "Palatino Linotype", "Arial Black"};
       for (int i = 0; i < myArrayFont.length; i++) {
         arrayListItem.add(myArrayFont[i]);
       }

    listAdapter = new MoreListAdapter(contextLocal);
    listView.setAdapter(listAdapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        System.out.println();

        try {
          SaveFont(myArrayFont[position]);
        } catch (IOException e) {
          e.printStackTrace();
          Toast toast = Toast.makeText(getContext(),
                  "Произошла ошибка сохранения: "+e,Toast.LENGTH_LONG);
          toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
          toast.show();
        }
      }
    });

  }


  class MoreListAdapter extends ArrayAdapter<String> {
    private boolean[] selections;

    MoreListAdapter(Context v) {
      super(v, R.layout.fragment_change_font_dialog, arrayListItem);
      this.selections = new boolean[arrayListItem.size()];
    }


    public View getView(int position, View convertView, ViewGroup parent) {
      View row;

      if (convertView == null) {
        LayoutInflater inflater = getLayoutInflater();
        row = inflater.inflate(R.layout.fragment_change_font_text, parent, false);
      } else {
        row = convertView;
      }
      TextView textviewChangeFontSettings = (TextView) row.findViewById(R.id.textviewChangeFontSettings);
      Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/"+getItem(position)+".ttf");
      textviewChangeFontSettings.setTypeface(font);
      textviewChangeFontSettings.setText(getItem(position));

      return row;
    }
  }



  private void SaveFont(String nameFont) throws IOException {
    String nameFontTTF = nameFont+".ttf";
    File dir = new File(pathGame+"/files/SAMP/fonts/"+nameFontTTF);
    if (dir.exists()){
      Wini ini = new Wini(new File(pathGame+pathSettings));
      //  System.out.println("МИХАИЛ лог save Font"+pathGame+pathSettings);
      ini.put("gui", "Font", nameFontTTF);
      ini.store();
      Toast toast = Toast.makeText(getContext(),
              "Шрифт изменен на "+nameFont+"!",Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
      toast.show();
      textViewNameFont.setText(""+nameFont);
    }else {
      Toast toast = Toast.makeText(getContext(),
              "Шрифт "+nameFont+" не найден в каталоге игры!",Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
      toast.show();
    }
    dismiss();
  }

}