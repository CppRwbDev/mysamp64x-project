package com.flin.online;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flin.online.jsonenter.PublicInfo;
import com.flinc.core.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SelectMods extends AppCompatActivity {

    ArrayList<String> arrayListItemID;
    ArrayList<String> arrayListItemName;
    ArrayList<String> arrayListItemDescription;
    ArrayList<String> arrayListItemDescriptionFull;
    ArrayList<String> arrayListImageURL1;
    ArrayList<String> arrayListImageURL2;
    ArrayList<String> arrayListImageURL3;
    ArrayList<String> arrayListVersion;
    ArrayList<String> arrayListVersionName;
    ArrayList<String> arrayListVersionUpdate;
    ArrayList<String> arrayListVersionArchive;
    ArrayList<String> arrayListVersionArcCount;
    ArrayList<String> arrayListVersionURL;

    private ListModsAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mods);

        TextView infoVersionApp = (TextView) findViewById(R.id.textViewVersion);
        infoVersionApp.setText("v"+ PublicInfo.VersionNameAppStatic+" (Build "+ PublicInfo.VersionAppStatic+")");

        //Меню
        //Нажатие меню настроек
        TextView imageDownLeftMenu = (TextView) findViewById(R.id.imageDownLeftMenu);
        imageDownLeftMenu.setClickable(true);
        imageDownLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОБНОВИТЬКОД
                Intent intent = new Intent(SelectMods.this, SetingsActivity.class);
                startActivity(intent);
            }
        });

        //Нажатие меню старта игры
        FloatingActionButton imageDownCenterMenu = (FloatingActionButton) findViewById(R.id.imageDownCenterMenu);
        imageDownCenterMenu.setClickable(true);
        imageDownCenterMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMods.this, MenuActivity.class);
                startActivity(intent);
            }

        });

        //Нажатие меню Доната
        TextView imageDownRightMenu = (TextView) findViewById(R.id.imageDownRightMenu);
        imageDownRightMenu.setClickable(true);
        imageDownRightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMods.this, DonateActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.dialog_list_mods);
        listView.setVisibility(View.VISIBLE);
        arrayListItemID = new ArrayList<>();
        arrayListItemName = new ArrayList<>();
        arrayListItemDescription = new ArrayList<>();
        arrayListItemDescriptionFull = new ArrayList<>();
        arrayListImageURL1 = new ArrayList<>();
        arrayListImageURL2 = new ArrayList<>();
        arrayListImageURL3 = new ArrayList<>();
        arrayListVersion = new ArrayList<>();
        arrayListVersionName = new ArrayList<>();
        arrayListVersionUpdate = new ArrayList<>();
        arrayListVersionArchive = new ArrayList<>();
        arrayListVersionArcCount = new ArrayList<>();
        arrayListVersionURL = new ArrayList<>();


        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray rootJSON = new JSONArray(new JSONTokener(PublicInfo.jsonMods));
            for (int i = 0; i < rootJSON.length(); i++) {
                JSONObject o = rootJSON.getJSONObject(i);
                arrayListItemID.add(o.getString("id"));
                arrayListItemName.add(o.getString("name"));
                arrayListItemDescription.add(o.getString("description"));
                arrayListItemDescriptionFull.add(o.getString("descriptionFull"));
                arrayListImageURL1.add(o.getString("img_1"));
                arrayListImageURL2.add(o.getString("img_2"));
                arrayListImageURL3.add(o.getString("img_3"));
                arrayListVersion.add(o.getString("version"));
                arrayListVersionName.add(o.getString("versionName"));
                arrayListVersionUpdate.add(o.getString("versionUpdate"));
                arrayListVersionArchive.add(o.getString("archive"));
                arrayListVersionArcCount.add(o.getString("archiveCount"));
                arrayListVersionURL.add(o.getString("url"));

                System.out.println("Mihail ID: "+o.getString("id")+" NameJson:"+o.getString("name")+" FullText:"+ o.getString("descriptionFull") );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*arrayListItemName.add("Стандартная");
        arrayListItemDescription.add("Стандартная версия игры от Flin Online");

        arrayListItemName.add("Зимняя сборка");
        arrayListItemDescription.add("Замена текстур на зимнюю атмосферу");*/


        listAdapter = new ListModsAdapter();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                System.out.println("MIHAIL "+"Items["+arg2+"]"+ arrayListItemName.get(arg2));
                //arg1.setBackgroundResource(R.drawable.dialog_bac_list_view_click);
                listAdapter.switchSelection(arg2);
              //  mSelected = arg2;
                Intent intent = new Intent(SelectMods.this, ScrollingMods.class);
                intent.putExtra("mod_id", arrayListItemID.get(arg2));
                intent.putExtra("mod_name", arrayListItemName.get(arg2));
                intent.putExtra("mod_full_description", arrayListItemDescriptionFull.get(arg2));
                intent.putExtra("mod_install", 1);
                String[] images = { ""+arrayListImageURL1.get(arg2),  ""+arrayListImageURL2.get(arg2),  ""+arrayListImageURL3.get(arg2)};
                intent.putExtra("mod_img", images);
                intent.putExtra("mod_version", arrayListVersion.get(arg2));
                intent.putExtra("mod_versionName", arrayListVersionName.get(arg2));
                intent.putExtra("mod_versionUpdate", arrayListVersionUpdate.get(arg2));
                intent.putExtra("mod_archive", arrayListVersionArchive.get(arg2));
                intent.putExtra("mod_archive_count", Integer.parseInt(arrayListVersionArcCount.get(arg2)));
                System.out.println("MIHAIL mod_archive_count mod_archive_count"+ arrayListVersionArcCount.get(arg2));
                intent.putExtra("mod_url", arrayListVersionURL.get(arg2));

                startActivity(intent);
            }

        });
    }


    class ListModsAdapter extends ArrayAdapter<String> {
        private boolean[] selections;

        ListModsAdapter() {
            super(SelectMods.this, R.layout.mods_list_item, arrayListItemName);
            this.selections = new boolean[arrayListItemName.size()];
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
                row = inflater.inflate(R.layout.mods_list_item, parent, false);
            } else {
                row = convertView;
            }
           /*TextView textViewItemOne = (TextView) row.findViewById(R.id.textViewDialogListItemOne);
            TextView textViewItemTwo = (TextView) row.findViewById(R.id.textViewDialogListItemTwo);
            TextView textViewItemThree = (TextView) row.findViewById(R.id.textViewDialogListItemThree);
            TextView textViewItemFour = (TextView) row.findViewById(R.id.textViewDialogListItemFour);
            LinearLayout linearLayoutDialogItemTabOne = (LinearLayout) row.findViewById(R.id.linearLayoutDialogItemTabOne);
            */


           /* String[] subStrTab = getItem(position).replace("\\t","<bb>").split("<bb>");
            for(int i = 0; i < subStrTab.length; i++) {
                // arrayListItem.add(subStr[i]);
                if(i == 0) {
                    textViewItemOne.setVisibility(View.VISIBLE);
                    textViewItemOne.setText(subStrTab[i]);
                    ViewGroup.LayoutParams params = linearLayoutDialogItemTabOne.getLayoutParams();
                    params.height = 290;
                    params.width = 300;
                    linearLayoutDialogItemTabOne.setLayoutParams(params);

                    System.out.println("MIHAIL TAB LIST ["+i+"]:"+subStrTab[i]+ "Размер:"+textViewItemOne.getMeasuredWidth());
                }
                if(i == 1){
                    textViewItemTwo.setVisibility(View.VISIBLE);
                    textViewItemTwo.setText(subStrTab[i]);
                    System.out.println("MIHAIL TAB LIST ["+i+"]:"+subStrTab[i]+ "Размер:"+textViewItemTwo.getMeasuredWidth());

                }
                if(i == 2) {
                    textViewItemThree.setVisibility(View.VISIBLE);
                    textViewItemThree.setText(subStrTab[i]);
                    System.out.println("MIHAIL TAB LIST ["+i+"]:"+subStrTab[i]+ "Размер:"+textViewItemThree.getMeasuredWidth());

                }
                if(i == 3) {
                    textViewItemFour.setVisibility(View.VISIBLE);
                    textViewItemFour.setText(subStrTab[i]);
                }

            }*/



         /*   String ttr1 = getItem(position).replace( "\\n", "<br>");
            String ttr = ttr1.replace( "{", "<font color='#");
            String ttrd = ttr.replace("}", "'>");
            String ttrdd =ttrd.replace("<font ", "</font><font ");

            tv.setText(Html.fromHtml(ttrdd+"</font>"));*/

           /* if(mSelected == position) {
                tv.setBackgroundDrawable( getResources().getDrawable(R.drawable.dialog_bac_list_view_click) );
            }
            else {
                tv.setBackgroundDrawable( getResources().getDrawable(R.drawable.dialog_bac_list_view) );
            }*/

            TextView textViewNameMode = (TextView) row.findViewById(R.id.textViewNameMode);
            textViewNameMode.setText(getItem(position));

            TextView textViewDescriptionMode = (TextView) row.findViewById(R.id.textViewDescriptionMode);
            textViewDescriptionMode.setText(arrayListItemDescription.get(position));

            return row;
        }
    }


}