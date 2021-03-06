package com.minimaldev4playstore.grocerylist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.GroceryItemViewHolder> {
    private List<GroceryItem> groceryItemList;
    public ItemClickListener itemClickListener;
    public GroceryItemAdapter(List<GroceryItem> groceryItemList) {
        this.groceryItemList = groceryItemList;
    }

    @NonNull
    @Override
    public GroceryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item_custom_view, parent, false);
        return new GroceryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemViewHolder holder, int position) {
        holder.titleTextView.setText(this.groceryItemList.get(position).getTitle());
        GroceryItem groceryItem = this.groceryItemList.get(position);
        if(groceryItem.getFruitList() != null && groceryItem.getFruitList().size() > 0){
            holder.fruitImageView.setVisibility(View.VISIBLE);
            holder.fruitQuantityTextView.setVisibility(View.VISIBLE);
            holder.fruitQuantityTextView.setText(String.valueOf(groceryItem.getFruitList().size()));
        }if(groceryItem.getVegetableList() != null && groceryItem.getVegetableList().size() > 0){
            holder.vegetableImageView.setVisibility(View.VISIBLE);
            holder.vegetableQuantityTextView.setVisibility(View.VISIBLE);
            holder.vegetableQuantityTextView.setText(String.valueOf(groceryItem.getVegetableList().size()));
        }if(groceryItem.getSpiceList() != null && groceryItem.getSpiceList().size() > 0){
            holder.spiceImageView.setVisibility(View.VISIBLE);
            holder.spiceQuantityTextView.setVisibility(View.VISIBLE);
            holder.spiceQuantityTextView.setText(String.valueOf(groceryItem.getSpiceList().size()));
        }if(groceryItem.getOthersList() != null && groceryItem.getOthersList().size() > 0){
            holder.othersImageView.setVisibility(View.VISIBLE);
            holder.othersTextView.setVisibility(View.VISIBLE);
            holder.othersTextView.setText(String.valueOf(groceryItem.getOthersList().size()));
        }
    }

    @Override
    public int getItemCount() {
        return this.groceryItemList.size();
    }

    public void updateList(List<GroceryItem> updatedList){
        this.groceryItemList = updatedList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class GroceryItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private static final String TAG = "GroceryItemViewHolder";
        TextView titleTextView;
        ImageView fruitImageView;
        TextView fruitQuantityTextView;
        ImageView vegetableImageView;
        TextView vegetableQuantityTextView;
        ImageView spiceImageView;
        TextView spiceQuantityTextView;
        ImageView othersImageView;
        TextView othersTextView;

        public GroceryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            fruitImageView = (ImageView) itemView.findViewById(R.id.fruit_item);
            fruitQuantityTextView = (TextView) itemView.findViewById(R.id.fruit_quantity);
            vegetableImageView = (ImageView) itemView.findViewById(R.id.vegetable_item);
            vegetableQuantityTextView = (TextView) itemView.findViewById(R.id.vegetable_quantity);
            spiceImageView = (ImageView) itemView.findViewById(R.id.spice_item);
            spiceQuantityTextView = (TextView) itemView.findViewById(R.id.spice_quantity);
            othersImageView = (ImageView) itemView.findViewById(R.id.others_item);
            othersTextView = (TextView) itemView.findViewById(R.id.others_quantity);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (itemClickListener != null) {
                Log.e(TAG, "long clicked.");
                itemClickListener.onLongClick(view, getAdapterPosition());
            }
            return true;
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                Log.e(TAG, "clicked.");
                itemClickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
