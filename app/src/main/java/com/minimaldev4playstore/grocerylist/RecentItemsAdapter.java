package com.minimaldev4playstore.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecentItemsAdapter extends RecyclerView.Adapter<RecentItemsAdapter.RecentsViewHolder> {
    private static final String TAG = "RecentItemsAdapter";
    private final List<String> recentItems;
    private final Context context;
    private ItemClickListener itemClickListener;
    private String family;
    public RecentItemsAdapter(List<String> recentItems, Context context, String family) {
        this.recentItems = recentItems;
        this.context = context;
        this.family = family;
    }

    @NonNull
    @Override
    public RecentItemsAdapter.RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recents_custom_view, parent, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentItemsAdapter.RecentsViewHolder holder, int position) {
        String[] data = recentItems.get(holder.getAdapterPosition()).split("##");
        //Log.e(TAG, "DATA in recent adapter is: " + recentItems.get(holder.getAdapterPosition()));
        holder.recentsItemName.setText(data[0]);
        if(data.length > 1 && data[1].length() > 0 && !data[1].equals("null")){
            GlideApp.with(context).load(data[1]).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(holder.recentsImageView);
        }else{
            //Log.e(TAG, "family in recent adapter is: " + family);
            switch(family){
                case "Fruit":
                    GlideApp.with(context).load(R.drawable.fruit).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(holder.recentsImageView);
                    break;
                case "Vegetable":
                    GlideApp.with(context).load(R.drawable.vegetables).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(holder.recentsImageView);
                    break;
                case "Spice":
                    GlideApp.with(context).load(R.drawable.spice).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(holder.recentsImageView);
                    break;
                case "Others":
                    GlideApp.with(context).load(R.drawable.dairybread).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(holder.recentsImageView);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return recentItems.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public class RecentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView recentsImageView;
        TextView recentsItemName;
        public RecentsViewHolder(@NonNull View itemView) {
            super(itemView);
            recentsImageView = (ImageView) itemView.findViewById(R.id.recents_item_image);
            recentsItemName = (TextView) itemView.findViewById(R.id.recents_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
