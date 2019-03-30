package com.example.imageredactorcft;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChoseLoadDialog.ChoseLoadDialogListener{

	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_GALLERY = 2;
	public static final int REQUEST_PERMISSION=111;

    Button btnRotate;
    Button btnGray;
    Button btnMirror;
    ImageView imvRedactor;
    RecyclerView rvResults;
    Context context;
    File photoFile;
	List<PictureClass> results;
	ResultRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_main);
        imvRedactor = findViewById(R.id.imv_red);
        rvResults = findViewById(R.id.rv_results);
		btnGray = findViewById(R.id.btn_to_gray);
		btnRotate = findViewById(R.id.btn_rotate);
		btnMirror = findViewById(R.id.btn_mirror);
		imvRedactor.setImageDrawable(getApplicationContext().getResources().getDrawable(android.R.drawable.ic_menu_add));
		imvRedactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				onClickChose();//loadFromGallery();
            }
        });
        /*btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadFromCamera();
            }
        });*/
        //photoFile=FileUtils.getPhotoFile(this, null);
		//updatePhotoView();
		results = new ArrayList<PictureClass>();
		initTestData();
		setRecyclerView();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					REQUEST_PERMISSION);
			//dialog.dismiss();
			return;
		}
	}

	private void initViews(){

	}

	private void initTestData(){
    	String filePath = FileUtils.getPhotoFile(this,FileUtils.getFileName()).getPath();
    	PictureClass cl1 = new PictureClass(filePath);
    	results.add(cl1);
    	results.add(cl1);
	}

	private void setRecyclerView() {
		LinearLayoutManager layoutManager =
				new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		adapter = new ResultRecyclerAdapter(this, results);
		rvResults.setLayoutManager(layoutManager);
		rvResults.setHasFixedSize(true);
		rvResults.setAdapter(adapter);
		RecyclerView.ItemDecoration itemDecoration =
				new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		rvResults.addItemDecoration(itemDecoration);
		rvResults.setItemAnimator(new DefaultItemAnimator());
	}

	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_PERMISSION:
				if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
					//readDataExternal();
				}
				break;

			default:
				break;
		}
	}

	public void onClickChose(){
		ChoseLoadDialog dialog = ChoseLoadDialog.newInstance();
		//dialog.setTargetFragment(this,0);
		dialog.show(getSupportFragmentManager(), ChoseLoadDialog.TAG);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode!= Activity.RESULT_OK){
			return;
		}
		
		if(requestCode==REQUEST_PHOTO){
			Uri uri = FileProvider.getUriForFile(this, "com.example.imageredactorcft.fileprovider", photoFile);
			this.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			updatePhotoView();
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

			String imPath = FileUtils.getRealPathFromURI_API19(this, imageUri);
			photoFile = new File(imPath);
			updatePhotoView();
		}
	}

    private void loadFromCamera(){
		photoFile=FileUtils.getPhotoFile(this, FileUtils.getFileName());
		final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = FileProvider.getUriForFile(this, "com.example.imageredactorcft.fileprovider", photoFile);
		captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		List<ResolveInfo> cameraActivities = this
			.getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo activity : cameraActivities){
			this.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		}
		startActivityForResult(captureImage, REQUEST_PHOTO);
    }

    private void loadFromGallery(){
		Intent intent = new Intent();
		// Show only images, no videos or anything else
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		
		/*Uri uri = FileProvider.getUriForFile(this, "com.example.imageredactorcft.fileprovider", photoFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);*/
		
		// Always show the chooser (if there are multiple options available)
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
		
		/*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);*/
    }



    /*private String getFileName(){
        String name="IMG_123.jpg";
        return name;
    }

    private File getPhotoFile(){
        File filesDir = context.getFilesDir();
        return new File(filesDir,getFileName());
    }*/
	
	private void updatePhotoView(){
		if(photoFile==null || !photoFile.exists()){
			imvRedactor.setImageDrawable(null);
		}
		else{
			Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), this);
			imvRedactor.setImageBitmap(bitmap);
		}
	}
	
	private Bitmap getRotateBitmap(Bitmap inpBitmap){
		Bitmap bOutput;
		float degrees = 90;//rotation degree
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees);
		bOutput = Bitmap.createBitmap(inpBitmap, 0, 0, inpBitmap.getWidth(), inpBitmap.getHeight(), matrix, true);
		return bOutput;
	}
	
	private Bitmap getMirrorHorizonBitmap(Bitmap inpBitmap){
		Bitmap bOutput;
		Matrix matrix = new Matrix();
		matrix.preScale(-1.0f, 1.0f);
		bOutput = Bitmap.createBitmap(inpBitmap, 0, 0, inpBitmap.getWidth(), inpBitmap.getHeight(), matrix, true);
		return bOutput;
	}

	@Override
	public void onGalleryClicked() {
		loadFromGallery();
	}

	@Override
	public void onCameraClicked() {
		loadFromCamera();
	}
	
	/*private Bitmap getGrayBitmap(Bitmap inpBitmap){

        //Custom color matrix to convert to GrayScale
        /*float[] matrix = new float[]{
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
        canvas.drawBitmap(src, 0, 0, paint);

        return dest;
    }*/
}
