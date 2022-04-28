package com.app.kuliga.ui.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.ui.dialogs.DialogDocumentation;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.ui.dialogs.DialogMessage;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        View mView = getView();
        TextView cardCode = mView.findViewById(R.id.main_card_code);
        TextView cardBalance = mView.findViewById(R.id.main_card_balance);
        CardView mySkiPassButton = mView.findViewById(R.id.main_cardbutton_pass);
        CardView servicesButton = mView.findViewById(R.id.main_cardbutton_shop);
        CardView mapButton = mView.findViewById(R.id.main_cardbutton_map);
        CardView hostelButton = mView.findViewById(R.id.main_cardbutton_hostel);
        CardView docButton = mView.findViewById(R.id.main_cardbutton_doc);
        if (MainViewModel.getCards() != null && !MainViewModel.getCards().isEmpty()){
            cardCode.setText(MainViewModel.getCards().get(0).getCode());
            if (MainViewModel.getCards().get(0).getBalance() != null) {
                cardBalance.setText(MainViewModel.getCards().get(0).getBalance());
            }
        }
        View.OnClickListener listenerSkiPassOn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_nav_main_to_nav_mySkiPass);
                requireActivity().findViewById(R.id.nav_view_bottom).setVisibility(View.GONE);
            }
        };
        View.OnClickListener listenerSkiPassOff = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMessage dialog = new DialogMessage();
                dialog.createAlertDialog(requireActivity(),
                        "Пополнение будет доступно снова через некоторое время");
            }
        };
        MainViewModel.getLoadCardsResult().observe(this, new Observer<MainViewModel.LoadCardsResult>() {
            @Override
            public void onChanged(MainViewModel.LoadCardsResult result) {
                if (result.getSuccess() != null) {
                    if (!result.getSuccess().isEmpty()) {
                        cardCode.setText(result.getSuccess().get(0).getCode());
                        if (result.getSuccess().get(0).getBalance() != null) {
                            cardBalance.setText(result.getSuccess().get(0).getBalance());
                        }
                    }
                }
            }
        });
        MainViewModel.getLoadCardResult().observe(this, new Observer<MainViewModel.LoadCardResult>() {
            @Override
            public void onChanged(MainViewModel.LoadCardResult result) {
                if (result == null){
                    return;
                }
                if (result.getSuccess() != null) {
                    Card card = result.getSuccess();
                    if (card.getBalance() != null){
                        cardBalance.setText(card.getBalance());
                    }
                }
            }
        });
        MainViewModel.getPayResult().observe(this, new Observer<MainViewModel.OperationResult>() {
            @Override
            public void onChanged(MainViewModel.OperationResult result) {
                if (result.getSuccess() != null) {
                    mySkiPassButton.setOnClickListener(listenerSkiPassOff);
                }
            }
        });
        MainViewModel.getTimerResult().observe(this, new Observer<MainViewModel.OperationResult>() {
            @Override
            public void onChanged(MainViewModel.OperationResult result) {
                if (result.getSuccess() != null) {
                    mySkiPassButton.setOnClickListener(listenerSkiPassOn);
                }
            }
        });

        mySkiPassButton.setOnClickListener(listenerSkiPassOn);
        servicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_nav_main_to_nav_services);
                requireActivity().findViewById(R.id.nav_view_bottom).setVisibility(View.GONE);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_nav_main_to_nav_map);
                requireActivity().findViewById(R.id.nav_view_bottom).setVisibility(View.GONE);
            }
        });
        hostelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMessage dialog = new DialogMessage();
                dialog.createAlertDialog(requireActivity(),
                        "Данный раздел находится в разработке.");
            }
        });
        docButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDocumentation dialog = new DialogDocumentation();
                dialog.createAlertDialog(requireActivity());
            }
        });
        requireActivity().findViewById(R.id.nav_view_bottom).setVisibility(View.VISIBLE);
    }

}