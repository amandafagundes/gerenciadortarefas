package com.strider.desafio.gerenciamentotarefas.Util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Util {
    /*
     * Reduz o tamanho de imagens que tenham tamanho superior a  2048 x 2048*/
    public static void compressInputImage(Intent data, Context context, ImageView newIV) {
        Bitmap bitmap;
        Uri inputImageData = data.getData();
        try {
            Bitmap bitmapInputImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), inputImageData);
            if (bitmapInputImage.getWidth() > 2048 && bitmapInputImage.getHeight() > 2048) {
                bitmap = Bitmap.createScaledBitmap(bitmapInputImage, 1024, 1280, true);
                newIV.setImageBitmap(bitmap);
            } else if (bitmapInputImage.getWidth() > 2048 && bitmapInputImage.getHeight() < 2048) {
                bitmap = Bitmap.createScaledBitmap(bitmapInputImage, 1920, 1200, true);
                newIV.setImageBitmap(bitmap);
            } else if (bitmapInputImage.getWidth() < 2048 && bitmapInputImage.getHeight() > 2048) {
                bitmap = Bitmap.createScaledBitmap(bitmapInputImage, 1024, 1280, true);
                newIV.setImageBitmap(bitmap);
            } else if (bitmapInputImage.getWidth() < 2048 && bitmapInputImage.getHeight() < 2048) {
                bitmap = Bitmap.createScaledBitmap(bitmapInputImage, bitmapInputImage.getWidth(), bitmapInputImage.getHeight(), true);
                newIV.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.e("Exception:", e.toString());
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public static String getImagePath(Context context, Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

}
