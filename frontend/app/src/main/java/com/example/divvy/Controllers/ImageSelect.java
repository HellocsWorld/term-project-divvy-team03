package com.example.divvy.Controllers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageSelect {

    private static double MAX_LINEAR_DIMENSION = 500;

    static Bitmap getBitmapForUri(ContentResolver contentResolver, Uri imageUri){
        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);

        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    static void selectImage(Activity activity){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, 1);
    }

    static Bitmap scaleImage(Bitmap bitmap){
        int originalHeight = bitmap.getHeight();
        int originalWidth = bitmap.getWidth();

        double scaleFactor = MAX_LINEAR_DIMENSION / (double)(originalHeight + originalWidth);

        if(scaleFactor < 1.0){
            int targetWidth = (int) Math.round(originalWidth * scaleFactor);
            int targetHeight = (int) Math.round(originalHeight * scaleFactor);
            return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
        }else{
            return bitmap;
        }
    }

    static Uri savePhotoImage(Bitmap bitmap, Activity activity){
        File photoFile = null;
        try{
            photoFile = createImageFile(activity);
        }catch (IOException e){
            e.printStackTrace();
        }

        if(photoFile == null){
            Log.d("ERROR: ", "Error creating media file");
            return null;
        }

        try{
            FileOutputStream fos = new FileOutputStream(photoFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return Uri.fromFile(photoFile);
    }

    static File createImageFile(Activity activity) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String imageFileNamePrefix = "Divvy-" + timeStamp;
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageFileNamePrefix, ".jpg", storageDir);
        return imageFile;
    }

    static Bitmap getBitmap(Intent data, Activity activity){
        Uri uri = data.getData();
        Bitmap bitmap = getBitmapForUri(activity.getContentResolver(), uri);
        Bitmap resizedBitmap = scaleImage(bitmap);
        if(bitmap != resizedBitmap){
            savePhotoImage(resizedBitmap, activity);
            bitmap = resizedBitmap;
        }
        return bitmap;
    }

    static String encodeImage(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            is.close();
            return d;
        } catch (Exception e) {
            Log.d("ERROR: ", e.toString());
            return null;
        }
    }

    public static Bitmap decodeImage(String data)
    {
        byte[] b = Base64.decode(data,Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }

    public static class ImageRetrieverTask extends AsyncTask<Object, Void, Drawable>{
        private ImageView imageView;
        public ImageRetrieverTask(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Drawable doInBackground(Object... url) {
            return LoadImageFromWebOperations((String) url[0]);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            imageView.setImageDrawable(drawable);
            this.cancel(true);
        }
    }
}
