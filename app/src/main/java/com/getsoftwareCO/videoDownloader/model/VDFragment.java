/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.videoDownloader.model;

import androidx.fragment.app.Fragment;

import com.getsoftwareCO.videoDownloader.activity.MainActivity;
import com.getsoftwareCO.videoDownloader.VDApp;

public class VDFragment extends Fragment {

    public MainActivity getVDActivity() {
        return (MainActivity) getActivity();
    }

    public VDApp getVDApp() {
        return (VDApp) getActivity().getApplication();
    }
}
