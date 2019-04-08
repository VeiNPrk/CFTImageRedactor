package com.example.imageredactorcft;

import android.content.Intent;

public interface ExifContractor {
    interface View {
		void initData();
    }

    interface Presenter {
       void onInitViews(Intent state);
		void onDestroy();
		String getExifResolution();
		String getExifCamera();
		String getExifISO();
		String getExifDate();
		String getExifFPower();
    }
}
