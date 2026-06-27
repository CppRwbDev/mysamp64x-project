package com.flin.online.snap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flin.online.DonateActivity;
import com.flin.online.MenuActivity;
import com.flin.online.ReUpdateClient;
import com.flin.online.SetingsActivity;
import com.flin.online.Snap;
import com.flin.online.Util;
import com.flin.online.jsonenter.PublicInfo;
import com.flin.online.model.NewsModel;
import com.flinc.core.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterSnapGeneric extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewsModel> items = new ArrayList<NewsModel>();

    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private int layout_id;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, NewsModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterSnapGeneric(Context context, List<NewsModel> items, int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView brief;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            brief = v.findViewById(R.id.brief);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);
        vh = new OriginalViewHolder(v);


        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewsModel obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.name.setText(obj.name);
            view.brief.setText(obj.data);
            //displayImageOriginal(ctx, view.image, obj.image, position);

            Glide.with(ctx).load(obj.imageURL)
                   .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(view.image);
            
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                    if(obj.type == 1) {
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(ctx);
                        builderUpdateClient.setTitle("Переход на новость");
                        builderUpdateClient.setMessage("Вы действительно хотите открыть страницу с новостью?\n\nURL: " + obj.url);
                        builderUpdateClient.setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.url));
                                ctx.startActivity(browserIntent);
                            }
                        });
                        builderUpdateClient.setNegativeButton("Закрыть", null);
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                    if(obj.type == 2) {
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(ctx);
                        builderUpdateClient.setTitle(""+ obj.name);
                        builderUpdateClient.setMessage(""+ obj.description);
                        builderUpdateClient.setPositiveButton("Пополнить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ctx, DonateActivity.class);
                                ctx.startActivity(intent);
                            }
                        });
                        builderUpdateClient.setNegativeButton("Закрыть", null);
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                    if(obj.type == 3) {
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(ctx);
                        builderUpdateClient.setTitle(""+ obj.name);
                        builderUpdateClient.setMessage(""+ obj.description);
                        builderUpdateClient.setNegativeButton("Закрыть", null);
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                    if(obj.type == 4) {
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(ctx);
                        builderUpdateClient.setTitle(""+ obj.name);
                        builderUpdateClient.setMessage(""+ obj.description);
                        builderUpdateClient.setPositiveButton("Открыть настройки", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ctx, SetingsActivity.class);
                                ctx.startActivity(intent);
                            }
                        });
                        builderUpdateClient.setNegativeButton("Закрыть", null);
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                    if(obj.type == 5) {
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(ctx);
                        builderUpdateClient.setTitle(""+ obj.name);
                        builderUpdateClient.setMessage(""+ obj.description);
                        builderUpdateClient.setPositiveButton("Открыть статью", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.url));
                                ctx.startActivity(browserIntent);
                            }
                        });
                        builderUpdateClient.setNegativeButton("Закрыть", null);
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                    if(obj.type == 6) {
                        AlertDialog.Builder builderUpdateClient;
                        builderUpdateClient = new AlertDialog.Builder(ctx);
                        builderUpdateClient.setTitle(""+ obj.name);
                        builderUpdateClient.setMessage(""+ obj.description);
                        builderUpdateClient.setPositiveButton("Перейти", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.url));
                                ctx.startActivity(browserIntent);
                            }
                        });
                        builderUpdateClient.setNegativeButton("Закрыть", null);
                        AlertDialog alert = builderUpdateClient.create();
                        alert.show();
                    }
                }
            });

        }
    }

    public static void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable, int id) {
        try {
            if (id == 0) Glide.with(ctx).load("https://i.imgur.com/oguZ5P2.jpeg")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
            if (id == 1) Glide.with(ctx).load("https://i.imgur.com/O82xHMM.jpeg")

                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public NewsModel getItem(int position) {
        return items.get(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}