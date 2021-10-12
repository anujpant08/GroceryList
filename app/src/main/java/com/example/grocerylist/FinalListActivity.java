package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class FinalListActivity extends CreateFruitListActivity {
    private static final String TAG = "FinalListActivity";
    private SharedPreferences.Editor editor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_list);
        SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
        if(editor == null){
            editor = sharedPreferences.edit();
        }
        String jsonData = sharedPreferences.getString(NEW_LIST, "");
        Type type = new TypeToken<GroceryItem>(){}.getType();
        if(jsonData != null && !jsonData.equals("")){
            Log.e(TAG, "jsonDate in Final List Activity: " + jsonData);
            Gson gson = new Gson();
            newGroceryItem = gson.fromJson(jsonData, type);
        }
        Log.e(TAG, "In FinalActivity: " + newGroceryItem);
        List<List<? extends ItemCategoryInterface>> subItemsList = new LinkedList<>();
        RecyclerView recyclerView = findViewById(R.id.final_list_recycler_view);
        if(newGroceryItem != null){
            if (newGroceryItem.getFruitList() != null && !newGroceryItem.getFruitList().isEmpty()) {
                subItemsList.add(newGroceryItem.getFruitList());
            }
            if (newGroceryItem.getVegetableList() != null && !newGroceryItem.getVegetableList().isEmpty()) {
                subItemsList.add(newGroceryItem.getVegetableList());
            }
            if (newGroceryItem.getSpiceList() != null && !newGroceryItem.getSpiceList().isEmpty()) {
                subItemsList.add(newGroceryItem.getSpiceList());
            }
            if (newGroceryItem.getBreadList() != null && !newGroceryItem.getBreadList().isEmpty()) {
                subItemsList.add(newGroceryItem.getBreadList());
            }
            if (newGroceryItem.getDairyList() != null && !newGroceryItem.getDairyList().isEmpty()) {
                subItemsList.add(newGroceryItem.getDairyList());
            }
            FinalListParentAdapter finalListParentAdapter = new FinalListParentAdapter(this, subItemsList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(finalListParentAdapter);
            recyclerView.setNestedScrollingEnabled(false);
            finalListParentAdapter.notifyDataSetChanged();
        }
    }
}