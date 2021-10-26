package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
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

import org.json.JSONArray;
import org.json.JSONObject;

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
        ImageView shareImageView = findViewById(R.id.share_button_detail);
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAndSaveGroceryItem();
                String jsonContent = gson.toJson(groceryItem);
                Log.e(TAG, "final jsoncontent to share: " + jsonContent);
                jsonContent = formatContent(jsonContent);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, jsonContent);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateAndSaveGroceryItem();
    }

    private void updateAndSaveGroceryItem() {
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
    private String formatContent(String jsonContent){
        StringBuilder formattedResult = new StringBuilder(groceryItem.getTitle() + "\r\n");
        try{
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray fruitArray = jsonObject.optJSONArray("fruitList");
            JSONArray vegetableArray = jsonObject.optJSONArray("vegetableList");
            JSONArray spiceArray = jsonObject.optJSONArray("spiceList");
            JSONArray otherArray = jsonObject.optJSONArray("othersList");
            if(fruitArray != null && fruitArray.length() > 0){
                formattedResult.append("Fruits:").append("\r\n");
                for(int index = 0; index < fruitArray.length(); index++){
                    JSONObject fruit = (JSONObject) fruitArray.get(index);
                    formattedResult.append(fruit.get("name")).append("-");
                    if(!fruit.get("quantity").equals("")){
                        formattedResult.append(fruit.get("quantity")).append("\r\n");
                    }else if(!fruit.get("weight").equals("")){
                        formattedResult.append(fruit.get("weight")).append("\r\n");
                    }
                }
            }if(vegetableArray != null && vegetableArray.length() > 0){
                formattedResult.append("Vegetables:").append("\r\n");
                for(int index = 0; index < vegetableArray.length(); index++){
                    JSONObject vegetable = (JSONObject) vegetableArray.get(index);
                    formattedResult.append(vegetable.get("name")).append("-");
                    if(!vegetable.get("quantity").equals("")){
                        formattedResult.append(vegetable.get("quantity")).append("\r\n");
                    }else if(!vegetable.get("weight").equals("")){
                        formattedResult.append(vegetable.get("weight")).append("\r\n");
                    }
                }
            }if(spiceArray != null && spiceArray.length() > 0){
                formattedResult.append("Spices:").append("\r\n");
                for(int index = 0; index < spiceArray.length(); index++){
                    JSONObject spice = (JSONObject) spiceArray.get(index);
                    formattedResult.append(spice.get("name")).append("-");
                    if(!spice.get("quantity").equals("")){
                        formattedResult.append(spice.get("quantity")).append("\r\n");
                    }else if(!spice.get("weight").equals("")){
                        formattedResult.append(spice.get("weight")).append("\r\n");
                    }
                }
            }if(otherArray != null && otherArray.length() > 0){
                formattedResult.append("Others:").append("\r\n");
                for(int index = 0; index < otherArray.length(); index++){
                    JSONObject other = (JSONObject) otherArray.get(index);
                    formattedResult.append(other.get("name")).append("-");
                    if(!other.get("quantity").equals("")){
                        formattedResult.append(other.get("quantity")).append("\r\n");
                    }else if(!other.get("weight").equals("")){
                        formattedResult.append(other.get("weight")).append("\r\n");
                    }
                }
            }
            Log.e(TAG, "formattedrresult: " + formattedResult.toString());
        }catch(Exception e){
            Log.e(TAG, "An exception has occurred: ", e);
        }
        return formattedResult.toString();
    }
}