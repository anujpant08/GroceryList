package com.example.grocerylist;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetFragment";
    String base64Image = "";
    public static BottomSheetFragment newInstance() {
        return new BottomSheetFragment();
    }

    public void setImage(String base64Image){
        this.base64Image = base64Image;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);
        Log.e(TAG,"base64 data: " + base64Image);
        ImageView imageView = (ImageView) contentView.findViewById(R.id.item_image);
        if(base64Image != null && !base64Image.equals("")){
            Glide.with(requireContext()).load(base64Image).into(imageView);
        }else{
            Glide.with(requireContext()).load(R.drawable.fruit).into(imageView);
        }
    }
}