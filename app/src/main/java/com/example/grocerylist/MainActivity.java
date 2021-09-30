package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private static final String TAG = "MainActivity";
    List<GroceryItem> groceryItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_grocery_item);
        GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter(groceryItemList);
        groceryItemAdapter.setItemClickListener(this);
        recyclerView.setAdapter(groceryItemAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        createDummyList(groceryItemList);
        Log.e(TAG, "Final list: " + groceryItemList);
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
}