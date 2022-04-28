package com.app.kuliga.ui.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.ui.fragments.viewmodels.CardProfileViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

import java.text.SimpleDateFormat;

public class CardProfileFragment extends Fragment {

    private CardProfileViewModel mViewModel;
    private TextView name;
    private EditText editName;

    public static CardProfileFragment newInstance() {
        return new CardProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_profile_fragment_fulljava, container, false);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CardProfileViewModel.class);
        View mView = getView();

        ConstraintLayout loadingProgress = mView.findViewById(R.id.card_pr_progress);
        TextView textError = mView.findViewById(R.id.card_pr_text_error);
        ImageView imageOk = mView.findViewById(R.id.card_pr_img_ok);
        ProgressBar progressBar = mView.findViewById(R.id.card_pr_bar);

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


        Button saveButton = mView.findViewById(R.id.card_pr_button_save);
        saveButton.setEnabled(false);
        Button cancelButton = mView.findViewById(R.id.card_pr_button_cancel);
        name = mView.findViewById(R.id.card_pr_name);
        editName = mView.findViewById(R.id.card_pr_edit_name);
        TextView cardId = mView.findViewById(R.id.card_pr_cardId);
        TextView category = mView.findViewById(R.id.card_pr_category);
        TextView type = mView.findViewById(R.id.card_pr_type);
        TextView tariff = mView.findViewById(R.id.card_pr_tariff);
        TextView validFrom = mView.findViewById(R.id.card_pr_valid_from);
        TextView validTo = mView.findViewById(R.id.card_pr_valid_to);
        TextView balance = mView.findViewById(R.id.card_pr_balance);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.profileDataChanged(editName.getText().toString());
            }
        };
        editName.addTextChangedListener(textWatcher);

        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<CardProfileViewModel.CardFormState>() {
            @Override
            public void onChanged(@Nullable CardProfileViewModel.CardFormState state) {
                if (state == null) {
                    return;
                }
                if (state.isDataValid()) {
                    saveButton.setEnabled(true);
                }
                if (state.getNameError() != null) {
                    editName.setError(getString(state.getNameError()));
                }
            }
        });
        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<CardProfileViewModel.CardResult>() {
            @Override
            public void onChanged(@Nullable CardProfileViewModel.CardResult regResult) {
                textError.setVisibility(View.INVISIBLE);
                saveButton.setEnabled(true);
                if (regResult == null) {
                    loadingProgress.setVisibility(View.GONE);
                    return;
                }
                if (regResult.getError() != null) {
                    textError.setText(regResult.getError());
                    textError.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);
                }
                if (regResult.getSuccess() != null) {
                    progressBar.setVisibility(View.GONE);
                    imageOk.setVisibility(View.VISIBLE);
                    MainViewModel.saveCardToDB(regResult.getSuccess());
                    setNameCard(regResult.getSuccess());
                    new CountDownTimer( 2000, 1000) {
                        public void onTick(long millisUntilFinished) { }
                        public void onFinish() {
                            loadingProgress.setVisibility(View.GONE);
                        }
                    }.start();
                }
            }
        });

        setNameCard(card);
        cardId.setText(card.getCardId());
        category.setText(card.getCategory());
        type.setText(card.getCardType());
        tariff.setText(card.getTariffType());
        validFrom.setText(new SimpleDateFormat("dd.MM.yyyy").format(card.getValidFrom()));
        validTo.setText(new SimpleDateFormat("dd.MM.yyyy").format(card.getValidTo()));
        balance.setText(card.getBalance());


        Card finalCard = card;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton.setEnabled(false);
                loadingProgress.setVisibility(View.VISIBLE);
                mViewModel.renameCard(editName.getText().toString(), finalCard);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setText(MainViewModel.getUser().getLastName());
            }
        });

    }

    private void setNameCard(Card card){
        if (!card.getName().equals("none")) {
            editName.setText(card.getName());
            name.setText(card.getName());
        } else {
            editName.setText(card.getCode());
            name.setText(card.getCode());
        }
    }
}