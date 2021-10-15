package com.example.grocerylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class FinalListActivity extends CreateFruitListActivity {
    private static final String TAG = "FinalListActivity";
    private SharedPreferences.Editor editor = null;
    private List<GroceryItem> savedLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_list);
        EditText finalListName = findViewById(R.id.final_list_name);
        SharedPreferences sharedPreferences = this.getSharedPreferences(NEW_LIST, Context.MODE_PRIVATE);
        if(editor == null){
            editor = sharedPreferences.edit();
        }
        String jsonData = sharedPreferences.getString(NEW_LIST, "");
        Type type = new TypeToken<GroceryItem>(){}.getType();
        if(jsonData != null && !jsonData.equals("")){
            Log.e(TAG, "jsonDate in Final List Activity: " + jsonData);
            Gson gson = new Gson();
            newGroceryItem = gson.fromJson(jsonData, type);
        }
        Log.e(TAG, "In FinalActivity: " + newGroceryItem);
        List<List<? extends ItemCategoryInterface>> subItemsList = new LinkedList<>();
        RecyclerView recyclerView = findViewById(R.id.final_list_recycler_view);
        if(newGroceryItem != null){
            if (newGroceryItem.getFruitList() != null && !newGroceryItem.getFruitList().isEmpty()) {
                subItemsList.add(newGroceryItem.getFruitList());
            }
            if (newGroceryItem.getVegetableList() != null && !newGroceryItem.getVegetableList().isEmpty()) {
                subItemsList.add(newGroceryItem.getVegetableList());
            }
            if (newGroceryItem.getSpiceList() != null && !newGroceryItem.getSpiceList().isEmpty()) {
                subItemsList.add(newGroceryItem.getSpiceList());
            }
            if (newGroceryItem.getBreadList() != null && !newGroceryItem.getBreadList().isEmpty()) {
                subItemsList.add(newGroceryItem.getBreadList());
            }
            if (newGroceryItem.getDairyList() != null && !newGroceryItem.getDairyList().isEmpty()) {
                subItemsList.add(newGroceryItem.getDairyList());
            }
            ExtendedFloatingActionButton extendedFloatingActionButton = findViewById(R.id.done_fab);
            FinalListParentAdapter finalListParentAdapter = new FinalListParentAdapter(this, subItemsList, newGroceryItem);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(finalListParentAdapter);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                    if (dy > 0)
                        extendedFloatingActionButton.hide();
                    else if (dy < 0)
                        extendedFloatingActionButton.show();
                }
            });
            finalListParentAdapter.notifyDataSetChanged();
            String SAVEDLIST = "SavedList";
            SharedPreferences sharedPreferencesGroceryList = this.getSharedPreferences(SAVEDLIST, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferencesGroceryList.edit();
            String listsJsonData = sharedPreferencesGroceryList.getString(SAVEDLIST, "");
            Type typeList = new TypeToken<List<GroceryItem>>(){}.getType();
            if(listsJsonData != null && !listsJsonData.equals("")){
                Log.e(TAG, "already saved lists: " + listsJsonData);
                Gson gson = new Gson();
                savedLists = gson.fromJson(listsJsonData, typeList);
            }else{
                savedLists = new LinkedList<>();
            }
            Log.e(TAG,"Gor this list from sharedPrefs : " + savedLists);
            extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = finalListName.getText().toString();
                    if(title.length() == 0){
                        Snackbar.make(view, "Please provide the list name", Snackbar.LENGTH_LONG)
                                .setAnchorView(extendedFloatingActionButton)
                                .show();
                    }else{
                        newGroceryItem.setTitle(title);
                        for(List<? extends ItemCategoryInterface> subList : subItemsList){
                            if(subList.get(0).getFamily().equals("Fruits")){
                                newGroceryItem.setFruitList((List<Fruit>) subList);
                            }else if(subList.get(0).getFamily().equals("Vegetables")){
                                newGroceryItem.setVegetableList((List<Vegetable>) subList);
                            }
                        }
                        Log.e(TAG, "final list saved: " + newGroceryItem);
                        savedLists.add(newGroceryItem);
                        Gson gson = new Gson();
                        String jsonContent = gson.toJson(savedLists);
                        Log.e(TAG,"all list in saved lists: " + jsonContent);
                        editor.putString(SAVEDLIST, jsonContent);
                        editor.apply();
                        Intent intent = new Intent(FinalListActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }
}