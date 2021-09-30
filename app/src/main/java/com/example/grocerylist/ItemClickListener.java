package com.example.grocerylist;

import android.view.View;

public interface ItemClickListener {

    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}