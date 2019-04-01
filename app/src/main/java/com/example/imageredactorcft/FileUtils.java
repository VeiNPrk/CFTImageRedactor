package com.example.imageredactorcft;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtils {

    public static final String INITIAL_FILE_NAME="CFT_1.jpg";
    public static final String STR_FILE_PROVIDER="";

    public static void saveBitmapToFile(Bitmap bitmap, String fileName){
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromFile(File file, Activity activity){
        Bitmap bitmap = null;
        if(file==null || !file.exists()){
            bitmap = null;
        }
        else{
            bitmap = PictureUtils.getScaledBitmap(file.getPath(), activity);
            //imvRedactor.setImageBitmap(bitmap);
        }
        return bitmap;
    }

    public static String getFileName(){
        return INITIAL_FILE_NAME;
    }

    public static String getFileName(String nameFile){
        String name="CFT_"+nameFile+".jpg";
        return name;
    }

    public static File getPhotoFile(Context context, String nameFile){
        File filesDir = context.getFilesDir();
        return new File(filesDir,nameFile);
    }
	
	public static boolean deleteFile(String path){
		File fdelete = new File(path);
		if (fdelete.exists())
			return fdelete.delete();
		else
			return false;
			/*if (fdelete.delete()) {
				System.out.println("file Deleted :" + uri.getPath());
			} else {
				System.out.println("file not Deleted :" + uri.getPath());
			}*/

	}

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
