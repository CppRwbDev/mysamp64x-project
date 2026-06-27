package com.flin.online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flin.online.adapter.AdapterListBasic;
import com.flin.online.model.Servers;
import com.flinc.core.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListActivityServers extends AppCompatActivity {


    private View parent_view;

    private RecyclerView recyclerView;
    public static AdapterListBasic mAdapter;
    public static List<Servers> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_servers);
        parent_view = findViewById(android.R.id.content);


        DialogLoginFragment.LoadPreferences();

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Менеджер паролей");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }

    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListServer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        items =  new ArrayList<Servers>();
        if(DialogLoginFragment.getNickList(0).equals("none")){
            items.add(new Servers("Не задан","",0));
        }
        else items.add(new Servers(DialogLoginFragment.getNickList(0),DialogLoginFragment.getServerStr(0),DialogLoginFragment.getServer(0)));

        if(DialogLoginFragment.getNickList(1).equals("none")){
            items.add(new Servers("Не задан","",0));
        }
        else items.add(new Servers(DialogLoginFragment.getNickList(1),DialogLoginFragment.getServerStr(1),DialogLoginFragment.getServer(1)));

        if(DialogLoginFragment.getNickList(2).equals("none")){
            items.add(new Servers("Не задан","",0));
        }
        else items.add(new Servers(DialogLoginFragment.getNickList(2),DialogLoginFragment.getServerStr(2),DialogLoginFragment.getServer(2)));

        if(DialogLoginFragment.getNickList(3).equals("none")){
            items.add(new Servers("Не задан","",0));
        }
        else items.add(new Servers(DialogLoginFragment.getNickList(3),DialogLoginFragment.getServerStr(3),DialogLoginFragment.getServer(3)));

        if(DialogLoginFragment.getNickList(4).equals("none")){
            items.add(new Servers("Не задан","",0));
        }
        else items.add(new Servers(DialogLoginFragment.getNickList(4),DialogLoginFragment.getServerStr(4),DialogLoginFragment.getServer(4)));

        mAdapter = new AdapterListBasic(this, items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterListBasic.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Servers obj, int position) {
                Context wrapper = new ContextThemeWrapper(ListActivityServers.this, R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper, view);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() ==  R.id.action_pop_menu_delete){
                            //items.remove(position);
                            ListActivityServers.items.set(position, new Servers("Не задано","", 2));
                            DialogLoginFragment.SavePreferences(position, 2, "none", "1", "1");
                            mAdapter.notifyDataSetChanged();
                        }
                        if(item.getItemId() ==  R.id.action_pop_menu_edit){

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            DialogLoginFragment newFragment = new DialogLoginFragment(position);
                            //newFragment.setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE,R.style.BottomSheetTheme);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.menu_item_server_pass);
                popupMenu.show();

            }
        });

    }

    public static List<Servers> getData(Context ctx) {
        List<Servers> items = new ArrayList<>();
//       TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
  //      String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        String[] nick = {"Mihail_Dimkov", "Misha_Dimkov", "Ford", "Mazda"};
        String[] servers = {"Первый сервер", "Второй сервер", "Тестовый"};
        for (int i = 0; i < 2; i++) {
            Servers obj = new Servers();
            obj.image = 1;
            obj.name =  nick[i];
            obj.description =  servers[i];
           // obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}