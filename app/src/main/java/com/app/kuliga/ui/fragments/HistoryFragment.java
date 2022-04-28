package com.app.kuliga.ui.fragments;

import androidx.lifecycle.Observer;
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
import com.app.kuliga.data.entity.History;
import com.app.kuliga.ui.adapters.AdapterHistory;
import com.app.kuliga.ui.fragments.viewmodels.HistoryViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        View mView = getView();

        Card card = null;
        if (getArguments() != null && getArguments().getString("card") != null
                && !MainViewModel.getCards().isEmpty()){
            for (Card cd: MainViewModel.getCards()){
                if (cd.getCardId().equals(getArguments().getString("card"))){
                    card = cd;
                    break;
                }
            }
        }
        if (card == null) {
            requireActivity().onBackPressed();
            return;
        }

        TextView textNoCards = mView.findViewById(R.id.history_text_no_cards);
        ScrollView historyView = mView.findViewById(R.id.history_view);
        RecyclerView listItems = mView.findViewById(R.id.history_item_list);

        textNoCards.setVisibility(View.GONE);
        historyView.setVisibility(View.GONE);
        mViewModel.loadHistory(card.getCode());

        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<HistoryViewModel.HistoryResult>() {
            @Override
            public void onChanged(@Nullable HistoryViewModel.HistoryResult result) {
                if (result == null) {
                    return;
                }
                if (result.getError() != null) {
                    textNoCards.setVisibility(View.VISIBLE);
                    historyView.setVisibility(View.GONE);
                }
                if (result.getSuccess() != null) {
                    AdapterHistory adapter = new AdapterHistory(
                            (ArrayList<History>) result.getSuccess(), HistoryFragment.this);
                    listItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listItems.setAdapter(adapter);
                    textNoCards.setVisibility(View.GONE);
                    historyView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}