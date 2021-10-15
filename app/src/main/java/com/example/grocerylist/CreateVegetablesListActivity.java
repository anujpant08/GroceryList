package com.example.grocerylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CreateVegetablesListActivity extends CreateFruitListActivity implements ItemClickListener {
    private static final String TAG = "CreateVegListActivity";
    public static final List<String> allVegetables = new LinkedList<>();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    public DatabaseReference childDatabaseReference = null;
    private String imageString = "";
    private String intentValue = "";
    private static final String RECENT_VEGGIES_LIST = "RecentVeggiesList";
    private List<String> recentItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_vegetables_list);
            Intent intent = getIntent();
            intentValue = intent.getStringExtra("ClearData");
            SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
            if(editor == null){
                editor = sharedPreferences.edit();
            }
            String vegetableJSON = "";
            String jsonData = sharedPreferences.getString(NEW_LIST, "");
            Type type = new TypeToken<GroceryItem>(){}.getType();
            if(jsonData != null && !jsonData.equals("")){
                Log.e(TAG, "jsonATA: " + jsonData);
                Gson gson = new Gson();
                newGroceryItem = gson.fromJson(jsonData, type);
            }
            Log.e(TAG, "In Veggies: " + newGroceryItem);
            ImageView imageView = (ImageView)findViewById(R.id.feature_vegetable);
            Glide.with(this).load(R.drawable.vegetables).into(imageView);
            if(allVegetables.isEmpty()) {
                InputStream inputStream = this.getAssets().open("vegetables.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                vegetableJSON = new String(buffer, StandardCharsets.UTF_8);
                parseJSON(vegetableJSON);
            }
            SharedPreferences recentsSharedPrefs = this.getSharedPreferences(RECENT_VEGGIES_LIST, Context.MODE_PRIVATE);
            SharedPreferences.Editor recentEditor = recentsSharedPrefs.edit();
            recentItems = new LinkedList<>(recentsSharedPrefs.getStringSet(RECENT_VEGGIES_LIST, new LinkedHashSet<>()));
            RecyclerView recentsRecyclerView = findViewById(R.id.recents_veggies_recycler_view);
            RecentItemsAdapter recentItemsAdapter = new RecentItemsAdapter(recentItems, this, "Vegetable");
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
            recentsRecyclerView.setLayoutManager(gridLayoutManager);
            recentsRecyclerView.setAdapter(recentItemsAdapter);
            recentItemsAdapter.notifyDataSetChanged();
            recentItemsAdapter.setItemClickListener(this);
            AutoCompleteTextView searchBox = (AutoCompleteTextView) findViewById(R.id.search_text_vegetable);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_search_view, allVegetables);
            searchBox.setThreshold(1);
            searchBox.setAdapter(adapter);
            searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    //Add bottom popup to show fruit details with fruit image
                    childDatabaseReference = databaseReference.child("Vegetables/" + adapterView.getItemAtPosition(position));
                    if(!isNetworkAvailable()){
                        bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Vegetable");
                        Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_VEGGIES_LIST, new LinkedHashSet<>()));
                        recents.add(adapterView.getItemAtPosition(position) + "##");
                        recentEditor.putStringSet(RECENT_VEGGIES_LIST, recents);
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
                                Log.e(TAG, "Firebase vegetable URL: " + imageString);
                                bottomSheetFragment.setItemData(imageString, (String) adapterView.getItemAtPosition(position), newGroceryItem, "Vegetable");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                                Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_VEGGIES_LIST, new LinkedHashSet<>()));
                                recents.add(adapterView.getItemAtPosition(position) + "##" + imageString);
                                recentEditor.putStringSet(RECENT_VEGGIES_LIST, recents);
                                recentEditor.apply();
                                recentItems.clear();
                                recentItems.addAll(recents);
                                recentItemsAdapter.notifyDataSetChanged();
                                recentsRecyclerView.setAdapter(recentItemsAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "An exception has occurred: ", error.toException());
                                bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Vegetable");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                                Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_VEGGIES_LIST, new LinkedHashSet<>()));
                                recents.add(adapterView.getItemAtPosition(position) + "##");
                                recentEditor.putStringSet(RECENT_VEGGIES_LIST, recents);
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
            ExtendedFloatingActionButton extendedFloatingActionButton = findViewById(R.id.get_spices);
            extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateVegetablesListActivity.this, FinalListActivity.class);
                    Gson gson = new Gson();
                    String jsonContent = gson.toJson(BottomSheetFragment.getGroceryItem());
                    Log.e(TAG,"new list in vegetables: " + jsonContent);
                    editor.putString(NEW_LIST, jsonContent);
                    editor.apply();
                    startActivity(intent);
                    intentValue = "";
                }
            });
            TextView clearTextView = findViewById(R.id.recents_veggies_clear);
            clearTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recentItemsAdapter.notifyItemRangeRemoved(0, recentItems.size());
                    recentItems.clear();
                    recentEditor.putStringSet(RECENT_VEGGIES_LIST, null);
                    recentEditor.apply();
                }
            });
        }catch(Exception exception){
            Log.e(TAG, "An exception occurred: ", exception);
        }
    }
    private void parseJSON(String vegetableJSON){
        try{
            JSONObject response = new JSONObject(vegetableJSON);
            JSONArray vegetablesArray = response.getJSONArray("vegetables");
            String vegetableName = "";
            for(int index = 0; index < vegetablesArray.length(); index++){
                vegetableName = vegetablesArray.getString(index);
                vegetableName = Character.toUpperCase(vegetableName.charAt(0)) + vegetableName.substring(1);
                allVegetables.add(vegetableName);
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
            bottomSheetFragment.setItemData(recentItems.get(position).split("##")[1], recentItems.get(position).split("##")[0], newGroceryItem, "Vegetable");
        }else{
            bottomSheetFragment.setItemData("", recentItems.get(position).split("##")[0], newGroceryItem, "Vegetable");
        }
        bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
    }
}