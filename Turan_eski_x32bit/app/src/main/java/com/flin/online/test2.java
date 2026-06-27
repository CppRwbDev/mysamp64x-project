package com.flin.online;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test2 extends AppCompatActivity {

    ArrayList<String> arrayListItem;
    private MoreListAdapter listAdapter;
    private int mSelected;
    String ttext = "Deagle\\t$5000\\t100\\nSawnoff\\t$5000\\t100\\nPistol\\t$1000\\t50";
    //String ttext = "{ffb94f}[1]{ffffff} Статистика персонажа\\n{ffb94f}[2]{ffffff} Инвентарь{ffffff}\\n{ffb94f}[3]{ffffff} Настройки\\n{ffb94f}[4]{ffffff} Помощь по игре\\n{ffb94f}[5]{ffffff} Команды сервера\\n{ffb94f}[6]{ffffff} {FFFF00}Связь с администрацией{ffffff}\\n{ffb94f}[7]{ffffff} Навыки\\n{ffb94f}[8]{ffffff} Улучшения\\n{ffb94f}[9]{ffffff} Пожертвования\\n{ffb94f}[10]{ffffff} Ввести бонус-код\\n{ffb94f}[11]{ffffff} Ввести промокод\\n{ffb94f}[12]{ffffff} Задания, достижения и награды\\n{ffb94f}[13]{ffffff} Battle Pass\\n{ffb94f}[14]{ffffff} {AA3333}Донат\\n{ffb94f}[15]{ffffff} {FFFF00}Рулетка\\n{ffb94f}[16]{ffffff} {33AA33}Кейсы\\n{ffb94f}[17]{ffffff} Горячие клавиши\\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog);

        TextView textView_text_dialog = (TextView) findViewById(R.id.textView_text_dialog);
        textView_text_dialog.setVisibility(View.GONE);

        ListView listView = (ListView) findViewById(R.id.dialog_list_view);
        listView.setVisibility(View.VISIBLE);
        arrayListItem = new ArrayList<>();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            try {
                List<String> lines = Arrays.asList(ttext, "DIMKOV");
                Path file = Paths.get(Environment.getExternalStorageDirectory() + "/Android/data/" + PublicInfo.checkReleasePackageName + "/ddd.txt");
                Files.write(file, lines, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] subStr = ttext.replace("\\n","<br>").split("<br>");
        for(int i = 0; i < subStr.length; i++) {
            arrayListItem.add(subStr[i]);
        }

        listAdapter = new MoreListAdapter();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                System.out.println("MIHAIL "+"Items["+arg2+"]"+ arrayListItem.get(arg2));
                //arg1.setBackgroundResource(R.drawable.dialog_bac_list_view_click);
                listAdapter.switchSelection(arg2);
                mSelected = arg2;

            }

        });

    }

    class MoreListAdapter extends ArrayAdapter<String> {
        private boolean[] selections;

        MoreListAdapter() {
            super(test2.this, R.layout.dialog_list_item, arrayListItem);
            this.selections = new boolean[arrayListItem.size()];
        }

        public void switchSelection(int position){
            selections[position] = !selections[position];
            //оповещаем адаптер об изменениях, чтобы он обновил все элементы списка.
            notifyDataSetChanged();
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            View row;

            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.dialog_list_item, parent, false);
            } else {
                row = convertView;
            }
            TextView tv = (TextView) row.findViewById(R.id.textViewDialogListItem);
            String ttr1 = getItem(position).replace( "\\n", "<br>");
            String ttr = ttr1.replace( "{", "<font color='#");
            String ttrd = ttr.replace("}", "'>");
            String ttrdd =ttrd.replace("<font ", "</font><font ");

            tv.setText(Html.fromHtml(ttrdd+"</font>"));

            if(mSelected == position) {
                tv.setBackgroundDrawable( getResources().getDrawable(R.drawable.dialog_bac_list_view_click) );
            }
            else {
                tv.setBackgroundDrawable( getResources().getDrawable(R.drawable.dialog_bac_list_view) );
            }
            return row;
        }
    }



    public class NamesAdapter extends ArrayAdapter<String> {
        private String[] names;
        private boolean[] selections;
        private LayoutInflater inflater;
        private final int colorWhite = Color.WHITE;
        private final int colorGray = Color.GRAY;

        public NamesAdapter(String[] names, Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
            this.names = names;
            this.selections = new boolean[names.length];
        }

        /**
         * Снимает выделение если элемент уже выделен и наоборот
         * @param position
         */
        public void switchSelection(int position){
            selections[position] = !selections[position];
            //оповещаем адаптер об изменениях, чтобы он обновил все элементы списка.
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Здесь создаем View и заполняем его данными.
            convertView = inflater.inflate(R.layout.dialog_list_item, parent, false);
            boolean isSelected = selections[position];
            String name = names[position];
            String ttr1 = name.replace( "\\n", "<br>");
            String ttr = ttr1.replace( "{", "<font color='#");
            String ttrd = ttr.replace("}", "'>");
            String ttrdd =ttrd.replace("<font ", "</font><font ");
            TextView textView = (TextView) convertView.findViewById(R.id.textViewDialogListItem);



            textView.setText(Html.fromHtml(ttrdd+"</font>"));



            //Ключевой момент - просто ставим цвет фона в зависимости от значения переменной selection
          /*  if (isSelected){
                convertView.setBackgroundColor(colorGray);
            }else {
                convertView.setBackgroundColor(colorWhite);
            }*/
            return convertView;
        }

        /**
         * возвращает количество элементов, которые должны быть отображены в нашем списке.
         * если его не переопределить, список останется пустым
         * @return
         */
        @Override
        public int getCount() {
            return names.length;
        }

    }


}
