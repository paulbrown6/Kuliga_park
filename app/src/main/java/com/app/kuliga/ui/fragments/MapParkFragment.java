package com.app.kuliga.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.kuliga.R;
import com.app.kuliga.ui.fragments.viewmodels.MapParkViewModel;

public class MapParkFragment extends Fragment {

    private MapParkViewModel mViewModel;

    public static MapParkFragment newInstance() {
        return new MapParkFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_park_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapParkViewModel.class);
        View mView = getView();
    }

}