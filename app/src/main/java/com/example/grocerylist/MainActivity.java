package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private static final String TAG = "MainActivity";
    List<GroceryItem> groceryItemList = new ArrayList<>();
    private GroceryItemAdapter groceryItemAdapter;
    private AutoCompleteTextView searchBox;
    private RecyclerView recyclerView = null;
    public static final String NEW_LIST = "NewList";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        searchBox = findViewById(R.id.search_box);
        groceryItemAdapter = new GroceryItemAdapter(groceryItemList);
        groceryItemAdapter.setItemClickListener(this);
        recyclerView = findViewById(R.id.recycler_view_grocery_item);
        recyclerView.setAdapter(groceryItemAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        createDummyList(groceryItemList);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!searchBox.isCursorVisible() && charSequence.toString().length() > 0){
                    searchBox.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0){
                    if(searchBox.isCursorVisible()){
                        searchBox.setCursorVisible(false);
                    }
                    searchBox.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_round_search_24), null, null, null);
                }else{
                    searchBox.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                }
                filterList(editable.toString());
                //searchBox.setFocusableInTouchMode(true);
            }
        });
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchBox.isCursorVisible()){
                    searchBox.setCursorVisible(true);
                }
            }
        });
        ExtendedFloatingActionButton newList = findViewById(R.id.new_list_fab);
        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateFruitListActivity.class);
                intent.putExtra("ClearData", "true");
                startActivity(intent);
            }
        });
        Log.e(TAG, "Final list: " + groceryItemList);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filterList(String searchString) {
        if(!groceryItemList.isEmpty()){
            List<GroceryItem> searchResults = new ArrayList<>();
            for(GroceryItem groceryItem : groceryItemList){
                if(groceryItem.getTitle().toLowerCase().startsWith(searchString.toLowerCase())){
                    searchResults.add(groceryItem);
                }
            }
            groceryItemAdapter.updateList(searchResults);
            recyclerView.setAdapter(groceryItemAdapter);// To reflect display items after filtering.
        }
    }

    private void createDummyList(List<GroceryItem> groceryItemList) {
        GroceryItem groceryItem1 = new GroceryItem();
        groceryItem1.setTitle("Sunday's Grocery");
        List<Fruit> fruits = new ArrayList<>();
        fruits.add(new Fruit("Apple"));
        fruits.add(new Fruit("Orange"));
        groceryItem1.setFruitList(fruits);
        List<Vegetable> vegetables = new ArrayList<>();
        vegetables.add(new Vegetable("Broccoli"));
        vegetables.add(new Vegetable("Broccoli"));
        vegetables.add(new Vegetable("Broccoli"));
        vegetables.add(new Vegetable("Broccoli"));
        vegetables.add(new Vegetable("Broccoli"));
        groceryItem1.setVegetableList(vegetables);
        List<Spice> spices = new ArrayList<>();
        spices.add(new Spice("Chilli"));
        groceryItem1.setSpiceList(spices);
        groceryItemList.add(groceryItem1);

        GroceryItem groceryItem2 = new GroceryItem();
        groceryItem2.setTitle("Dinner List");
        fruits = new ArrayList<>();
        fruits.add(new Fruit("Apple"));
        groceryItem2.setFruitList(fruits);
        vegetables = new ArrayList<>();
        vegetables.add(new Vegetable("Broccoli"));
        vegetables.add(new Vegetable("Broccoli"));
        vegetables.add(new Vegetable("Broccoli"));
        groceryItem2.setVegetableList(vegetables);
        List<Bread> breads = new ArrayList<>();
        breads.add(new Bread("Flour"));
        groceryItem2.setBreadList(breads);
        groceryItemList.add(groceryItem2);
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {
        Intent intent = new Intent(this, GroceryItemPopupActivity.class);
        Gson gson = new Gson();
        String jsonGroceryListObject = gson.toJson(groceryItemList.get(position));
        intent.putExtra("Popup", jsonGroceryListObject);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchBox.setFocusable(false);
        closeKeyboard();
        searchBox.setFocusableInTouchMode(true);
    }
}