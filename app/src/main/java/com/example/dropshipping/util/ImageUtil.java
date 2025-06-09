package com.example.dropshipping.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;


public class ImageUtil {

    public static void createImage(Context context, String base64Image, ImageView imageView){
        try{
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(context).load(decodedByte).placeholder(R.drawable.loading).into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
