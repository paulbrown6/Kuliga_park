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
import com.app.kuliga.ui.fragments.viewmodels.NoPassViewModel;

public class NoPassFragment extends Fragment {

    private NoPassViewModel mViewModel;

    public static NoPassFragment newInstance() {
        return new NoPassFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.no_pass_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoPassViewModel.class);
        View mView = getView();
        ConstraintLayout loadingProgress = mView.findViewById(R.id.nopass_progress);
        TextView textError = mView.findViewById(R.id.nopass_text_error);
        Button button = mView.findViewById(R.id.nopass_button);
        button.setEnabled(false);
        EditText login = mView.findViewById(R.id.nopass_input_login);
        ImageView imageOk = mView.findViewById(R.id.nopass_img_ok);
        ProgressBar progressBar = mView.findViewById(R.id.nopass_bar);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.loginDataChanged(login.getText().toString());
            }
        };
        login.addTextChangedListener(textWatcher);

        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<NoPassViewModel.NoPassFormState>() {
            @Override
            public void onChanged(@Nullable NoPassViewModel.NoPassFormState state) {
                if (state == null) {
                    return;
                }
                button.setEnabled(state.isDataValid());
                if (state.getLoginError() != null) {
                    login.setError(getString(state.getLoginError()));
                }
            }
        });

        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<NoPassViewModel.NoPassResult>() {
            @Override
            public void onChanged(@Nullable NoPassViewModel.NoPassResult loginResult) {
                textError.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                if (loginResult == null) {
                    loadingProgress.setVisibility(View.GONE);
                    return;
                }
                if (loginResult.getError() != null) {
                    textError.setText(loginResult.getError());
                    textError.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);
                }
                if (loginResult.getSuccess() != null) {
                    progressBar.setVisibility(View.GONE);
                    imageOk.setVisibility(View.VISIBLE);
                    new CountDownTimer( 2000, 1000) {
                        public void onTick(long millisUntilFinished) { }
                        public void onFinish() {
                            loadingProgress.setVisibility(View.GONE);
                            requireActivity().onBackPressed();
                        }
                    }.start();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                loadingProgress.setVisibility(View.VISIBLE);
                mViewModel.passResurrection(login.getText().toString());
            }
        });
    }

}