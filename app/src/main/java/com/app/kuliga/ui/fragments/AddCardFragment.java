package com.app.kuliga.ui.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.app.kuliga.ui.fragments.viewmodels.AddCardViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

public class AddCardFragment extends Fragment {

    private AddCardViewModel mViewModel;

    public static AddCardFragment newInstance() {
        return new AddCardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_card_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddCardViewModel.class);
        View mView = getView();

        ConstraintLayout loadingProgress = mView.findViewById(R.id.card_add_progress);
        TextView textError = mView.findViewById(R.id.card_add_text_error);
        ImageView imageOk = mView.findViewById(R.id.card_add_img_ok);
        ProgressBar progressBar = mView.findViewById(R.id.card_add_bar);

        Button saveButton = mView.findViewById(R.id.card_add_button_save);
        saveButton.setEnabled(false);
        EditText editCode = mView.findViewById(R.id.card_add_edit_name);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.profileDataChanged(editCode.getText().toString());
            }
        };
        editCode.addTextChangedListener(textWatcher);

        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<AddCardViewModel.EditFormState>() {
            @Override
            public void onChanged(@Nullable AddCardViewModel.EditFormState state) {
                if (state == null) {
                    return;
                }
                if (state.isDataValid()) {
                    saveButton.setEnabled(true);
                }
                if (state.getNameError() != null) {
                    editCode.setError(getString(state.getNameError()));
                }
            }
        });
        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<AddCardViewModel.AddCardResult>() {
            @Override
            public void onChanged(@Nullable AddCardViewModel.AddCardResult result) {
                textError.setVisibility(View.INVISIBLE);
                saveButton.setEnabled(true);
                if (result == null) {
                    loadingProgress.setVisibility(View.GONE);
                    return;
                }
                if (result.getError() != null) {
                    textError.setText(result.getError());
                    textError.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);
                }
                if (result.getSuccess() != null) {
                    progressBar.setVisibility(View.GONE);
                    imageOk.setVisibility(View.VISIBLE);
                    MainViewModel.updateCards();
                    new CountDownTimer( 2000, 1000) {
                        public void onTick(long millisUntilFinished) { }
                        public void onFinish() {
                            loadingProgress.setVisibility(View.GONE);
                        }
                    }.start();
                    requireActivity().onBackPressed();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton.setEnabled(false);
                loadingProgress.setVisibility(View.VISIBLE);
                mViewModel.addCard(editCode.getText().toString());
            }
        });
    }

}