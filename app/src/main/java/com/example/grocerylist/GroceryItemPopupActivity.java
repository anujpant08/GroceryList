package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class GroceryItemPopupActivity extends AppCompatActivity {
    private static final String TAG = "GroceryItemPopup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_grocery_item_view);
        setFinishOnTouchOutside(true);
        Intent intent = getIntent();
        String groceryItemListJSON = intent.getStringExtra("Popup");
        Gson gson = new Gson();
        GroceryItem groceryItem = gson.fromJson(groceryItemListJSON, GroceryItem.class);
        Log.e(TAG, "Data received from intent: " + groceryItem);
        List<List<? extends ItemCategoryInterface>> subItemsList = null;
        TextView popupTitle = (TextView) findViewById(R.id.popup_title);
        popupTitle.setText(groceryItem.getTitle());
        subItemsList = new LinkedList<>();
        if(groceryItem.getFruitList() != null && !groceryItem.getFruitList().isEmpty()){
            subItemsList.add(groceryItem.getFruitList());
        }if(groceryItem.getVegetableList() != null && !groceryItem.getVegetableList().isEmpty()){
            subItemsList.add(groceryItem.getVegetableList());
        }if(groceryItem.getSpiceList() != null && !groceryItem.getSpiceList().isEmpty()){
            subItemsList.add(groceryItem.getSpiceList());
        }if(groceryItem.getBreadList() != null && !groceryItem.getBreadList().isEmpty()){
            subItemsList.add(groceryItem.getBreadList());
        }if(groceryItem.getDairyList() != null && !groceryItem.getDairyList().isEmpty()){
            subItemsList.add(groceryItem.getDairyList());
        }
        RecyclerView popupRecyclerView = (RecyclerView) findViewById(R.id.popup_recycler_view);
        GroceryItemPopupAdapter groceryItemPopupAdapter = new GroceryItemPopupAdapter(this, subItemsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        popupRecyclerView.setLayoutManager(layoutManager);
        popupRecyclerView.setAdapter(groceryItemPopupAdapter);
        groceryItemPopupAdapter.notifyDataSetChanged();
    }
}