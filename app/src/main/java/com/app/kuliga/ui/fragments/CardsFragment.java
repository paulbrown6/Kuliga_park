package com.app.kuliga.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.ui.adapters.AdapterCardOperation;
import com.app.kuliga.ui.fragments.viewmodels.CardsViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

import java.util.ArrayList;

public class CardsFragment extends Fragment {

    private CardsViewModel mViewModel;

    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cards_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CardsViewModel.class);
        View mView = getView();

        TextView textNoCards = mView.findViewById(R.id.cards_text_no_cards);
        ScrollView cardView = mView.findViewById(R.id.cards_view);
        Button button = mView.findViewById(R.id.cards_button_add);
        RecyclerView listCards = mView.findViewById(R.id.cards_card_list);

        if (MainViewModel.getCards() != null && MainViewModel.getCards().isEmpty()){
            textNoCards.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        } else {
            AdapterCardOperation adapter = new AdapterCardOperation(
                    (ArrayList<Card>) MainViewModel.getCards(), this, true);

            listCards.setLayoutManager(new LinearLayoutManager(getActivity()));
            listCards.setAdapter(adapter);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CardsFragment.this)
                            .navigate(R.id.action_nav_cards_to_nav_addCard);
            }
        });
    }

}