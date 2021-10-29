package com.minimaldev4playstore.grocerylist;

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
    List<GroceryItem> groceryItemList = new ArrayList<>();
    private GroceryItem groceryItem;
    private FinalListParentAdapter finalListParentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_detail_view);
            TextView detailsTextView = findViewById(R.id.list_detail_name);
            Intent intent = getIntent();
            String groceryItemListJSON = intent.getStringExtra("ViewList");
            Gson gson = new Gson();
            groceryItem = gson.fromJson(groceryItemListJSON, GroceryItem.class);
            //Log.e(TAG, "Data received from intent: " + groceryItem);
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
            ImageView shareImageView = findViewById(R.id.share_button_detail);
            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateAndSaveGroceryItem();
                    String result = formatContent();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, result);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            });
        }catch(Exception e){
            //Log.e(TAG, "An exception has occurred: ", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateAndSaveGroceryItem();
    }

    private void updateAndSaveGroceryItem() {
        groceryItem = finalListParentAdapter.getGroceryItem();
        //Log.e(TAG, "List from final parent adapter: " + groceryItem);
        String savedList = "SavedList";
        SharedPreferences sharedPreferencesGroceryList = this.getSharedPreferences(savedList, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesGroceryList.edit();
        String listsJsonData = sharedPreferencesGroceryList.getString(savedList, "");
        Type typeList = new TypeToken<List<GroceryItem>>(){}.getType();
        if(listsJsonData != null && !listsJsonData.equals("")){
            ////Log.e(TAG, "already saved lists: " + listsJsonData);
            Gson gson = new Gson();
            groceryItemList = gson.fromJson(listsJsonData, typeList);
            for(GroceryItem eachItem : groceryItemList){
                if(groceryItem.getTitle().equals(eachItem.getTitle())){
                    eachItem.setFruitList(groceryItem.getFruitList());
                    eachItem.setVegetableList(groceryItem.getVegetableList());
                    eachItem.setSpiceList(groceryItem.getSpiceList());
                    eachItem.setOthersList(groceryItem.getOthersList());
                    ////Log.e(TAG, "new grocery item : " + eachItem);
                }
            }
            String jsonContent = gson.toJson(groceryItemList);
            editor.putString(savedList, jsonContent);
            editor.apply();
        }
    }
    private String formatContent(){
        StringBuilder formattedResult = new StringBuilder(groceryItem.getTitle() + "\r\n");
        try{
            if(groceryItem.getFruitList() != null && !groceryItem.getFruitList().isEmpty()){
                formattedResult.append("Fruits:").append("\r\n");
                for(Fruit fruit : groceryItem.getFruitList()){
                    formattedResult.append(fruit.getName()).append("-");
                    if(!fruit.getQuantity().equals("")){
                        formattedResult.append(fruit.getQuantity()).append("\r\n");
                    }else if(!fruit.getWeight().equals("")){
                        formattedResult.append(fruit.getWeight()).append("\r\n");
                    }
                }
            }
            if(groceryItem.getVegetableList() != null && !groceryItem.getVegetableList().isEmpty()){
                formattedResult.append("Vegetables:").append("\r\n");
                for(Vegetable vegetable : groceryItem.getVegetableList()){
                    formattedResult.append(vegetable.getName()).append("-");
                    if(!vegetable.getQuantity().equals("")){
                        formattedResult.append(vegetable.getQuantity()).append("\r\n");
                    }else if(!vegetable.getWeight().equals("")){
                        formattedResult.append(vegetable.getWeight()).append("\r\n");
                    }
                }
            }
            if(groceryItem.getSpiceList() != null && !groceryItem.getSpiceList().isEmpty()){
                formattedResult.append("Spices:").append("\r\n");
                for(Spice spice : groceryItem.getSpiceList()){
                    formattedResult.append(spice.getName()).append("-");
                    if(!spice.getQuantity().equals("")){
                        formattedResult.append(spice.getQuantity()).append("\r\n");
                    }else if(!spice.getWeight().equals("")){
                        formattedResult.append(spice.getWeight()).append("\r\n");
                    }
                }
            }
            if(groceryItem.getOthersList() != null && !groceryItem.getOthersList().isEmpty()){
                formattedResult.append("Others:").append("\r\n");
                for(Others other : groceryItem.getOthersList()){
                    formattedResult.append(other.getName()).append("-");
                    if(!other.getQuantity().equals("")){
                        formattedResult.append(other.getQuantity()).append("\r\n");
                    }else if(!other.getWeight().equals("")){
                        formattedResult.append(other.getWeight()).append("\r\n");
                    }
                }
            }
            ////Log.e(TAG, "formatted result: " + formattedResult.toString());
        }catch(Exception e){
            ////Log.e(TAG, "An exception has occurred: ", e);
        }
        return formattedResult.toString();
    }
}