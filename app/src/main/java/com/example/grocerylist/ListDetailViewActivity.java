package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListDetailViewActivity extends AppCompatActivity {
    private static final String TAG = "ListDetailViewActivity";
    private final String SAVEDLIST = "SavedList";
    List<GroceryItem> groceryItemList = new ArrayList<>();
    private GroceryItem groceryItem;
    private FinalListParentAdapter finalListParentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_view);
        TextView detailsTextView = findViewById(R.id.list_detail_name);
        Intent intent = getIntent();
        String groceryItemListJSON = intent.getStringExtra("ViewList");
        Gson gson = new Gson();
        groceryItem = gson.fromJson(groceryItemListJSON, GroceryItem.class);
        Log.e(TAG, "Data received from intent: " + groceryItem);
        List<List<? extends ItemCategoryInterface>> subItemsList = new LinkedList<>();
        RecyclerView recyclerView = findViewById(R.id.detail_recycler_view);
        if(groceryItem != null){
            detailsTextView.setText(groceryItem.getTitle());
            if (groceryItem.getFruitList() != null && !groceryItem.getFruitList().isEmpty()) {
                subItemsList.add(groceryItem.getFruitList());
            }
            if (groceryItem.getVegetableList() != null && !groceryItem.getVegetableList().isEmpty()) {
                subItemsList.add(groceryItem.getVegetableList());
            }
            if (groceryItem.getSpiceList() != null && !groceryItem.getSpiceList().isEmpty()) {
                subItemsList.add(groceryItem.getSpiceList());
            }
            if (groceryItem.getOthersList() != null && !groceryItem.getOthersList().isEmpty()) {
                subItemsList.add(groceryItem.getOthersList());
            }
        }
        finalListParentAdapter = new FinalListParentAdapter(this, subItemsList, groceryItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(finalListParentAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        finalListParentAdapter.notifyDataSetChanged();
        ImageView backImageView = findViewById(R.id.back_button_detail);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        groceryItem = finalListParentAdapter.getGroceryItem();
        Log.e(TAG, "List from final parent adapter: " + groceryItem);
        SharedPreferences sharedPreferencesGroceryList = this.getSharedPreferences(SAVEDLIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesGroceryList.edit();
        String listsJsonData = sharedPreferencesGroceryList.getString(SAVEDLIST, "");
        Type typeList = new TypeToken<List<GroceryItem>>(){}.getType();
        if(listsJsonData != null && !listsJsonData.equals("")){
            Log.e(TAG, "already saved lists: " + listsJsonData);
            Gson gson = new Gson();
            groceryItemList = gson.fromJson(listsJsonData, typeList);
            for(GroceryItem eachItem : groceryItemList){
                if(groceryItem.getTitle().equals(eachItem.getTitle())){
                    eachItem.setFruitList(groceryItem.getFruitList());
                    eachItem.setVegetableList(groceryItem.getVegetableList());
                    eachItem.setSpiceList(groceryItem.getSpiceList());
                    eachItem.setOthersList(groceryItem.getOthersList());
                    Log.e(TAG, "new grocery item : " + eachItem);
                }
            }
            String jsonContent = gson.toJson(groceryItemList);
            editor.putString(SAVEDLIST, jsonContent);
            editor.apply();
        }
    }
}