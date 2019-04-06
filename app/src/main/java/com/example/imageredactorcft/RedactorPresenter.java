package com.example.imageredactorcft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RedactorPresenter implements RedactorContractor.Presenter {
    public final String TAG = getClass().getSimpleName();
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_EXIF = 3;
    public static final String FILE_NAME_STATE_KEY="file_name_state_key";

    RedactorContractor.View view;
    DBClass db;
    Activity activity;
    Context context;
    File photoFile = null;

    public RedactorPresenter(RedactorContractor.View _view, Context _context){
        view=_view;
        activity = (Activity) _view;
        context = _context;
        db = new DBClass();
    }

    @Override
    public void onPickImageClick() {
        view.viewDialog();
    }

    public void onExifClick(){
        if(photoFile!=null) {
			Intent intent = new Intent(context, ExifActivity.class);
			intent.putExtra(ExifPresenter.EXIF_PATH_KEY, photoFile.getPath());
			view.startActivityToResults(intent, REQUEST_EXIF);
        }
        else
            view.showError(context.getString(R.string.error_no_source));
    }

    private void/*String*/ saveNewImg(Bitmap bitmap){
        String newPath="";
        long newDateTime = new Date().getTime();
        try {
            String oldPath = photoFile.getPath();
            ExifInterface exif = new ExifInterface(oldPath);
            String fileName = String.valueOf(newDateTime);
            String newFileName = FileUtils.getFileName(fileName);
            newPath = FileUtils.getPhotoFile(context, newFileName).getPath();
            FileUtils.saveBitmapToFile(bitmap, newPath);
            SimpleDateFormat  df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			String strDateTime = df.format(newDateTime);
			String newDeviceName=context.getString(R.string.app_name);
			//String newDateString = DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()).toString();

            FileUtils.setExif(newPath, exif, strDateTime, newDeviceName);
            PictureClass newPicture = new PictureClass(newPath, newDateTime);
            db.savePicture(newPicture);
        }
        catch (Exception ex){
            view.showError(ex.getMessage());
        }
        //return newPath;
    }

    @Override
    public void onRotateImageClick() {
        if(photoFile!=null) {
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);

            Bitmap rotateBitmap = null;
            try {
                rotateBitmap = PictureUtils.getRotateBitmap(bitmap);
                if (rotateBitmap != null) {
                    saveNewImg(rotateBitmap);
                    view.updateRvData(db.getData());
                }
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        }
        else
            view.showError(context.getString(R.string.error_no_source));
    }

    @Override
    public void onMirrorImageClick() {
        if(photoFile!=null) {
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            Bitmap mirrorBitmap = null;
            try {
                mirrorBitmap = PictureUtils.getMirrorHorizonBitmap(bitmap);
                if (mirrorBitmap != null) {
                    saveNewImg(mirrorBitmap);
                    view.updateRvData(db.getData());
                }
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        }
        else
            view.showError(context.getString(R.string.error_no_source));
    }

    @Override
    public void onGrayImageClick() {
        if(photoFile!=null) {
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            Bitmap grayBitmap = null;
            try {
                grayBitmap = PictureUtils.getGrayBitmap(bitmap);
                if (grayBitmap != null) {
                    saveNewImg(grayBitmap);
                    view.updateRvData(db.getData());
                }
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        }
        else
            view.showError(context.getString(R.string.error_no_source));
    }
/*
    @Override
    public void onListItemClick() {

    }*/

    @Override
    public void onListItemRemoveClick(PictureClass picture) {
		try{
			if(db.delete(picture.getDateTime())>0) {
                if (FileUtils.deleteFile(picture.getPath())) {
                    view.updateRvData(db.getData());
                    if (photoFile!=null && picture.getPath().equals(photoFile.getPath())) {
                        view.updatePhotoView(null);
                        photoFile=null;
                    }
                }
                else {
                    picture.save();
                    view.showError(context.getString(R.string.delete_file_error));
                }
            }
            else
                view.showError(context.getString(R.string.delete_file_error));
		}
		catch(Exception ex){
			view.showError(ex.getMessage());
		}
        //view.showError("Remove "+picture.getDateTime());
    }

    @Override
    public void onListItemSourceClick(PictureClass picture) {
		String path = picture.getPath();
		photoFile = new File(path);
        Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
        view.updatePhotoView(bitmap);
        //view.showError("Source "+picture.getDateTime());
    }

    @Override
    public void onLoadFromCameraClick() {
        photoFile=FileUtils.getPhotoFile(context, FileUtils.getFileName());
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFile.getPath());
            String str123 = exif.getAttribute(ExifInterface.TAG_DATETIME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(context, FileUtils.STR_FILE_PROVIDER, photoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = context
                .getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity : cameraActivities){
            context.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        view.startActivityToResults(captureImage, REQUEST_PHOTO);
    }

    @Override
    public void onLoadFromGalleryClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        view.startActivityToResults(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
    }

    @Override
    public void onActivityResultFinish(int requestCode, Intent data) {
        if(requestCode==REQUEST_PHOTO){

            try {
                ExifInterface exif =  new ExifInterface(photoFile.getPath());
                String str123 = exif.getAttribute(ExifInterface.TAG_DATETIME);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = FileProvider.getUriForFile(context, FileUtils.STR_FILE_PROVIDER, photoFile);
            context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            try {
                if(photoFile!=null) {
                    ExifInterface exif = new ExifInterface(photoFile.getAbsolutePath());
                    String str1 = exif.getAttribute(ExifInterface.TAG_COMPONENTS_CONFIGURATION);
                    int i = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
            photoFile=FileUtils.getPhotoFile(context, FileUtils.getFileName());

            try {
                ExifInterface exif =  new ExifInterface(photoFile.getPath());
                String str123 = exif.getAttribute(ExifInterface.TAG_DATETIME);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==REQUEST_GALLERY){

            //Uri uri = data.getData();
			/*Uri uri = FileProvider.getUriForFile(this, "com.example.imageredactorcft.fileprovider", photoFile);
			this.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*/
            Uri imageUri = data.getData();

			/*final InputStream imageStream;
			try {
				imageStream = getContentResolver().openInputStream(imageUri);
				final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
				imvRedactor.setImageBitmap(selectedImage);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/

            String imPath = FileUtils.getRealPathFromURI_API19(context, imageUri);
            photoFile = new File(imPath);

            InputStream in;
            try {
                if(photoFile!=null) {
                    //in = context.getContentResolver().openInputStream(imageUri);
                    ExifInterface exif=null;
                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    exif = new ExifInterface(imPath);
                    //}
                    //ExifInterface exif = new ExifInterface();
                    String str1 = exif.getAttribute(ExifInterface.TAG_COMPONENTS_CONFIGURATION);
                    int i = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
        }
    }
/*
    @Override
    public void isBitmapNullError() {
        view.showError(context.getString(R.string.bitmap_null_error));
    }*/

    @Override
    public void onDestroy() {
        view=null;
    }

    @Override
    public void onInitViews(Bundle savedState) {
        view.updateRvData(db.getData());
        if(savedState!=null)
        {

            String path = savedState.getString(FILE_NAME_STATE_KEY);
            if(path!=null && path!=""){
                photoFile = new File(path);
                Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
                view.updatePhotoView(bitmap);
            }
        }
    }

    @Override
    public Bundle onSaveState(Bundle bundle) {
        if(photoFile!=null)
            bundle.putString(FILE_NAME_STATE_KEY, photoFile.getPath());
        return bundle;
    }

}
