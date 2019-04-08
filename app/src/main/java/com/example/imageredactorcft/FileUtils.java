package com.example.imageredactorcft;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class FileUtils {

    private static final String INITIAL_FILE_NAME="CFT_1.jpg";
    public static final String STR_FILE_PROVIDER="com.example.imageredactorcft.fileprovider";

    public static void saveBitmapToFile(Bitmap bitmap, String fileName){
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
	}

    public static void setExif(String newFilePath, ExifInterface oldExif, String newDateTime, String newDeviceName) {
        try {
            ExifInterface newExif = new ExifInterface(newFilePath);
            final Class<ExifInterface> cls = ExifInterface.class;
            final Field[] fields = cls.getFields();
            for (Field field : fields) {
                final String fieldName = field.getName();
                if (!TextUtils.isEmpty(fieldName) && fieldName.startsWith("TAG")) {
                    final String tag = field.get(cls).toString();
                    final String value = oldExif.getAttribute(tag);
                    if (value != null) {
                        newExif.setAttribute(tag, value);
                    }
                }
            }
			newExif.setAttribute(ExifInterface.TAG_DATETIME, newDateTime);
			newExif.setAttribute(ExifInterface.TAG_MODEL, newDeviceName);
            newExif.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
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
