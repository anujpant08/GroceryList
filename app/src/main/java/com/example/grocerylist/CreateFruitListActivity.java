package com.example.grocerylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class CreateFruitListActivity extends AppCompatActivity {
    private static final String TAG = "CreateNewListActivity";
    public static final List<String> allFruits = new LinkedList<>();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    public DatabaseReference childDatabaseReference = null;
    private String imageString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_fruits_list);
            ImageView imageView = (ImageView)findViewById(R.id.feature_fruit);
            Glide.with(this).load(R.drawable.fruit).into(imageView);
            String fruitsJSON = "";
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
                    Log.e(TAG, "File name: " + "Fruits/" + adapterView.getItemAtPosition(position));
                    childDatabaseReference = databaseReference.child("Fruits/" + adapterView.getItemAtPosition(position));
                    childDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            imageString = (String)snapshot.getValue();
                            Log.e(TAG,"Firebase fruit URL: " + imageString);
                            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance();
                            bottomSheetFragment.setImage(imageString);
                            bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "An exception has occurred: ", error.toException());
                            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance();
                            bottomSheetFragment.setImage("");
                            bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
                        }
                    });
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
}