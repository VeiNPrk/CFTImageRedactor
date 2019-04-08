package com.example.imageredactorcft;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		int inSampleSize = 1;
		if(srcHeight > destHeight || srcWidth > destWidth){
			float heightScale = srcHeight/destHeight;
			float widthScale = srcWidth/destWidth;
			
			inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
		}
		
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		return BitmapFactory.decodeFile(path, options);
	}
	
	public static Bitmap getScaledBitmap(String path, Activity activity){
		Point size = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(size);
		
		return getScaledBitmap(path, size.x, size.y);
	}

	public static Bitmap getRotateBitmap(Bitmap inpBitmap){
		Bitmap bOutput;
		float degrees = 90;
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees);
		bOutput = Bitmap.createBitmap(inpBitmap, 0, 0, inpBitmap.getWidth(), inpBitmap.getHeight(), matrix, true);
		return bOutput;
	}

	public static Bitmap getMirrorHorizonBitmap(Bitmap inpBitmap){
		Bitmap bOutput;
		Matrix matrix = new Matrix();
		matrix.preScale(-1.0f, 1.0f);
		bOutput = Bitmap.createBitmap(inpBitmap, 0, 0, inpBitmap.getWidth(), inpBitmap.getHeight(), matrix, true);
		return bOutput;
	}

	public static Bitmap getGrayBitmap(Bitmap inpBitmap){

        float[] matrix = new float[]{
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0.3f, 0.59f, 0.11f, 0, 0,
                0, 0, 0, 1, 0,};

        Bitmap dest = Bitmap.createBitmap(
                inpBitmap.getWidth(),
                inpBitmap.getHeight(),
                inpBitmap.getConfig());

        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(inpBitmap, 0, 0, paint);
        return dest;
    }
}
