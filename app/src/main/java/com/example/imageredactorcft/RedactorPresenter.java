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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RedactorPresenter implements RedactorContractor.Presenter {
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_EXIF = 3;
    public static final String FILE_NAME_STATE_KEY="file_name_state_key";
    public static final String LOADER_START_KEY="loader_start_key";

    RedactorContractor.View view;
    DBClass db;
    Activity activity;
    Context context;
    File photoFile = null;
    boolean loaderIsStart=false;

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

    private void saveNewImg(Bitmap bitmap){
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
            FileUtils.setExif(newPath, exif, strDateTime, newDeviceName);
            PictureClass newPicture = new PictureClass(newPath, newDateTime);
            db.savePicture(newPicture);
        }
        catch (Exception ex){
            view.showError(ex.getMessage());
        }
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
    }

    @Override
    public void onListItemSourceClick(PictureClass picture) {
		String path = picture.getPath();
		photoFile = new File(path);
        Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
        view.updatePhotoView(bitmap);
    }

    @Override
    public void onLoadFromCameraClick() {
        photoFile=FileUtils.getPhotoFile(context, FileUtils.getFileName());
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
        view.startActivityToResults(Intent.createChooser(intent, context.getString(R.string.intent_load_gallery)), REQUEST_GALLERY);
    }

    @Override
    public void onDlgDownloadYesClick(String url) {
        if(!url.equals("") && url.length()>0) {
            String path = FileUtils.getPhotoFile(context, FileUtils.getFileName()).getPath();
            Bundle bundle = new Bundle();
            bundle.putString(UrlFileLoader.KEY_DOWNLOAD_URL, url);
            bundle.putString(UrlFileLoader.KEY_DOWNLOAD_PATH, path);
            view.initDownloadLoader(bundle);
            view.updateProgress(0);
            view.showProgressBar();
            loaderIsStart=true;
        }
        else
            view.showError(context.getString(R.string.error_no_url));
    }

    @Override
    public void onLoadFromUrlClick() {
        view.showUrlDialog();
    }

    @Override
    public void onLoadFinished(String data) {
        if(data.equals("")&&data.length()==0) {
            photoFile = FileUtils.getPhotoFile(context, FileUtils.getFileName());
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
            view.hideProgressBar();
        }
        else{
            view.hideProgressBar();
            view.showError(context.getString(R.string.error_download_url));
        }
        loaderIsStart=false;
    }

    @Override
    public void onActivityResultFinish(int requestCode, Intent data) {
        if(requestCode==REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(context, FileUtils.STR_FILE_PROVIDER, photoFile);
            context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
            photoFile=FileUtils.getPhotoFile(context, FileUtils.getFileName());
        }
        if(requestCode==REQUEST_GALLERY){
            Uri imageUri = data.getData();
            String imPath = FileUtils.getRealPathFromURI_API19(context, imageUri);
            photoFile = new File(imPath);
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        view=null;
    }

    @Override
    public void onMessageEvent(int progress) {
        view.updateProgress(progress);
    }

    @Override
    public void onInitViews(Bundle savedState) {
        view.updateRvData(db.getData());
        if(savedState!=null)
        {
            String path = savedState.getString(FILE_NAME_STATE_KEY);
            loaderIsStart  = savedState.getBoolean(LOADER_START_KEY,false);
            if(path!=null && path!=""){
                photoFile = new File(path);
                Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
                view.updatePhotoView(bitmap);
            }
            if(loaderIsStart)
            {
                view.restartDownloadLoader();
                view.showProgressBar();
            }
        }
    }

    @Override
    public Bundle onSaveState(Bundle bundle) {
        bundle.putBoolean(LOADER_START_KEY, loaderIsStart);
        if(photoFile!=null)
            bundle.putString(FILE_NAME_STATE_KEY, photoFile.getPath());
        return bundle;
    }

}
