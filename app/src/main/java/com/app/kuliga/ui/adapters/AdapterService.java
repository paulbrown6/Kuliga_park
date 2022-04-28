package com.app.kuliga.ui.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Service;

import java.util.ArrayList;

public class AdapterService extends RecyclerView.Adapter<AdapterService.ViewHolder> {

        private ArrayList<Service> contents;
        private Fragment fragment;
        public static int ADAPTER_GREEN = R.layout.adapter_tarifs_green_fulljava;
        public static int ADAPTER_BLUE = R.layout.adapter_tarifs_blue_fulljava;
        private int currentLayout = ADAPTER_GREEN;

        public AdapterService(ArrayList<Service> contents, Fragment fragment, @NonNull int layout) {
            this.fragment = fragment;
            this.contents = contents;
            currentLayout = layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(currentLayout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.button.setText(contents.get(position).getCategoryName());
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("service", contents.get(position).getServiceId());
                    NavHostFragment.findNavController(fragment)
                            .navigate(R.id.action_nav_services_to_nav_service, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (contents == null)
                return 0;
            return contents.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void updateItems(ArrayList<Service> services){
            contents.clear();
            contents.addAll(services);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            AppCompatButton button;

            public ViewHolder(View itemView) {
                super(itemView);
                button = itemView.findViewById(R.id.tariff_button);
            }
        }
}
