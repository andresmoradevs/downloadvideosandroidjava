/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.bajarvideosamicelulargratis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getsoftwareCO.bajarvideosamicelulargratis.R;
import com.getsoftwareCO.bajarvideosamicelulargratis.VDApp;
import com.getsoftwareCO.bajarvideosamicelulargratis.browser.BrowserManager;
import com.getsoftwareCO.bajarvideosamicelulargratis.download.frag.Downloads;
import com.getsoftwareCO.bajarvideosamicelulargratis.download.frag.DownloadsC;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SlashsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DOWNLOAD = "Downloads";
    private static final String HISTORY = "History";
    private static final String SETTING = "Settings";
    private static final String TUTORIAL = "Tutorial";
    private EditText searchTextBar;
    private ImageView btnSearchCancel;
    private BrowserManager browserManager;
    private Uri appLinkData;
    private FragmentManager manager;
    private BottomNavigationView navView;
    private TextView titleDownload;
    private ImageView btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slashs);

        titleDownload = findViewById(R.id.title_download);
        btnHome = findViewById(R.id.btn_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        appLinkData = appLinkIntent.getData();

        manager = this.getSupportFragmentManager();
        // This is for creating browser manager fragment
        if ((browserManager = (BrowserManager) this.getSupportFragmentManager().findFragmentByTag("BM")) == null)
        {
            browserManager = new BrowserManager(SlashsActivity.this);

            manager.beginTransaction()
                    .add(browserManager, "BM").commit();

        }

        // Bottom navigation
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final CardView cardApps = findViewById(R.id.card_view_apps);

    }

    private void setUPBrowserToolbarView(){

        //Toolbar home button
        ImageView toolbarHome = findViewById(R.id.btn_home);
        toolbarHome.setOnClickListener(this);

        //Settings
        ImageView settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(), SlashsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_progress:
                    historyClicked();
                    return true;
                case R.id.navigation_downloads:
                    downloadClicked();
                    return true;
                case R.id.navigation_tutorial:
                    Intent intentSlash = new Intent(getApplicationContext(),GuideActivity.class);
                    startActivity(intentSlash);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search_cancel:
                searchTextBar.getText().clear();
                break;
            case R.id.btn_home:
                titleDownload.setVisibility(View.VISIBLE);
                searchTextBar.getText().clear();
                CardView cardViewApp = findViewById(R.id.card_view_apps);
                cardViewApp.setVisibility(View.VISIBLE);
                getBrowserManager().closeAllWindow();
                break;
            case R.id.btn_settings:
                settingsClicked();
                break;

            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (manager.findFragmentByTag(DOWNLOAD) != null ||
                manager.findFragmentByTag(HISTORY) != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            browserManager.resumeCurrentWindow();
            navView.setSelectedItemId(R.id.navigation_home);
        }
        else if (manager.findFragmentByTag(SETTING) != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            browserManager.resumeCurrentWindow();
            navView.setVisibility(View.VISIBLE);
        }
        else if (VDApp.getInstance().getOnBackPressedListener() != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Nos aseguramos de salir?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SlashsActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //nada
                        }
                    })
                    .create()
                    .show();
        }
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public interface OnBackPressedListener {
        void onBackpressed();
    }

    public void setOnBackPressedListener(MainActivity.OnBackPressedListener onBackPressedListener) {
        VDApp.getInstance().setOnBackPressedListener(onBackPressedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        } else {
            // Permiso ya concedido, proceder con la lógica de descarga

        }
        if (appLinkData != null) {
            browserManager.newWindow(appLinkData.toString());
        }
    }

    private void requestStoragePermission() {

    }

    public void browserClicked() {
        browserManager.unhideCurrentWindow();
    }

    public void downloadClicked(){
        closeHistory();
        if (manager.findFragmentByTag(DOWNLOAD) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.main_content, new DownloadsC(), DOWNLOAD).commit();
        }
    }
    public void slashClicked(){
        closeDownloads();
        if (manager.findFragmentByTag(HISTORY) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.main_content, new Downloads(SlashsActivity.this), HISTORY).commit();
        }
    }

    public void historyClicked(){
        closeDownloads();
        if (manager.findFragmentByTag(HISTORY) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.main_content, new Downloads(SlashsActivity.this), HISTORY).commit();
        }
    }
    public void tutorialClicked(){
        closeDownloads();
        if (manager.findFragmentByTag(HISTORY) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.main_content, new Downloads(SlashsActivity.this), HISTORY).commit();
        }
    }

    private void settingsClicked(){
        if (manager.findFragmentByTag(SETTING) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            navView.setVisibility(View.GONE);
            manager.beginTransaction().add(R.id.main_content, new Settings(), SETTING).commit();
        }
    }
    public void SlashClicked(){
        titleDownload.setVisibility(View.VISIBLE);
        browserManager.unhideCurrentWindow();
        browserManager.resumeCurrentWindow();
        closeDownloads();
        closeHistory();

    }

    public void homeClicked(){
        titleDownload.setVisibility(View.VISIBLE);
        browserManager.unhideCurrentWindow();
        browserManager.resumeCurrentWindow();
        closeDownloads();
        closeHistory();

    }

    private void closeDownloads() {
        Fragment fragment = manager.findFragmentByTag(DOWNLOAD);
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    private void closeHistory() {
        Fragment fragment = manager.findFragmentByTag(HISTORY);
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback;

    public void setOnRequestPermissionsResultListener(ActivityCompat
                                                              .OnRequestPermissionsResultCallback
                                                              onRequestPermissionsResultCallback) {
        this.onRequestPermissionsResultCallback = onRequestPermissionsResultCallback;
    }

}
