package com.adeasy.advertise.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ImageQualityReducer {

    public static byte[] reduceQuality(Drawable drawable){
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    public static byte[] reduceQualityFromBitmap(Uri uri, Context context){
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

}
