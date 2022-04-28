package com.app.kuliga.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Stock;

import java.util.ArrayList;

public class AdapterStock extends RecyclerView.Adapter<AdapterStock.ViewHolder> {

        private ArrayList<Stock> contents;
        private Fragment fragment;

        public AdapterStock(ArrayList<Stock> contents, Fragment fragment) {
            this.contents = contents;
            this.fragment = fragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock_fulljava, parent, false);
            return new ViewHolder(v);
        }

        @SuppressLint({"SimpleDateFormat", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.image.setImageBitmap(convertBase64ToImage(contents.get(position).getBase64()));
            holder.title.setText(contents.get(position).getTitle());
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contents.get(position).getUrl() != null) {
                        Uri uri = Uri.parse(contents.get(position).getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        fragment.requireActivity().startActivity(intent);
                    }
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
        public void updateItems(ArrayList<Stock> services){
            contents.clear();
            contents.addAll(services);
            notifyDataSetChanged();
        }

        public Bitmap convertBase64ToImage(String decode){
            byte[] decodedString = Base64.decode(decode, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            CardView card;
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.ad_stock_image);
                title = itemView.findViewById(R.id.ad_stock_text);
                card = itemView.findViewById(R.id.ad_stock_view);
            }
        }
}
