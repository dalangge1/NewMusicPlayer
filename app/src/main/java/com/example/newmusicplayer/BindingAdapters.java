package com.example.newmusicplayer;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.newmusicplayer.custom.MyImageView;

public class BindingAdapters {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }


    @BindingAdapter("android:src")
    public static void setSrc(MyImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:src")
    public static void setSrc(MyImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

}