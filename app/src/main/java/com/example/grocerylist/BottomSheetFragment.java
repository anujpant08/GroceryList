package com.example.grocerylist;

import static com.example.grocerylist.CreateFruitListActivity.NEW_LIST;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetFragment";
    private String base64Image = "";
    private String itemName = "";
    private String quantityValue = "";
    private String weightValue = "";
    public static GroceryItem groceryItem = null;
    private View contentView = null;
    private String family = "";

    public static BottomSheetFragment newInstance() {
        return new BottomSheetFragment();
    }

    public void setItemData(String base64Image, String fruitName, GroceryItem groceryItem, String family){
        Log.e(TAG, "grocery item in BottomSheetFragment: " + groceryItem);
        this.base64Image = base64Image;
        this.itemName = fruitName;
        BottomSheetFragment.groceryItem = groceryItem;
        this.family = family;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);
        Log.e(TAG,"base64 data: " + base64Image);
        ImageView imageView = (ImageView) contentView.findViewById(R.id.item_image);
        TextView textView = (TextView) contentView.findViewById(R.id.item_name);
        EditText quantity = (EditText) contentView.findViewById(R.id.quantity_edit_text);
        EditText weight = (EditText) contentView.findViewById(R.id.weight_edit_text);
        textView.setText(itemName);
        if(base64Image != null && !base64Image.equals("")){
            Glide.with(requireContext()).load(base64Image).into(imageView);
        }else{
            switch(family){
                case "Fruit":
                    Glide.with(requireContext()).load(R.drawable.fruit).into(imageView);
                    break;
                case "Vegetable":
                    Glide.with(requireContext()).load(R.drawable.vegetables).into(imageView);
                    break;

            }
        }
        ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) contentView.findViewById(R.id.add_to_list);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityValue = quantity.getText().toString();
                weightValue = weight.getText().toString();
                switch(family){
                    case "Fruit":
                        BottomSheetFragment.this.setFruitData();
                        break;
                    case "Vegetable":
                        BottomSheetFragment.this.setVegetableData();
                        break;
                    case "Spice":
                        break;
                    case "Bread":
                        break;
                    case "Dairy":
                        break;
                }
            }
        });
    }

    private void setFruitData() {
        Fruit fruit = new Fruit(itemName);
        fruit.setQuantity(quantityValue);
        fruit.setWeight(weightValue);
        Log.e(TAG, "Fruit added: " + fruit);
        if(BottomSheetFragment.groceryItem == null){
            BottomSheetFragment.groceryItem = new GroceryItem();
        }
        Set<Fruit> fruits;
        if (BottomSheetFragment.groceryItem.getFruitList() != null) {
            fruits = new LinkedHashSet<>(BottomSheetFragment.groceryItem.getFruitList());
        } else {
            fruits = new LinkedHashSet<>();
        }
        fruits.remove(fruit);
        fruits.add(fruit);//Updating fruit list with new fruit
        BottomSheetFragment.groceryItem.setFruitList(new ArrayList<>(fruits));
        Log.e(TAG, "new grocery item: " + BottomSheetFragment.groceryItem);
        Snackbar snackbar = Snackbar.make(contentView.getRootView(), itemName + " added to the list", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.fruit_red));
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.setAnchorView(contentView);
        snackbar.show();
    }

    private void setVegetableData(){
        Vegetable vegetable = new Vegetable(itemName);
        vegetable.setQuantity(quantityValue);
        vegetable.setWeight(weightValue);
        Log.e(TAG, "Vegetable added: " + vegetable);
        if(BottomSheetFragment.groceryItem == null){
            BottomSheetFragment.groceryItem = new GroceryItem();
        }
        Set<Vegetable> vegetables;
        if (BottomSheetFragment.groceryItem.getVegetableList() != null) {
            vegetables = new LinkedHashSet<>(BottomSheetFragment.groceryItem.getVegetableList());
        } else {
            vegetables = new LinkedHashSet<>();
        }
        vegetables.remove(vegetable);
        vegetables.add(vegetable);
        BottomSheetFragment.groceryItem.setVegetableList(new ArrayList<>(vegetables));
        Snackbar snackbar = Snackbar.make(contentView.getRootView(), itemName + " added to the list", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_green));
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.setAnchorView(contentView);
        snackbar.show();
    }

    public static GroceryItem getGroceryItem() {
        return BottomSheetFragment.groceryItem;
    }
}