package com.app.kuliga.ui.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Card;

import java.util.ArrayList;

public class AdapterCardOperation extends RecyclerView.Adapter<AdapterCardOperation.ViewHolder> {

        private ArrayList<Card> contents;
        private Fragment fragment;
        private boolean profile;

        public AdapterCardOperation(ArrayList<Card> contents, Fragment fragment, boolean profile) {
            this.fragment = fragment;
            this.contents = contents;
            this.profile = profile;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_card_operation_fulljava, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (!contents.get(position).getName().equals("none")) {
                holder.name.setText(contents.get(position).getName());
            } else {
                holder.name.setText(contents.get(position).getCode());
            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("card", contents.get(position).getCardId());
                    if (profile) {
                        NavHostFragment.findNavController(fragment)
                                .navigate(R.id.action_nav_cards_to_nav_cardProfile, bundle);
                    } else {
                        NavHostFragment.findNavController(fragment)
                                .navigate(R.id.action_nav_history_to_nav_cardHis, bundle);
                    }
                }
            });
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

        class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout layout;
            TextView name;
            ImageView line;

            public ViewHolder(View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.ad_card_op_layout);
                name = itemView.findViewById(R.id.ad_card_op_name);
                line = itemView.findViewById(R.id.ad_card_op_bottom_img);
            }
        }
}
