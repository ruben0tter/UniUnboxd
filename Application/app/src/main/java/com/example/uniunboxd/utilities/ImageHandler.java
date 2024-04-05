package com.example.uniunboxd.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * The ImageHandler class provides a method for decoding a Base64 encoded image string into a Bitmap.
 */
public class ImageHandler {
    /**
     * Decodes a Base64 encoded image string into a Bitmap.
     * @param image The Base64 encoded image string.
     * @return The decoded Bitmap.
     */
    public static Bitmap decodeImageString(String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
