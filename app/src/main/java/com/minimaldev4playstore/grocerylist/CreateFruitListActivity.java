package com.minimaldev4playstore.grocerylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CreateFruitListActivity extends AppCompatActivity implements ItemClickListener {
    private static final String TAG = "CreateNewListActivity";
    public static final List<String> allFruits = new LinkedList<>();
    public static final String NEW_LIST = "NewList";
    private static final String RECENT_FRUIT_LIST = "RecentFruitList";
    protected final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    protected final DatabaseReference databaseReference = firebaseDatabase.getReference();
    protected DatabaseReference childDatabaseReference = null;
    private String imageString = "";
    private String intentValue = "";
    public BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance();
    public static GroceryItem newGroceryItem = new GroceryItem();
    public static SharedPreferences.Editor editor = null;
    private List<String> recentItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_fruits_list);
            TextView factTextView = findViewById(R.id.fact_fruit);
            String fact = getFunFact();
            factTextView.setText(fact);
            if(savedInstanceState == null){
                Intent intent = getIntent();
                intentValue = intent.getStringExtra("ClearData");
                if(intentValue != null && intentValue.equals("true")){
                    Log.e(TAG, "claerdata oncreate furit list activity");
                    SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    newGroceryItem = new GroceryItem();
                    BottomSheetFragment.groceryItem = null;
                }
            }
            ImageView imageView = (ImageView)findViewById(R.id.feature_fruit);
            Glide.with(this).load(R.drawable.fruit).into(imageView);
            String fruitsJSON = "";
            SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
            SharedPreferences recentsSharedPrefs = this.getSharedPreferences(RECENT_FRUIT_LIST, Context.MODE_PRIVATE);
            SharedPreferences.Editor recentEditor = recentsSharedPrefs.edit();
            if(editor == null){
                editor = sharedPreferences.edit();
            }
            recentItems = new LinkedList<>(recentsSharedPrefs.getStringSet(RECENT_FRUIT_LIST, new LinkedHashSet<>()));
            Log.e(TAG, "recents list: " + recentItems);
            RecyclerView recentsRecyclerView = findViewById(R.id.recents_recycler_view);
            RecentItemsAdapter recentItemsAdapter = new RecentItemsAdapter(recentItems, this, "Fruit");
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
            recentsRecyclerView.setLayoutManager(gridLayoutManager);
            recentsRecyclerView.setAdapter(recentItemsAdapter);
            recentItemsAdapter.notifyDataSetChanged();
            recentItemsAdapter.setItemClickListener(this);
            //newGroceryItem = new GroceryItem();
            if(allFruits.isEmpty()){
                InputStream inputStream = this.getAssets().open("fruits.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                fruitsJSON = new String(buffer, StandardCharsets.UTF_8);
                parseJSON(fruitsJSON);
            }
            AutoCompleteTextView searchBox = (AutoCompleteTextView) findViewById(R.id.search_text);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_search_view, allFruits);
            searchBox.setThreshold(1);
            searchBox.setAdapter(adapter);
            searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    //Add bottom popup to show fruit details with fruit image
                    childDatabaseReference = databaseReference.child("Fruits/" + adapterView.getItemAtPosition(position));
                    if(!isNetworkAvailable()){
                        bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Fruit");
                        bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                        Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_FRUIT_LIST, new LinkedHashSet<>()));
                        recents.add(adapterView.getItemAtPosition(position) + "##");
                        recentEditor.putStringSet(RECENT_FRUIT_LIST, recents);
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
                                Log.e(TAG, "Firebase fruit URL: " + imageString);
                                bottomSheetFragment.setItemData(imageString, (String) adapterView.getItemAtPosition(position), newGroceryItem, "Fruit");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                                Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_FRUIT_LIST, new LinkedHashSet<>()));
                                recents.add(adapterView.getItemAtPosition(position) + "##" + imageString);
                                recentEditor.putStringSet(RECENT_FRUIT_LIST, recents);
                                recentEditor.apply();
                                recentItems.clear();
                                recentItems.addAll(recents);
                                recentItemsAdapter.notifyDataSetChanged();
                                recentsRecyclerView.setAdapter(recentItemsAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "An exception has occurred: ", error.toException());
                                bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Fruit");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                                Set<String> recents = new LinkedHashSet<>(recentsSharedPrefs.getStringSet(RECENT_FRUIT_LIST, new LinkedHashSet<>()));
                                recents.add(adapterView.getItemAtPosition(position) + "##");
                                recentEditor.putStringSet(RECENT_FRUIT_LIST, recents);
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
            ExtendedFloatingActionButton addVeggies = (ExtendedFloatingActionButton) findViewById(R.id.get_veggies);
            addVeggies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateFruitListActivity.this, CreateVegetablesListActivity.class);
                    intentValue = "";
                    Gson gson = new Gson();
                    String jsonContent = gson.toJson(BottomSheetFragment.getGroceryItem());
                    Log.e(TAG,"new list in fruits: " + jsonContent);
                    editor.putString(NEW_LIST, jsonContent);
                    editor.apply();
                    startActivity(intent);
                }
            });
            TextView clearTextView = findViewById(R.id.recents_clear);
            clearTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recentItemsAdapter.notifyItemRangeRemoved(0, recentItems.size());
                    recentItems.clear();
                    recentEditor.putStringSet(RECENT_FRUIT_LIST, null);
                    recentEditor.apply();
                }
            });

        }catch(Exception exception){
            Log.e(TAG, "An exception occurred: ", exception);
        }
    }
    private void parseJSON(String json){
        try{
            JSONObject response = new JSONObject(json);
            JSONArray fruitsArray = response.getJSONArray("fruits");
            String fruitName = "";
            for(int index = 0; index < fruitsArray.length(); index++){
                fruitName = fruitsArray.getString(index);
                fruitName = Character.toUpperCase(fruitName.charAt(0)) + fruitName.substring(1);
                allFruits.add(fruitName);
            }
        }catch(Exception exception){
            Log.e(TAG, "An exception occurred while JSON parsing: ", exception);
        }
    }
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        newGroceryItem = BottomSheetFragment.getGroceryItem();
        Log.e(TAG, "resume of fruit list activity: " + newGroceryItem);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
        editor.clear();
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onClick(View view, int position) {
        String[] data = recentItems.get(position).split("##");
        if(data.length > 1){
            bottomSheetFragment.setItemData(recentItems.get(position).split("##")[1], recentItems.get(position).split("##")[0], newGroceryItem, "Fruit");
        }else{
            bottomSheetFragment.setItemData("", recentItems.get(position).split("##")[0], newGroceryItem, "Fruit");
        }
        bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
    }

    @Override
    public void onLongClick(View view, int position) {

    }
    private String getFunFact() throws Exception{
        String fact = "";
        InputStream inputStream = this.getAssets().open("fun facts fruits.json");
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String factJSON = new String(buffer, StandardCharsets.UTF_8);
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 14);
        JSONObject jsonObject = new JSONObject(factJSON);
        JSONArray factsArray = jsonObject.getJSONArray("facts");
        fact = factsArray.getString(randomNumber);
        return "Fun fact: " + fact;
    }
}