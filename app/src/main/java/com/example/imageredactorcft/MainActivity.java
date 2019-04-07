package com.example.imageredactorcft;

import android.Manifest;
import android.app.Activity;
//import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RedactorContractor.View, ResultRecyclerAdapter.ResultPopMenuClickListener,
		ChoseLoadDialog.ChoseLoadDialogListener, DownloadUrlDialog.DownloadDialogListener, LoaderManager.LoaderCallbacks<String> {


	public static final int REQUEST_PERMISSION=111;
	public final String TAG = getClass().getSimpleName();
    Button btnRotate;
    Button btnGray;
    Button btnMirror;
    Button btnExif;
    ImageView imvRedactor;
    RecyclerView rvResults;
    LinearLayout pbDownloadImage;
    TextView tvProgressDownload;
    Context context;
    //File photoFile;
	List<PictureClass> results;
	ResultRecyclerAdapter adapter;
	RedactorContractor.Presenter presenter;
	boolean loaderIsStart=false;
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
		//getSupportLoaderManager();
		presenter.onInitViews(savedInstanceState);
	}

	private void initViews(){
		imvRedactor = findViewById(R.id.imv_red);
		rvResults = findViewById(R.id.rv_results);
		btnGray = findViewById(R.id.btn_to_gray);
		btnRotate = findViewById(R.id.btn_rotate);
		btnMirror = findViewById(R.id.btn_mirror);
		btnExif = findViewById(R.id.btn_exif);
		tvProgressDownload = findViewById(R.id.tv_progress);
		pbDownloadImage = findViewById(R.id.pb_download_image);
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
		btnExif.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.onExifClick();
			}
		});
		//getLoaderManager();
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent event) {
		if(event!=null){
			presenter.onMessageEvent(event.progress);
		}
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
	public void onUrlClicked() {
    	presenter.onLoadFromUrlClick();
		//testurl();
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
	public void showUrlDialog() {
		DownloadUrlDialog dialog = DownloadUrlDialog.newInstance();
		dialog.show(getSupportFragmentManager(), DownloadUrlDialog.TAG);
	}

	@Override
	public void showProgressBar() {
		imvRedactor.setVisibility(View.GONE);
		pbDownloadImage.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressBar() {
		imvRedactor.setVisibility(View.VISIBLE);
		pbDownloadImage.setVisibility(View.GONE);
	}

	@Override
	public void updateProgress(int progress) {
		tvProgressDownload.setText(progress+"%");
	}

	@Override
	public void initDownloadLoader(Bundle bundle) {
		getSupportLoaderManager().initLoader(UrlFileLoader.DOWNLOAD_LOADER_ID, bundle, this);
	}

	@Override
	public void restartDownloadLoader() {
		getSupportLoaderManager().initLoader(UrlFileLoader.DOWNLOAD_LOADER_ID, null, this);
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


	@Override
	public Loader<String> onCreateLoader(int id, Bundle args) {
		loaderIsStart=true;
		Log.d(TAG, "onCreateLoader");
        return new UrlFileLoader(this, args);

	}

	@Override
	public void onLoadFinished(Loader<String> loader, String data) {
		int id = loader.getId();
		presenter.onLoadFinished(data);
		getSupportLoaderManager().destroyLoader(id);
		loaderIsStart=false;
		Log.d(TAG, "onLoadFinished");
	}

	@Override
	public void onLoaderReset(Loader<String> loader) {
		Log.d(TAG, "onLoaderReset");
	}

	@Override
	public void onYesDownloadClicked(String data) {
		//String url = "https://gear.blizzard.com/media/catalog/product/o/w/ow-mercy-gold-video_1.jpg";
		//String url = "https://www.pngkey.com/png/detail/224-2245923_mercy-overwatch-png-vector-mercy-transparent-overwatch-artwork.png";

		presenter.onDlgDownloadYesClick(data);
	}
}
