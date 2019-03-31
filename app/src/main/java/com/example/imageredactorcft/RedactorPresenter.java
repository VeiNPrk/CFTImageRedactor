package com.example.imageredactorcft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;

import java.io.File;
import java.util.List;

public class RedactorPresenter implements RedactorContractor.Presenter {

    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_GALLERY = 2;

    RedactorContractor.View view;
    Activity activity;
    Context context;
    File photoFile = null;

    public RedactorPresenter(RedactorContractor.View _view, Context _context){
        view=_view;
        activity = (Activity) _view;
        context = _context;
    }

    @Override
    public void onPickImageClick() {
        view.viewDialog();
    }

    @Override
    public void onRotateImageClick() {

    }

    @Override
    public void onMirrorImageClick() {

    }

    @Override
    public void onGrayImageClick() {

    }

    @Override
    public void onListItemClick() {

    }

    @Override
    public void onListItemRemoveClick() {

    }

    @Override
    public void onListItemSourceClick() {

    }

    @Override
    public void onLoadFromCameraClick() {
        photoFile=FileUtils.getPhotoFile(context, FileUtils.getFileName());
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(context, "com.example.imageredactorcft.fileprovider", photoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = context
                .getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity : cameraActivities){
            context.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        view.startActivityToResults(captureImage, REQUEST_PHOTO);
        //startActivityForResult();
    }

    @Override
    public void onLoadFromGalleryClick() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

		/*Uri uri = FileProvider.getUriForFile(this, "com.example.imageredactorcft.fileprovider", photoFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);*/

        // Always show the chooser (if there are multiple options available)
        view.startActivityToResults(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
        //startActivityForResult();

		/*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);*/
    }

    @Override
    public void onActivityResultFinish(int requestCode, Intent data) {
        if(requestCode==REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(context, "com.example.imageredactorcft.fileprovider", photoFile);
            context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
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
            Bitmap bitmap = FileUtils.getBitmapFromFile(photoFile, activity);
            view.updatePhotoView(bitmap);
        }
    }

    @Override
    public void isBitmapNullError() {
        view.showError(context.getString(R.string.bitmap_null_error));
    }

    @Override
    public void onDestroy() {

    }
}
