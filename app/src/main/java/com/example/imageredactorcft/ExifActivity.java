package com.example.imageredactorcft;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ExifActivity extends AppCompatActivity implements ExifContractor.View {

	public final String TAG = getClass().getSimpleName();
    TextView tvExifResolution;
    TextView tvExifCamera;
    TextView tvExifISO;
    TextView tvExifDate;
    TextView tvExifFPower;
	
    Context context;
	ExifContractor.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_exif);
        presenter = new ExifPresenter(this, getApplicationContext());
        initViews();
		presenter.onInitViews(getIntent());
	}

	private void initViews(){
		tvExifResolution = findViewById(R.id.tv_exifresolution);
		tvExifCamera = findViewById(R.id.tv_exifcamera);
		tvExifISO = findViewById(R.id.tv_exifiso);
		tvExifDate = findViewById(R.id.tv_exifdate);
		tvExifFPower = findViewById(R.id.tv_exiffpower);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initData(){
		tvExifResolution.setText(presenter.getExifResolution());
		tvExifCamera.setText(presenter.getExifCamera());
		tvExifISO.setText(presenter.getExifISO());
		tvExifDate.setText(presenter.getExifDate());
		tvExifFPower.setText(presenter.getExifFPower());
	}
}
