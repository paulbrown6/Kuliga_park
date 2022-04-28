package com.app.kuliga.ui.fragments;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Service;
import com.app.kuliga.ui.adapters.AdapterService;
import com.app.kuliga.ui.fragments.viewmodels.ServicesViewModel;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    private ServicesViewModel mViewModel;
//    private String[] nameServicesSummer = {};

    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.services_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServicesViewModel.class);
        View mView = getView();

        TextView item1 = mView.findViewById(R.id.item1);
        TextView item2 = mView.findViewById(R.id.item2);
        FrameLayout servicesTabs = mView.findViewById(R.id.services_tabs);
        RecyclerView layoutBlue = mView.findViewById(R.id.services_list_winter);
        RecyclerView layoutGreen = mView.findViewById(R.id.services_list_summer);
        TextView select = mView.findViewById(R.id.select);
        ColorStateList def = item2.getTextColors();
        View.OnClickListener clickTabs = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.item1) {
                    select.animate().x(0f).setDuration(100);
                    item1.setTextColor(
                            AppCompatResources.getColorStateList(
                                    requireContext(),
                                    R.color.white)
                    );
                    select.setBackgroundResource(R.drawable.frame_layout_button_bagraund);
                    servicesTabs.setBackgroundResource(R.drawable.frame_latout_bacgraund);
                    item2.setTextColor(def);
                    layoutBlue.setVisibility(View.GONE);
                    layoutGreen.setVisibility(View.VISIBLE);
                } else if (view.getId() == R.id.item2) {
                    item1.setTextColor(def);
                    select.setBackgroundResource(R.drawable.frame_layout_button_bacgraund_2);
                    item2.setTextColor(
                            AppCompatResources.getColorStateList(
                                    requireContext(),
                                    R.color.white)
                    );
                    layoutBlue.setVisibility(View.VISIBLE);
                    layoutGreen.setVisibility(View.GONE);
                    servicesTabs.setBackgroundResource(R.drawable.frame_layout_baacgraund_2);
                    double size = item2.getWidth() / 1.030;
                    select.animate().x((float) size).setDuration(100);
                }
            }
        };
        item1.setOnClickListener(clickTabs);
        item2.setOnClickListener(clickTabs);

        ArrayList<Service> services = new ArrayList<>();
        if (mViewModel.getServices() != null && !mViewModel.getServices().isEmpty()){
            services = (ArrayList<Service>) mViewModel.getServices();
        }

        AdapterService adapterSummer = new AdapterService(services, this, AdapterService.ADAPTER_GREEN);
        AdapterService adapterWinter = new AdapterService(services, this, AdapterService.ADAPTER_BLUE);

        layoutGreen.setLayoutManager(new LinearLayoutManager(getActivity()));
        layoutGreen.setAdapter(adapterSummer);
        layoutBlue.setLayoutManager(new LinearLayoutManager(getActivity()));
        layoutBlue.setAdapter(adapterWinter);
    }

    private ArrayList<Service> getSummerServices(ArrayList<Service> services){
        ArrayList<Service> sumServices = new ArrayList<>();

        return sumServices;
    }

}