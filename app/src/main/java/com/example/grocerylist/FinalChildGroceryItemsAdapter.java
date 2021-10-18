package com.example.grocerylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FinalChildGroceryItemsAdapter extends RecyclerView.Adapter<FinalChildGroceryItemsAdapter.FinalChildViewHolder> {
    private static final String TAG = "FinalChildItemsAdapter";
    List<? extends ItemCategoryInterface> eachCategoryItems = null;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    public DatabaseReference childDatabaseReference = null;
    private String imageString = "";
    private final Context context;
    private GroceryItem newGroceryItem;
    public FinalChildGroceryItemsAdapter(Context context, List<? extends ItemCategoryInterface> eachCategoryItems, GroceryItem groceryItem) {
        this.context = context;
        this.eachCategoryItems = eachCategoryItems;
        newGroceryItem = groceryItem;
    }

    @NonNull
    @Override
    public FinalChildGroceryItemsAdapter.FinalChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_child_custom_view, parent, false);
        return new FinalChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinalChildGroceryItemsAdapter.FinalChildViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.e(TAG, "value is: " + eachCategoryItems.get(position).getQuantity() + " weight is: " + eachCategoryItems.get(position).getWeight());
        childDatabaseReference = databaseReference.child(eachCategoryItems.get(position).getFamily() + "/" + eachCategoryItems.get(position).getName());
        if(!isNetworkAvailable()){
            switch (eachCategoryItems.get(position).getFamily()){
                case "Fruits":
                    Glide.with(context).load(R.drawable.fruit).into(holder.itemImage);
                    break;
                case "Vegetables":
                    Glide.with(context).load(R.drawable.vegetables).into(holder.itemImage);
                    break;
                case "Spices":
                    Glide.with(context).load(R.drawable.spice).into(holder.itemImage);
                    break;
                case "Others":
                    Glide.with(context).load(R.drawable.dairybread).into(holder.itemImage);
                    break;
            }
        }else{
            childDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageString = (String) snapshot.getValue();
                    Log.e(TAG, "Firebase fruit URL: " + imageString);
                    if(imageString == null || imageString.equals("")) {
                        switch (eachCategoryItems.get(position).getFamily()) {
                            case "Fruits":
                                Glide.with(context).load(R.drawable.fruit).into(holder.itemImage);
                                break;
                            case "Vegetables":
                                Glide.with(context).load(R.drawable.vegetables).into(holder.itemImage);
                                break;
                            case "Spices":
                                Glide.with(context).load(R.drawable.spice).into(holder.itemImage);
                                break;
                            case "Others":
                                Glide.with(context).load(R.drawable.dairybread).into(holder.itemImage);
                                break;
                        }
                    }else{
                        Glide.with(context).load(imageString).into(holder.itemImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "An exception has occurred: ", error.toException());
                    switch (eachCategoryItems.get(position).getFamily()) {
                        case "Fruits":
                            Glide.with(context).load(R.drawable.fruit).into(holder.itemImage);
                            break;
                        case "Vegetables":
                            Glide.with(context).load(R.drawable.vegetables).into(holder.itemImage);
                            break;
                        case "Spices":
                            Glide.with(context).load(R.drawable.spice).into(holder.itemImage);
                            break;
                        case "Others":
                            Glide.with(context).load(R.drawable.dairybread).into(holder.itemImage);
                            break;
                    }
                }
            });
        }
        holder.itemNameTextView.setText(eachCategoryItems.get(position).getName());
        if(eachCategoryItems.get(position).getQuantity() != null && !eachCategoryItems.get(position).getQuantity().equals("")){
            holder.itemQuantityTextView.setText(eachCategoryItems.get(position).getQuantity() + " Qty.");
        }else if(eachCategoryItems.get(position).getWeight() != null && !eachCategoryItems.get(position).getWeight().equals("")){
            holder.itemQuantityTextView.setText(eachCategoryItems.get(position).getWeight());
        }
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (eachCategoryItems.get(holder.getAdapterPosition()).getFamily()) {
                    case "Fruits":
                        eachCategoryItems.remove(holder.getAdapterPosition());
                        newGroceryItem.setFruitList((List<Fruit>) eachCategoryItems);
                        break;
                    case "Vegetables":
                        eachCategoryItems.remove(holder.getAdapterPosition());
                        newGroceryItem.setVegetableList((List<Vegetable>) eachCategoryItems);
                        break;
                    case "Spices":
                        eachCategoryItems.remove(holder.getAdapterPosition());
                        newGroceryItem.setSpiceList((List<Spice>) eachCategoryItems);
                        break;
                    case "Others":
                        eachCategoryItems.remove(holder.getAdapterPosition());
                        newGroceryItem.setOthersList((List<Others>) eachCategoryItems);
                        break;
                }
                BottomSheetFragment.groceryItem = newGroceryItem;
                notifyDataSetChanged();
            }
        });
        holder.decreaseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.itemQuantityTextView.getText() != null && !holder.itemQuantityTextView.getText().equals("")){
                    String text = holder.itemQuantityTextView.getText().toString();
                    double value;
                    if(text.contains("Kg")){
                        value = Double.parseDouble(text.substring(0, text.indexOf("K")).trim());
                        if(value > 1){
                            --value;
                            holder.itemQuantityTextView.setText(value + " Kg");
                            eachCategoryItems.get(holder.getAdapterPosition()).setWeight(holder.itemQuantityTextView.getText().toString());
                        }
                    }else{
                        value = Double.parseDouble(text.substring(0, text.indexOf("Q")).trim());
                        if(value > 1){
                            --value;
                            Log.e(TAG, "value is: " + value + " text is:" + text);
                            holder.itemQuantityTextView.setText(String.valueOf(value));
                            eachCategoryItems.get(holder.getAdapterPosition()).setQuantity(holder.itemQuantityTextView.getText().toString());
                        }
                    }
                    switch (eachCategoryItems.get(position).getFamily()) {
                        case "Fruits":
                            newGroceryItem.setFruitList((List<Fruit>) eachCategoryItems);
                            break;
                        case "Vegetables":
                            newGroceryItem.setVegetableList((List<Vegetable>) eachCategoryItems);
                            break;
                        case "Spices":
                            newGroceryItem.setSpiceList((List<Spice>) eachCategoryItems);
                            break;
                        case "Others":
                            newGroceryItem.setOthersList((List<Others>) eachCategoryItems);
                            break;
                    }
                    BottomSheetFragment.groceryItem = newGroceryItem;
                    notifyDataSetChanged();
                }
            }
        });
        holder.increaseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.itemQuantityTextView.getText() != null && !holder.itemQuantityTextView.getText().equals("")){
                    String text = holder.itemQuantityTextView.getText().toString();
                    double value;
                    if(text.contains("Kg")){
                        value = Double.parseDouble(text.substring(0, text.indexOf("K")).trim());
                        ++value;
                        holder.itemQuantityTextView.setText(value + " Kg");
                        eachCategoryItems.get(holder.getAdapterPosition()).setWeight(holder.itemQuantityTextView.getText().toString());
                    }else{
                        value = Double.parseDouble(text.substring(0, text.indexOf("Q")).trim());
                        ++value;
                        holder.itemQuantityTextView.setText(String.valueOf(value));
                        eachCategoryItems.get(holder.getAdapterPosition()).setQuantity(holder.itemQuantityTextView.getText().toString());
                    }
                }else{
                    holder.itemQuantityTextView.setText("1");
                    eachCategoryItems.get(holder.getAdapterPosition()).setQuantity(holder.itemQuantityTextView.getText().toString());
                }
                switch (eachCategoryItems.get(position).getFamily()) {
                    case "Fruits":
                        newGroceryItem.setFruitList((List<Fruit>) eachCategoryItems);
                        break;
                    case "Vegetables":
                        newGroceryItem.setVegetableList((List<Vegetable>) eachCategoryItems);
                        break;
                    case "Spices":
                        newGroceryItem.setSpiceList((List<Spice>) eachCategoryItems);
                        break;
                    case "Others":
                        newGroceryItem.setOthersList((List<Others>) eachCategoryItems);
                        break;
                }
                BottomSheetFragment.groceryItem = newGroceryItem;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eachCategoryItems.size();
    }
    public class FinalChildViewHolder extends RecyclerView.ViewHolder{
        TextView itemNameTextView;
        TextView itemQuantityTextView;
        ImageView itemImage;
        ImageView removeItem;
        ImageButton decreaseImageButton;
        ImageButton increaseImageButton;
        public FinalChildViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = (TextView) itemView.findViewById(R.id.final_child_item_name);
            itemQuantityTextView = (TextView) itemView.findViewById(R.id.final_item_quantity);
            itemImage = (ImageView) itemView.findViewById(R.id.final_item_image);
            removeItem = (ImageView) itemView.findViewById(R.id.final_remove_item);
            decreaseImageButton = (ImageButton) itemView.findViewById(R.id.decrease);
            increaseImageButton = (ImageButton) itemView.findViewById(R.id.increase);
        }
    }
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
