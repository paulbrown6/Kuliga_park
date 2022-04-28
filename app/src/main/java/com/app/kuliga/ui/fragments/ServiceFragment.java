package com.app.kuliga.ui.fragments;

import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Service;
import com.app.kuliga.ui.fragments.viewmodels.ServicesViewModel;

public class ServiceFragment extends Fragment {

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_fragment, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View mView = getView();
        Service service = null;
        if (getArguments() != null && getArguments().getString("service") != null
            && !ServicesViewModel.getServices().isEmpty()){
            for (Service serv: ServicesViewModel.getServices()){
                if (serv.getServiceId().equals(getArguments().getString("service"))){
                    service = serv;
                    break;
                }
            }
        }
        if (service == null) {
            requireActivity().onBackPressed();
            return;
        }

        CardView info = mView.findViewById(R.id.service_info_card);
        TextView name = mView.findViewById(R.id.service_name);
        TextView nameR = mView.findViewById(R.id.service_nameR);
        TextView work = mView.findViewById(R.id.service_text_work);
        TextView time = mView.findViewById(R.id.service_text_time);
        TextView price = mView.findViewById(R.id.service_text_price);

        if (service.getServiceId() == null || service.getName() == null){
            info.setVisibility(View.GONE);
        } else {
            name.setText(service.getCategoryName());
            nameR.setText(service.getName());
            work.setText(service.getTimeWork());
            time.setText(service.getTariffTime());
            price.setText(service.getPrice().split("\\.")[0] + " руб.");
        }
    }

}