package com.example.grocerylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.LinkedList;
import java.util.List;

public class CreateFruitListActivity extends AppCompatActivity {
    private static final String TAG = "CreateNewListActivity";
    public static final List<String> allFruits = new LinkedList<>();
    public static final String NEW_LIST = "NewList";
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    public DatabaseReference childDatabaseReference = null;
    private String imageString = "";
    private String intentValue = "";
    public BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance();
    public static GroceryItem newGroceryItem = new GroceryItem();
    public static SharedPreferences.Editor editor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_fruits_list);
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
            if(editor == null){
                editor = sharedPreferences.edit();
            }
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
                    }else{
                        childDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                imageString = (String) snapshot.getValue();
                                Log.e(TAG, "Firebase fruit URL: " + imageString);
                                bottomSheetFragment.setItemData(imageString, (String) adapterView.getItemAtPosition(position), newGroceryItem, "Fruit");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "An exception has occurred: ", error.toException());
                                bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Fruit");
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
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
}