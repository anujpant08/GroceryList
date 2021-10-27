package com.minimaldev4playstore.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CreateDairyBreadListActivity extends CreateFruitListActivity {
    private static final List<String> allProducts = new ArrayList<>();
    private static final String TAG = "CreateOtherListActivity";
    private String intentValue = "";
    private static final String RECENT_BREAD_DAIRY_LIST = "RecentBreadDairyList";
    private List<String> recentItems;
    private String imageString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_dairy_bread_list);
            TextView factTextView = findViewById(R.id.fact_others);
            String fact = getFunFact();
            factTextView.setText(fact);
            Intent intent = getIntent();
            intentValue = intent.getStringExtra("ClearData");
            SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
            if(editor == null){
                editor = sharedPreferences.edit();
            }
            String jsonData = sharedPreferences.getString(NEW_LIST, "");
            Type type = new TypeToken<GroceryItem>(){}.getType();
            if(jsonData != null && !jsonData.equals("")){
                Log.e(TAG, "jsonATA: " + jsonData);
                Gson gson = new Gson();
                newGroceryItem = gson.fromJson(jsonData, type);
            }
            Log.e(TAG, "In DairyBread: " + newGroceryItem);
            ImageView imageView = (ImageView)findViewById(R.id.feature_bread_dairy);
            Glide.with(this).load(R.drawable.dairybread).into(imageView);
            String othersJSON = "";
            if(allProducts.isEmpty()){
                InputStream inputStream = this.getAssets().open("breadDairyProducts.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                othersJSON = new String(buffer, StandardCharsets.UTF_8);
                parseJSON(othersJSON);
            }
            SharedPreferences recentsSharedPrefs = this.getSharedPreferences(RECENT_BREAD_DAIRY_LIST, Context.MODE_PRIVATE);
            SharedPreferences.Editor recentEditor = recentsSharedPrefs.edit();
            recentItems = new LinkedList<>(recentsSharedPrefs.getStringSet(RECENT_BREAD_DAIRY_LIST, new LinkedHashSet<>()));
            RecyclerView recentsRecyclerView = findViewById(R.id.recents_recycler_view_dairy);
            RecentItemsAdapter recentItemsAdapter = new RecentItemsAdapter(recentItems, this, "Others");
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
            recentsRecyclerView.setLayoutManager(gridLayoutManager);
            recentsRecyclerView.setAdapter(recentItemsAdapter);
            recentItemsAdapter.notifyDataSetChanged();
            recentItemsAdapter.setItemClickListener(this);
            AutoCompleteTextView searchBox = (AutoCompleteTextView) findViewById(R.id.search_text_dairy_bread);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_search_view, allProducts);
            searchBox.setThreshold(1);
            searchBox.setAdapter(adapter);
            searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    childDatabaseReference = databaseReference.child("Others/" + adapterView.getItemAtPosition(position));
                    if(!isNetworkAvailable()){
                        bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Others");
                        Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_BREAD_DAIRY_LIST, new LinkedHashSet<>()));
                        recents.add(adapterView.getItemAtPosition(position) + "##");
                        recentEditor.putStringSet(RECENT_BREAD_DAIRY_LIST, recents);
                        recentEditor.apply();
                        recentItems.clear();
                        recentItems.addAll(recents);
                        recentItemsAdapter.notifyDataSetChanged();
                        recentsRecyclerView.setAdapter(recentItemsAdapter);
                    }else{
                        childDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                imageString = (String) snapshot.getValue();
                                Log.e(TAG, "Firebase others URL: " + imageString);
                                bottomSheetFragment.setItemData(imageString, (String) adapterView.getItemAtPosition(position), newGroceryItem, "Others");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                                Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_BREAD_DAIRY_LIST, new LinkedHashSet<>()));
                                recents.add(adapterView.getItemAtPosition(position) + "##" + imageString);
                                recentEditor.putStringSet(RECENT_BREAD_DAIRY_LIST, recents);
                                recentEditor.apply();
                                recentItems.clear();
                                recentItems.addAll(recents);
                                recentItemsAdapter.notifyDataSetChanged();
                                recentsRecyclerView.setAdapter(recentItemsAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "An exception has occurred: ", error.toException());
                                bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Others");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                                Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_BREAD_DAIRY_LIST, new LinkedHashSet<>()));
                                recents.add(adapterView.getItemAtPosition(position) + "##");
                                recentEditor.putStringSet(RECENT_BREAD_DAIRY_LIST, recents);
                                recentEditor.apply();
                                recentItems.clear();
                                recentItems.addAll(recents);
                                recentItemsAdapter.notifyDataSetChanged();
                                recentsRecyclerView.setAdapter(recentItemsAdapter);
                            }
                        });
                    }
                    newGroceryItem = BottomSheetFragment.getGroceryItem();
                }
            });
            ExtendedFloatingActionButton extendedFloatingActionButton = findViewById(R.id.final_list_fab);
            extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateDairyBreadListActivity.this, FinalListActivity.class);
                    Gson gson = new Gson();
                    String jsonContent = gson.toJson(BottomSheetFragment.getGroceryItem());
                    Log.e(TAG,"new list in dairyBread: " + jsonContent);
                    editor.putString(NEW_LIST, jsonContent);
                    editor.apply();
                    startActivity(intent);
                    intentValue = "";
                }
            });
            TextView clearTextView = findViewById(R.id.recents_clear_dairy);
            clearTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recentItemsAdapter.notifyItemRangeRemoved(0, recentItems.size());
                    recentItems.clear();
                    recentEditor.putStringSet(RECENT_BREAD_DAIRY_LIST, null);
                    recentEditor.apply();
                }
            });
        }catch(Exception exception){
            Log.e(TAG, "An exception has occurred: ", exception);
        }
    }
    private void parseJSON(String json){
        try{
            JSONObject response = new JSONObject(json);
            JSONArray flourArray = response.getJSONArray("daily");
            JSONArray dairyArray = response.getJSONArray("dairy");
            String productName = "";
            for(int index = 0; index < flourArray.length(); index++){
                productName = flourArray.getString(index);
                productName = Character.toUpperCase(productName.charAt(0)) + productName.substring(1);
                allProducts.add(productName);
            }
            for(int index = 0; index < dairyArray.length(); index++){
                productName = dairyArray.getString(index);
                productName = Character.toUpperCase(productName.charAt(0)) + productName.substring(1);
                allProducts.add(productName);
            }
        }catch(Exception exception){
            Log.e(TAG, "An exception occurred while JSON parsing: ", exception);
        }
    }
    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString(NEW_LIST, "");
        Type type = new TypeToken<GroceryItem>() {
        }.getType();
        if (jsonData != null && !jsonData.equals("")) {
            Gson gson = new Gson();
            newGroceryItem = gson.fromJson(jsonData, type);
        }else{
            newGroceryItem = new GroceryItem();
        }
        super.onResume();
    }
    @Override
    public void onClick(View view, int position) {
        String[] data = recentItems.get(position).split("##");
        if(data.length > 1){
            bottomSheetFragment.setItemData(recentItems.get(position).split("##")[1], recentItems.get(position).split("##")[0], newGroceryItem, "Others");
        }else{
            bottomSheetFragment.setItemData("", recentItems.get(position).split("##")[0], newGroceryItem, "Others");
        }
        bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
    }
    private String getFunFact() throws Exception{
        String fact = "";
        InputStream inputStream = this.getAssets().open("fun facts others.json");
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String factJSON = new String(buffer, StandardCharsets.UTF_8);
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 20);
        JSONObject jsonObject = new JSONObject(factJSON);
        JSONArray factsArray = jsonObject.getJSONArray("facts");
        fact = factsArray.getString(randomNumber);
        return "Fun fact: " + fact;
    }
}