package com.app.kuliga.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {

        private ArrayList<History> contents;
        private Fragment fragment;

        public AdapterHistory(ArrayList<History> contents, Fragment fragment) {
            this.contents = contents;
            this.fragment = fragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history_fulljava, parent, false);
            return new ViewHolder(v);
        }

        @SuppressLint({"SimpleDateFormat", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.sum.setText(contents.get(position).getAmount());
            holder.title.setText(contents.get(position).getComment());
            holder.time.setText(contents.get(position).getTime());
            holder.date.setText(new SimpleDateFormat("dd.MM.yyyy").
                    format(contents.get(position).getDate()));
            if (contents.get(position).getComment() != null &&
                    contents.get(position).getComment().contains("полнение")){
                holder.image.setImageDrawable(fragment.getActivity().getDrawable(R.drawable.ic_card_plus));
            } else {
                holder.image.setImageDrawable(fragment.getActivity().getDrawable(R.drawable.ic_card_minus));
            }
            if (position == contents.size() - 1) {
                holder.line.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (contents == null)
                return 0;
            return contents.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void updateItems(ArrayList<History> services){
            contents.clear();
            contents.addAll(services);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView sum;
            TextView title;
            TextView time;
            TextView date;
            ImageView line;

            public ViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.ad_hr_image);
                sum = itemView.findViewById(R.id.ad_hr_sum);
                title = itemView.findViewById(R.id.ad_hr_title);
                time = itemView.findViewById(R.id.ad_hr_time);
                date = itemView.findViewById(R.id.ad_hr_date);
                line = itemView.findViewById(R.id.ad_hr_line);
            }
        }
}
