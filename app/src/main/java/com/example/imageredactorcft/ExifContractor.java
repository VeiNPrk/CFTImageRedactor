package com.example.imageredactorcft;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.List;

public interface ExifContractor {
    interface View {
		void initData();
    }

    interface Presenter {
       void onInitViews(Intent state);
		//void isBitmapNullError();
		void onDestroy();
		String getExifResolution();
		String getExifCamera();
		String getExifISO();
		String getExifDate();
		String getExifFPower();
    }
}
