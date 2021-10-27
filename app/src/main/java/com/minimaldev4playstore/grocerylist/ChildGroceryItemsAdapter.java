package com.minimaldev4playstore.grocerylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class ChildGroceryItemsAdapter extends RecyclerView.Adapter<ChildGroceryItemsAdapter.ChildGroceryItemPopupViewHolder> {
    private static final String TAG = "ChildGroceryItems";
    List<? extends ItemCategoryInterface> eachCategoryItems = null;

    public ChildGroceryItemsAdapter(Context context, List<? extends ItemCategoryInterface> eachCategoryItems) {
        this.eachCategoryItems = eachCategoryItems;
    }

    @NonNull
    @Override
    public ChildGroceryItemPopupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_grocery_item, parent, false);
        return new ChildGroceryItemPopupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildGroceryItemPopupViewHolder holder, int position) {
        holder.itemNameTextView.setText(eachCategoryItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return eachCategoryItems.size();
    }

    public static class ChildGroceryItemPopupViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox materialCheckBox;
        TextView itemNameTextView;
        public ChildGroceryItemPopupViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCheckBox = (MaterialCheckBox) itemView.findViewById(R.id.popup_check_box);
            itemNameTextView = (TextView) itemView.findViewById(R.id.child_item_name);
        }
    }
}
