package com.example.imageredactorcft;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;



public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(FlowConfig.builder(this)
                .addDatabaseConfig(DatabaseConfig.builder(AppDataBase.class)
                        .databaseName("AppDatabase")
                        .build())
                .build());
    }
}
