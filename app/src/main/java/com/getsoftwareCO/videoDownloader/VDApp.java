/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.videoDownloader;

import android.app.Application;
import android.content.Intent;

import com.getsoftwareCO.videoDownloader.activity.MainActivity;
import com.getsoftwareCO.videoDownloader.download.DownloadManager;
import com.google.android.gms.ads.MobileAds;

public class VDApp extends Application {
    private static VDApp instance;
    private Intent downloadService;
    private MainActivity.OnBackPressedListener onBackPressedListener;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MobileAds.initialize(this);
        downloadService = new Intent(getApplicationContext(), DownloadManager.class);
    }

    public Intent getDownloadService() {
        return downloadService;
    }

    public static VDApp getInstance() {
        return instance;
    }

    public MainActivity.OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }

    public void setOnBackPressedListener(MainActivity.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
}
