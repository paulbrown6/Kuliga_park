package com.app.kuliga.ui.fragments;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.ui.adapters.AdapterCard;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MySkiPassViewModel;

import java.util.ArrayList;
import java.util.List;

public class MySkiPassFragment extends Fragment {

    private MySkiPassViewModel mViewModel;

    public static MySkiPassFragment newInstance() {
        return new MySkiPassFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_ski_pass_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MySkiPassViewModel.class);
        View mView = getView();
        TextView cardCode = mView.findViewById(R.id.skipass_card_code);
        TextView cardBalance = mView.findViewById(R.id.skipass_card_balance);
        CardView selectCard = mView.findViewById(R.id.skipass_select_card);
        CheckBox checkCard = mView.findViewById(R.id.skipass_card_check);
        CardView selectBalance = mView.findViewById(R.id.skipass_select_balance);
        CheckBox checkBalance = mView.findViewById(R.id.skipass_balance_check);
        RecyclerView cardsList = mView.findViewById(R.id.skipass_card_list);
        LinearLayout balanceLayout = mView.findViewById(R.id.skipass_balance_layout);
        EditText textBalance = mView.findViewById(R.id.skipass_edit_balance);
        Button buttonPay = mView.findViewById(R.id.skipass_button_pay);
        ConstraintLayout progress = mView.findViewById(R.id.skipass_progress);

        selectCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCard.setChecked(!checkCard.isChecked());
            }
        });
        selectBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBalance.setChecked(!checkBalance.isChecked());
            }
        });
        checkCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkBalance.setChecked(false);
                    cardsList.setVisibility(View.VISIBLE);
                } else {
                    cardsList.setVisibility(View.GONE);
                }
            }
        });
        checkBalance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkCard.setChecked(false);
                    balanceLayout.setVisibility(View.VISIBLE);
                } else {
                    balanceLayout.setVisibility(View.GONE);
                }
            }
        });

        List<Card> cards = MainViewModel.getCards();
        if (cards != null && !cards.isEmpty()){
            cardCode.setText(cards.get(0).getCode());
            if (cards.get(0).getBalance() != null) {
                cardBalance.setText(MainViewModel.getCards().get(0).getBalance());
            }
            AdapterCard adapterCard = new AdapterCard((ArrayList<Card>) cards, mViewModel);
            cardsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            cardsList.setAdapter(adapterCard);
            checkCard.setChecked(true);
        }

        mViewModel.getResult().observe(this, new Observer<MySkiPassViewModel.CardSelected>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(MySkiPassViewModel.CardSelected result) {
                if (result == null){
                    return;
                }
                if (result.getUnselected() != null) {
                    cardCode.setText("00000000");
                    cardBalance.setText("0.0 RUB");
                }
                if (result.getSelected() != null) {
                    Card card = result.getSelected();
                    cardCode.setText(card.getCode());
                    if (card.getBalance() != null){
                        cardBalance.setText(card.getBalance());
                    } else {
                        cardBalance.setText("0.0 RUB");
                    }
                }
            }
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cardCode.getText().toString().equals("00000000") &&
                        textBalance.getText().toString().length() > 0) {
                    MainViewModel.pay(cardCode.getText().toString(), textBalance.getText().toString());
                    progress.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MySkiPassFragment.this.getContext(), "Нужно выбрать карту и ввести сумму", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}