package com.app.kuliga.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.ui.adapters.AdapterCardOperation;
import com.app.kuliga.ui.fragments.viewmodels.CardsHistoryViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

import java.util.ArrayList;

public class CardsHistoryFragment extends Fragment {

    private CardsHistoryViewModel mViewModel;

    public static CardsHistoryFragment newInstance() {
        return new CardsHistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cards_history_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CardsHistoryViewModel.class);
        View mView = getView();

        TextView textNoCards = mView.findViewById(R.id.card_hr_text_no_cards);
        ScrollView cardView = mView.findViewById(R.id.card_hr_view);
        RecyclerView listCards = mView.findViewById(R.id.card_hr_card_list);
        TextView cardCode = mView.findViewById(R.id.card_hr_card_code);
        TextView cardBalance = mView.findViewById(R.id.card_hr_card_balance);

        if (MainViewModel.getCards() != null && MainViewModel.getCards().isEmpty()){
            textNoCards.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        } else {
            AdapterCardOperation adapter = new AdapterCardOperation(
                    (ArrayList<Card>) MainViewModel.getCards(), this, false);
            listCards.setLayoutManager(new LinearLayoutManager(getActivity()));
            listCards.setAdapter(adapter);
            Card card = MainViewModel.getCards().get(0);
            cardCode.setText(card.getCode());
            cardBalance.setText(card.getBalance());
        }
    }

}