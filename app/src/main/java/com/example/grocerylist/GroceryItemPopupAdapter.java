package com.example.grocerylist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GroceryItemPopupAdapter extends RecyclerView.Adapter<GroceryItemPopupAdapter.GroceryItemPopupViewHolder> {
    private static final String TAG = "GroceryItemPopupAdapter";
    List<List<? extends ItemCategoryInterface>> subItemsList = null;
    private final Context context;

    public GroceryItemPopupAdapter(Context context, List<List<? extends ItemCategoryInterface>> subItemsList) {
        this.context = context;
        this.subItemsList = subItemsList;
    }

    @NonNull
    @Override
    public GroceryItemPopupAdapter.GroceryItemPopupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_custom_item_view, parent, false);
        return new GroceryItemPopupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemPopupAdapter.GroceryItemPopupViewHolder holder, int position) {
        //Add title and add items to child items recycler view adapter
        if (subItemsList.get(position).get(0).getFamily().equals("Fruits")) {
            holder.title.setText("Fruits");
        } else if (subItemsList.get(position).get(0).getFamily().equals("Vegetables")) {
            holder.title.setText("Vegetables");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.dark_green));
        } else if (subItemsList.get(position).get(0).getFamily().equals("Spices")) {
            holder.title.setText("Spices");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.dark_red));
        } else if (subItemsList.get(position).get(0).getFamily().equals("Breads")) {
            holder.title.setText("Breads");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.bread_yellow));
        } else if (subItemsList.get(position).get(0).getFamily().equals("Dairy")) {
            holder.title.setText("Dairy");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.black));
        }
        //        fruits = this.groceryItem.getFruitList();
        //        vegetables = this.groceryItem.getVegetableList();
        //        spices = this.groceryItem.getSpiceList();
        //        breads = this.groceryItem.getBreadList();
        //        dairyList = this.groceryItem.getDairyList();
        Log.e(TAG, "items: " + subItemsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
        ChildGroceryItemsAdapter childGroceryItemsAdapter = new ChildGroceryItemsAdapter(holder.recyclerViewChildItems.getContext(), subItemsList.get(position));
        holder.recyclerViewChildItems.setAdapter(childGroceryItemsAdapter);
        holder.recyclerViewChildItems.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return subItemsList.size();
    }

    public static class GroceryItemPopupViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerViewChildItems;

        public GroceryItemPopupViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_category);
            recyclerViewChildItems = (RecyclerView) itemView.findViewById(R.id.child_grocery_items_recycler_view);
        }
    }
}
