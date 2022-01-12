package com.kdbl.mywatchlist;

import android.graphics.Bitmap;

public class ImageEditor {
    public static Bitmap resize(Bitmap original, int screenWidth, int screenHeight) {
        if(screenWidth == 0 || screenHeight == 0) {
            return  Bitmap.createScaledBitmap(original, original.getWidth() * 2, original.getHeight() * 2, true);
        }
        return Bitmap.createScaledBitmap(original,
                (int)(original.getWidth() * ((int)(screenHeight / 4.5) / (double)original.getHeight())),
                (int)(screenHeight / 4.5), true);
    }
}
