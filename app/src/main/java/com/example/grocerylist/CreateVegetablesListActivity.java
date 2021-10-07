package com.example.grocerylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

public class CreateVegetablesListActivity extends CreateFruitListActivity {
    private static final String TAG = "CreateVegListActivity";
    public static final List<String> allVegetables = new LinkedList<>();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    public DatabaseReference childDatabaseReference = null;
    private String imageString = "";
    private SharedPreferences.Editor editor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_vegetables_list);
            SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
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
                        bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Vegetable", sharedPreferences);
                    }else{
                        childDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                imageString = (String) snapshot.getValue();
                                Log.e(TAG, "Firebase fruit URL: " + imageString);
                                bottomSheetFragment.setItemData(imageString, (String) adapterView.getItemAtPosition(position), newGroceryItem, "Vegetable", sharedPreferences);
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "An exception has occurred: ", error.toException());
                                bottomSheetFragment.setItemData("", (String) adapterView.getItemAtPosition(position), newGroceryItem, "Vegetable", sharedPreferences);
                                bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                            }
                        });
                    }
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
    protected void onPause() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonContent = gson.toJson(BottomSheetFragment.getGroceryItem());
        editor.putString(NEW_LIST, jsonContent);
        editor.apply();
        super.onPause();
    }
}