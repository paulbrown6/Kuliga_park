package com.app.kuliga.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.kuliga.R;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.ui.fragments.viewmodels.MySkiPassViewModel;

import java.util.ArrayList;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolder> {

        private ArrayList<Card> contents;
        private MySkiPassViewModel mViewModel;
        private ArrayList<CheckBox> cards;

        public AdapterCard(ArrayList<Card> contents, MySkiPassViewModel mViewModel) {
            this.mViewModel = mViewModel;
            this.contents = contents;
            cards = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_card_fulljava, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.cardBox.setText(contents.get(position).getCode());
            holder.cardBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    for (CheckBox box: cards) {
                        if (!box.equals(compoundButton)) {
                            box.setChecked(false);
                        }
                    }
                    if (b) {
                        mViewModel.selectCard(contents.get(position));
                    } else {
                        mViewModel.unselectCard();
                    }
                }
            });
            cards.add(holder.cardBox);
            if (position == 0){
                holder.cardBox.setChecked(true);
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

        class ViewHolder extends RecyclerView.ViewHolder {

            CheckBox cardBox;
            ImageView line;

            public ViewHolder(View itemView) {
                super(itemView);
                cardBox = itemView.findViewById(R.id.ad_card_check);
                line = itemView.findViewById(R.id.ad_card_bottom_img);
            }
        }
}
