package com.flin.online;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.flin.online.model.NewsModel;
import com.flin.online.snap.AdapterSnapGeneric;
import com.flin.online.snap.Image;
import com.flin.online.snap.StartSnapHelper;
import com.flinc.core.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Snap extends AppCompatActivity  implements AdapterSnapGeneric.OnItemClickListener {


    private List<Image> items = new ArrayList<>();
    private static Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);

        RecyclerView recyclerStart = findViewById(R.id.recyclerStart);

        recyclerStart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<NewsModel> items = getImageDate(this);

        recyclerStart.setAdapter(new AdapterSnapGeneric(this, items, R.layout.item_snap_basic));


        new StartSnapHelper().attachToRecyclerView(recyclerStart);

    }

    public static List<NewsModel> getImageDate(Context ctx) {
        List<NewsModel> items = new ArrayList<>();
        /*TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.sample_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.sample_images_name);
        String date_arr[] = ctx.getResources().getStringArray(R.array.general_date);*/
       /*for (int i = 0; i < drw_arr.length(); i++) {
            NewsModel obj = new NewsModel();
            obj.image = drw_arr.getResourceId(1, -1);
            obj.name = name_arr[i]+"вв";
            obj.data = date_arr[randInt(date_arr.length - 1)];
            obj.counter = r.nextBoolean() ? randInt(500) : null;
            obj.imageDrw = ctx.getResources().getDrawable(R.drawable.image_2);;
            items.add(obj);
        }*/
        Collections.shuffle(items);
        return items;
    }

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }
    public static void click(int position){
        System.out.println("MIHAIL нажал на главной "+position);
    }





    @Override
    public void onItemClick(View view, NewsModel obj, int position) {
        System.out.println("MIHAIL нажал1 "+position);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Дополнительный сборок не "+position,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}