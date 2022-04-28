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
import com.app.kuliga.data.entity.Stock;
import com.app.kuliga.ui.adapters.AdapterStock;
import com.app.kuliga.ui.fragments.viewmodels.StockViewModel;

import java.util.ArrayList;

public class StockFragment extends Fragment {

    private StockViewModel mViewModel;

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StockViewModel.class);
        View mView = getView();

        TextView textNoStocks = mView.findViewById(R.id.stocks_text_error);
        ScrollView historyView = mView.findViewById(R.id.stocks_view);
        RecyclerView listItems = mView.findViewById(R.id.stocks_list);

        textNoStocks.setVisibility(View.GONE);
        historyView.setVisibility(View.GONE);
        mViewModel.loadStocks();

        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<StockViewModel.StockResult>() {
            @Override
            public void onChanged(@Nullable StockViewModel.StockResult result) {
                if (result == null) {
                    return;
                }
                if (result.getError() != null) {
                    textNoStocks.setVisibility(View.VISIBLE);
                    historyView.setVisibility(View.GONE);
                }
                if (result.getSuccess() != null) {
                    AdapterStock adapter = new AdapterStock(
                            (ArrayList<Stock>) result.getSuccess(), StockFragment.this);
                    listItems.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listItems.setAdapter(adapter);
                    textNoStocks.setVisibility(View.GONE);
                    historyView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}