package com.example.imageredactorcft;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;



public class App extends Application {

    public static final String USERNAME_API="6987a48d-342e-4a69-8adc-e65b1cc0b9da";
    public static final String PASSWORD_API="MxYSIi6nQP2Y";
    public static final String MAINURL_API="https://gateway.watsonplatform.net/language-translator/api/";
	/*private static IdentApi identApi;
	private Retrofit retrofit;*/
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;

        FlowManager.init(FlowConfig.builder(this)
                .addDatabaseConfig(DatabaseConfig.builder(AppDataBase.class)
                        .databaseName("AppDatabase")
                        .build())
                .build());

        /*Gson gson = new GsonBuilder() .setLenient() .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(MAINURL_API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        identApi = retrofit.create(IdentApi.class);*/
    }

    /*public static Context getContext(){
        return mContext;
    }

    public static IdentApi getApi() {
        return identApi;
    }

    public static String getAuthToken() {
        byte[] data = new byte[0];
        try {
            data = (USERNAME_API + ":" + PASSWORD_API).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }*/
}
