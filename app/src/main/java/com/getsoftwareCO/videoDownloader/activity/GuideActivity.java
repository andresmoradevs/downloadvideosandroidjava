/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.videoDownloader.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getsoftwareCO.videoDownloader.R;
import com.getsoftwareCO.videoDownloader.adapter.SavedAdapter;
import com.getsoftwareCO.videoDownloader.download.frag.DownloadsCompleted;
import com.getsoftwareCO.videoDownloader.download.list.CompletedVideos;
import com.getsoftwareCO.videoDownloader.model.GuideModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.getsoftwareCO.videoDownloader.adapter.GuideAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class GuideActivity extends AppCompatActivity {

    private ViewPager2 pager;
    private AdvancedCardView back, next;

    View view;
    RecyclerView downloadsList;
    List<String> videos;
    CompletedVideos completedVideos;

    DownloadsCompleted.OnNumDownloadsCompletedChangeListener onNumDownloadsCompletedChangeListener;
    SavedAdapter adapter;
    LinearLayout empty;
//    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        back = findViewById(R.id.info_back);
        next = findViewById(R.id.info_next);

//        infoText = findViewById(R.id.info_text);
        ImageView close = findViewById(R.id.close);

        pager = findViewById(R.id.guide_pager);
        DotsIndicator indicator = findViewById(R.id.dots_indicator);

        GuideAdapter adapter = new GuideAdapter(this, getList());
        pager.setAdapter(adapter);

        indicator.setViewPager2(pager);
        pager.registerOnPageChangeCallback(callback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            TextView infoText = findViewById(R.id.info_text);
            ImageView btnOk = findViewById(R.id.btn_ok);
            switch (position) {
                case 0:
                    btnOk.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    infoText.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    btnOk.setVisibility(View.VISIBLE);
                    infoText.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    break;
                case 2:
                      btnOk.setVisibility(View.INVISIBLE);
                      infoText.setVisibility(View.VISIBLE);
                      next.setBackgroundResource(R.drawable.btn_ok);
                      back.setVisibility(View.INVISIBLE);


                    break;
                case 3:
                    infoText.setVisibility(View.INVISIBLE);
                    infoText.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    break;
                default:
                    infoText.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);


            }
        }
    };

    private void requestStoragePermission() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager.unregisterOnPageChangeCallback(callback);
    }

    private List<GuideModel> getList() {
        List<GuideModel> list = new ArrayList<>();
        list.add(new GuideModel("1", "Coloca la URL del video\n que desea descargar", R.drawable.info_one));
        list.add(new GuideModel("2", "Reproduce el video que\n deseas descargar", R.drawable.info_two));
        list.add(new GuideModel("3", "Haz click en el botón\n naranja de descarga", R.drawable.info_three));
        return list;
    }

}