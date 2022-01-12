package com.kdbl.mywatchlist;

import android.graphics.Bitmap;

public class ImageEditor {
    public static Bitmap resize(Bitmap original, int targetWidth, int targetHeight) {
        return Bitmap.createScaledBitmap(original, targetWidth, targetHeight, true);
    }
}
