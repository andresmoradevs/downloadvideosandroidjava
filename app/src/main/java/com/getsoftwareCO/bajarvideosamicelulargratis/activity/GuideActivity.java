/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.bajarvideosamicelulargratis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getsoftwareCO.bajarvideosamicelulargratis.R;
import com.getsoftwareCO.bajarvideosamicelulargratis.adapter.SavedAdapter;
import com.getsoftwareCO.bajarvideosamicelulargratis.download.frag.DownloadsCompleted;
import com.getsoftwareCO.bajarvideosamicelulargratis.download.list.CompletedVideos;
import com.getsoftwareCO.bajarvideosamicelulargratis.model.GuideModel;
import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.getsoftwareCO.bajarvideosamicelulargratis.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuideActivity extends AppCompatActivity {


    private static final int MULTIPLE_PERMISSIONS_CODE = 1;

    private ViewPager2 pager;
    private AdvancedCardView back, next;

    View view;
    RecyclerView downloadsList;
    List<String> videos;
    CompletedVideos completedVideos;

    DownloadsCompleted.OnNumDownloadsCompletedChangeListener onNumDownloadsCompletedChangeListener;
    SavedAdapter adapter;
    LinearLayout empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        back = findViewById(R.id.info_back);
        next = findViewById(R.id.info_next);
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

                    break;
                default:
                    infoText.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);

            }
        }
    };


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

    private boolean checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE
        };

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MULTIPLE_PERMISSIONS_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            // Inicializa los permisos con el resultado (asume que todos están denegados)
            perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.VIBRATE, PackageManager.PERMISSION_GRANTED);

            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }

            boolean allPermissionsGranted = true;
            for (String permission : perms.keySet()) {
                if (perms.get(permission) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Todos los permisos concedidos, realiza la descarga del archivo
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            } else {
                // Alguno(s) permiso(s) denegado(s)
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

}