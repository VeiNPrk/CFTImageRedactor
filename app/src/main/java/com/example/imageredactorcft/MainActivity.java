package com.example.imageredactorcft;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RedactorContractor.View, ResultRecyclerAdapter.ResultPopMenuClickListener,
		ChoseLoadDialog.ChoseLoadDialogListener{


	public static final int REQUEST_PERMISSION=111;
	public final String TAG = getClass().getSimpleName();
    Button btnRotate;
    Button btnGray;
    Button btnMirror;
    ImageView imvRedactor;
    RecyclerView rvResults;
    Context context;
    //File photoFile;
	List<PictureClass> results;
	ResultRecyclerAdapter adapter;
	RedactorContractor.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_main);
        presenter = new RedactorPresenter(this, getApplicationContext());
        initViews();
		results = new ArrayList<PictureClass>();
		setRecyclerView();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					REQUEST_PERMISSION);
			//dialog.dismiss();
			return;
		}
		presenter.onInitViews(savedInstanceState);
	}

	private void initViews(){
		imvRedactor = findViewById(R.id.imv_red);
		rvResults = findViewById(R.id.rv_results);
		btnGray = findViewById(R.id.btn_to_gray);
		btnRotate = findViewById(R.id.btn_rotate);
		btnMirror = findViewById(R.id.btn_mirror);
		imvRedactor.setImageDrawable(getApplicationContext().getResources().getDrawable(android.R.drawable.ic_menu_add));
		imvRedactor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.onPickImageClick();
			}
		});

		btnRotate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.onRotateImageClick();
			}
		});
		btnGray.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.onGrayImageClick();
			}
		});
		btnMirror.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.onMirrorImageClick();
			}
		});
	}

	private void setRecyclerView() {
		LinearLayoutManager layoutManager =
				new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		adapter = new ResultRecyclerAdapter(this, this, results);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode!= Activity.RESULT_OK){
			return;
		}
		presenter.onActivityResultFinish(requestCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
    	outState=presenter.onSaveState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onGalleryClicked() {
		presenter.onLoadFromGalleryClick();
	}

	@Override
	public void onCameraClicked() {
		presenter.onLoadFromCameraClick();
	}

	@Override
	public void updatePhotoView(Bitmap bitmap) {
		if(bitmap != null)
			imvRedactor.setImageBitmap(bitmap);
		else {
			imvRedactor.setImageDrawable(getApplicationContext().getResources().getDrawable(android.R.drawable.ic_menu_add));
			//presenter.isBitmapNullError();
		}
	}

	@Override
	public void startActivityToResults(Intent intent, int requestCode) {
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void updateRvData(List<PictureClass> data) {
		results = data;
		adapter.setData(results);
	}

	@Override
	public void showError(String error) {
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}

	@Override
	public void viewDialog() {
		ChoseLoadDialog dialog = ChoseLoadDialog.newInstance();
		dialog.show(getSupportFragmentManager(), ChoseLoadDialog.TAG);
	}

	@Override
	public void onSourceClick(PictureClass picture) {
		presenter.onListItemSourceClick(picture);
	}

	@Override
	public void onDeleteClick(PictureClass picture) {
		presenter.onListItemRemoveClick(picture);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}
}
