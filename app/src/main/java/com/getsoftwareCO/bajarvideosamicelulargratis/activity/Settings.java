/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.bajarvideosamicelulargratis.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.getsoftwareCO.bajarvideosamicelulargratis.R;
import com.getsoftwareCO.bajarvideosamicelulargratis.model.VDFragment;

public class Settings extends VDFragment implements MainActivity.OnBackPressedListener, View.OnClickListener {
    private View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);

        if (view == null) {
            view = inflater.inflate(R.layout.settings, container, false);

            getVDActivity().setOnBackPressedListener(this);
            final SharedPreferences prefs = getActivity().getSharedPreferences("settings", 0);

            //Back
            ImageView btnSettingsBack = view.findViewById(R.id.btn_settings_back);
            btnSettingsBack.setOnClickListener(this);

            view.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getVDActivity(), HistoryActivity.class);
                    startActivity(intent);
                }
            });

        }
        return view;
    }

    @Override
    public void onBackpressed() {
        getVDActivity().getBrowserManager().unhideCurrentWindow();
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_settings_back:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }

    private void privacyPolicyClicked(){
        Toast.makeText(getContext(), "Nothing yet", Toast.LENGTH_SHORT).show();
    }

    public void shareApp()
    {
        StringBuilder msg = new StringBuilder();
        msg.append(getString(R.string.share_msg));
        msg.append("\n");
        msg.append("https://play.google.com/store/apps/details?id=com.bajarvideosamicelulargratis");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getActivity().getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        else
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        intent.addFlags(flags);
        return intent;
    }
}
