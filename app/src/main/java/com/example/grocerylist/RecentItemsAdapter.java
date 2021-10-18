package com.example.grocerylist;

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

import org.w3c.dom.Text;

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
        Log.e(TAG, "DATA in recent adapter is: " + recentItems.get(holder.getAdapterPosition()));
        holder.recentsItemName.setText(data[0]);
        if(data.length > 1 && data[1].length() > 0 && !data[1].equals("null")){
            Glide.with(context).load(data[1]).into(holder.recentsImageView);
        }else{
            Log.e(TAG, "family in recent adapter is: " + family);
            switch(family){
                case "Fruit":
                    Glide.with(context).load(R.drawable.fruit).into(holder.recentsImageView);
                    break;
                case "Vegetable":
                    Glide.with(context).load(R.drawable.vegetables).into(holder.recentsImageView);
                    break;
                case "Spice":
                    Glide.with(context).load(R.drawable.spice).into(holder.recentsImageView);
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
