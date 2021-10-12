package com.example.grocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class FinalChildGroceryItemsAdapter extends RecyclerView.Adapter<FinalChildGroceryItemsAdapter.FinalChildViewHolder> {
    private static final String TAG = "FinalChildItemsAdapter";
    List<? extends ItemCategoryInterface> eachCategoryItems = null;
    List<Fruit> fruits;
    List<Vegetable> vegetables;
    List<Spice> spices;
    List<Bread> breads;
    List<Dairy> dairyList;
    private Context context;
    public FinalChildGroceryItemsAdapter(Context context, List<? extends ItemCategoryInterface> eachCategoryItems) {
        this.context = context;
        this.eachCategoryItems = eachCategoryItems;
    }

    @NonNull
    @Override
    public FinalChildGroceryItemsAdapter.FinalChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_child_custom_view, parent, false);
        return new FinalChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinalChildGroceryItemsAdapter.FinalChildViewHolder holder, int position) {
        holder.itemNameTextView.setText(eachCategoryItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return eachCategoryItems.size();
    }
    public class FinalChildViewHolder extends RecyclerView.ViewHolder{
        MaterialCheckBox materialCheckBox;
        TextView itemNameTextView;
        public FinalChildViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCheckBox = (MaterialCheckBox) itemView.findViewById(R.id.final_popup_check_box);
            itemNameTextView = (TextView) itemView.findViewById(R.id.final_child_item_name);
        }
    }
}
