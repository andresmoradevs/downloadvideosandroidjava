/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.bajarvideosamicelulargratis.model;

import androidx.fragment.app.Fragment;

import com.getsoftwareCO.bajarvideosamicelulargratis.activity.MainActivity;
import com.getsoftwareCO.bajarvideosamicelulargratis.VDApp;

public class VDFragment extends Fragment {

    public MainActivity getVDActivity() {
        return (MainActivity) getActivity();
    }

    public VDApp getVDApp() {
        return (VDApp) getActivity().getApplication();
    }
}
