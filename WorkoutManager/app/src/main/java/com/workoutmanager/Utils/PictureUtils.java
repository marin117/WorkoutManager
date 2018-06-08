package com.workoutmanager.Utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PictureUtils {

    public static void loadPicture(String picture, int x, int y, CircleImageView image){
        Picasso.get().load(picture).
                resize(x, y).
                centerCrop().
                into(image);
    }

    public static void loadPicture(String picture, int x, int y, ImageView image){
        Picasso.get().load(picture).
                resize(x, y).
                centerCrop().
                into(image);
    }
}
