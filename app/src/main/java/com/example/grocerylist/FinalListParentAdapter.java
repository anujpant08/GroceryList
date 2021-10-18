package com.example.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FinalListParentAdapter extends RecyclerView.Adapter<FinalListParentAdapter.FinalListParentViewHolder> {
    private static final String TAG = "FinalListParentAdapter";
    List<List<? extends ItemCategoryInterface>> subItemsList = null;
    private final Context context;
    private GroceryItem groceryItem;

    public FinalListParentAdapter(Context context, List<List<? extends ItemCategoryInterface>> subItemsList, GroceryItem groceryItem) {
        this.context = context;
        this.subItemsList = subItemsList;
        this.groceryItem = groceryItem;
    }

    @NonNull
    @Override
    public FinalListParentAdapter.FinalListParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_list_parent_custom_view, parent, false);
        return new FinalListParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinalListParentAdapter.FinalListParentViewHolder holder, int position) {
        if (subItemsList.get(position).get(0).getFamily().equals("Fruits")) {
            holder.title.setText("Fruits");
        } else if (subItemsList.get(position).get(0).getFamily().equals("Vegetables")) {
            holder.title.setText("Vegetables");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.dark_green));
        } else if (subItemsList.get(position).get(0).getFamily().equals("Spices")) {
            holder.title.setText("Spices");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.dark_red));
        } else if (subItemsList.get(position).get(0).getFamily().equals("Others")) {
            holder.title.setText("Others");
            holder.title.setTextColor(this.context.getResources().getColor(R.color.black));
        }
        Log.e(TAG, "items: " + subItemsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
        FinalChildGroceryItemsAdapter finalChildGroceryItemsAdapter = new FinalChildGroceryItemsAdapter(holder.recyclerViewChildItems.getContext(), subItemsList.get(position), groceryItem);
        RecyclerView.OnItemTouchListener scrollTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_MOVE) {
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
        holder.recyclerViewChildItems.addOnItemTouchListener(scrollTouchListener);
        holder.recyclerViewChildItems.setAdapter(finalChildGroceryItemsAdapter);
        holder.recyclerViewChildItems.setLayoutManager(layoutManager);
        finalChildGroceryItemsAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return subItemsList.size();
    }

    public class FinalListParentViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        RecyclerView recyclerViewChildItems;
        public FinalListParentViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.final_item_category);
            recyclerViewChildItems = (RecyclerView) itemView.findViewById(R.id.final_child_items_recycler_view);
        }

    }

}
