package com.example.imageredactorcft;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ExifActivity extends AppCompatActivity implements ExifContractor.View {

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

	@Override
	protected void onDestroy() {
    	presenter.onDestroy();
		super.onDestroy();
	}
}
