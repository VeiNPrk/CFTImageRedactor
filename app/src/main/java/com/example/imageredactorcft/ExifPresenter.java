package com.example.imageredactorcft;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import java.io.IOException;

public class ExifPresenter implements ExifContractor.Presenter {
    public static final String EXIF_PATH_KEY="exif_path_key";

    ExifContractor.View view;
    Context context;
    ExifInterface exif = null;
    public ExifPresenter(ExifContractor.View _view, Context _context){
        view=_view;
        context = _context;
    }
	
	@Override
    public void onDestroy() {
        view=null;
    }
	
	@Override
	public void onInitViews(Intent savedState){
        if(savedState!=null)
        {
            String path = savedState.getStringExtra(EXIF_PATH_KEY);
            if(path!=null && path!=""){
				try {
					exif = new ExifInterface(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
		if(exif!=null)
			view.initData();
	}
	
	@Override
	public String getExifResolution(){
		String exifResolution="";
		String xResolution = exif.getAttribute(ExifInterface.TAG_PIXEL_X_DIMENSION);
		String yResolution = exif.getAttribute(ExifInterface.TAG_PIXEL_Y_DIMENSION);
		exifResolution= xResolution+" X "+yResolution;
		if(xResolution==null || yResolution==null)
			exifResolution=context.getString(R.string.exif_no_data);
		return exifResolution;
	}
	
	@Override
	public String getExifCamera(){
		String exifCamera="";
		exifCamera= exif.getAttribute(ExifInterface.TAG_MODEL);
		if(exifCamera==null)
			exifCamera=context.getString(R.string.exif_no_data);
		return exifCamera;
	}
	
	@Override
	public String getExifISO(){
		String exifISO="";
		exifISO= exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
		if(exifISO!=null)
			exifISO="1/"+exifISO;
		else
			exifISO=context.getString(R.string.exif_no_data);
		return exifISO;
	}
	
	@Override
	public String getExifDate(){
		String exifDate="";
		exifDate= exif.getAttribute(ExifInterface.TAG_DATETIME);
		if(exifDate==null)
			exifDate=context.getString(R.string.exif_no_data);
		return exifDate;
	}
	
	@Override
	public String getExifFPower(){
		String exifFPower="";
		exifFPower= exif.getAttribute(ExifInterface.TAG_F_NUMBER);
		if(exifFPower==null)
			exifFPower=context.getString(R.string.exif_no_data);
		return exifFPower;
	}
}
