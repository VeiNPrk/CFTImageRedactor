package com.example.imageredactorcft;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class UrlFileLoader extends AsyncTaskLoader<String> {

    private final String TAG = getClass().getSimpleName();
    public static final String KEY_DOWNLOAD_URL = "url_download_key";
    public static final String KEY_DOWNLOAD_PATH = "url_download_path";
    public static final int DOWNLOAD_LOADER_ID = 100;
	private String donloadUrl;
	private String downloadPath="";
	private String strError="";
	Context context;

    public UrlFileLoader(Context context, Bundle args) {
        super(context);
        this.context=context;
        if (args != null){
            donloadUrl = args.getString(KEY_DOWNLOAD_URL);
            downloadPath = args.getString(KEY_DOWNLOAD_PATH);
		}
        Log.d(TAG, "create");
    }

    public UrlFileLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int count;
        try {
            URL url = new URL(donloadUrl);
            URLConnection conection = url.openConnection();
            conection.connect();
            int lenghtOfFile = conection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            OutputStream output = new FileOutputStream(downloadPath);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                int progress = (int) ((total * 100) / lenghtOfFile);
                EventBus.getDefault().post(new MessageEvent(progress));
                Log.d(TAG, progress+"%");
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            strError="";
        } catch (Exception e) {
            strError="Error: "+e.getMessage();
            Log.e(TAG, e.getMessage());
        }
        return strError;
    }

    @Override
    public void forceLoad() {
        Log.d(TAG, "forceLoad");
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(TAG, "onStopLoading");
    }

    @Override
    public void deliverResult(String data) {
        Log.d(TAG, "deliverResult");
        super.deliverResult(data);
    }
}
