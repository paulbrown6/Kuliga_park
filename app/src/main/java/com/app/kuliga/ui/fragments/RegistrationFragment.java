package com.app.kuliga.ui.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.ui.fragments.viewmodels.RegistrationViewModel;

public class RegistrationFragment extends Fragment {

    private RegistrationViewModel mViewModel;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        View mView = getView();
        ConstraintLayout loadingProgress = mView.findViewById(R.id.registration_progress);
        TextView textError = mView.findViewById(R.id.registration_text_error);
        Button regButton = mView.findViewById(R.id.registration_button);
        regButton.setEnabled(false);
        EditText login = mView.findViewById(R.id.registration_input_login);
        EditText password = mView.findViewById(R.id.registration_input_password);
        EditText passwordR = mView.findViewById(R.id.registration_input_passwordR);
        EditText code = mView.findViewById(R.id.registration_input_code);
        EditText number = mView.findViewById(R.id.registration_input_number);
        CheckBox checkAgree = mView.findViewById(R.id.registration_check_agree);
        TextView textAgree = mView.findViewById(R.id.registration_text_agree);
        ImageView imageOk = mView.findViewById(R.id.registration_img_ok);
        ProgressBar progressBar = mView.findViewById(R.id.registration_bar);
        textAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getString(R.string.text_url));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(number.getText().toString())){
                    numberCorrect(number);
                }
                mViewModel.regDataChanged(login.getText().toString(),
                        password.getText().toString(), passwordR.getText().toString(),
                        code.getText().toString(), number.getText().toString(), checkAgree.isChecked());
            }
        };
        login.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        passwordR.addTextChangedListener(textWatcher);
        number.addTextChangedListener(textWatcher);
        code.addTextChangedListener(textWatcher);
        checkAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mViewModel.regDataChanged(login.getText().toString(),
                        password.getText().toString(), passwordR.getText().toString(),
                        code.getText().toString(), number.getText().toString(), b);
            }
        });
        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<RegistrationViewModel.RegFormState>() {
            @Override
            public void onChanged(@Nullable RegistrationViewModel.RegFormState state) {
                if (state == null) {
                    return;
                }
                if (state.isDataValid() && checkAgree.isChecked()) {
                    regButton.setEnabled(true);
                } else {
                    regButton.setEnabled(false);
                }
                if (state.getLoginError() != null) {
                    login.setError(getString(state.getLoginError()));
                }
                if (state.getPasswordError() != null) {
                    password.setError(getString(state.getPasswordError()));
                }
                if (state.getPasswordRError() != null) {
                    passwordR.setError(getString(state.getPasswordRError()));
                }
                if (state.getCodeError() != null) {
                    code.setError(getString(state.getCodeError()));
                }
                if (state.getNumberError() != null) {
                    number.setError(getString(state.getNumberError()));
                }
            }
        });

        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<RegistrationViewModel.RegResult>() {
            @Override
            public void onChanged(@Nullable RegistrationViewModel.RegResult regResult) {
                textError.setVisibility(View.INVISIBLE);
                regButton.setEnabled(true);
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

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regButton.setEnabled(false);
                loadingProgress.setVisibility(View.VISIBLE);
                mViewModel.registration(code.getText().toString(), login.getText().toString(),
                        password.getText().toString());
            }
        });
    }

    private void numberCorrect(EditText number){
        String text = number.getText().toString();
        Integer textLength = text.length();
        if (!(text.endsWith("-") || text.endsWith(" "))) {
            if (textLength > 0 && textLength < 5) {
                if (!text.contentEquals("+7 (")) {
                    number.setText("+7 (");
                    number.setSelection(number.getText().length());
                }
            } else if (textLength == 8) {
                if (!text.contains(")")) {
                    number.setText(
                            new StringBuilder(text).insert(text.length() - 1, ") ").toString());
                    number.setSelection(number.getText().length());
                }
            } else if (textLength == 13) {
                number.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                number.setSelection(number.getText().length());
            } else if (textLength == 14) {
                if (!text.contains("-")) {
                    number.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    number.setSelection(number.getText().length());
                }
            } else if (textLength == 16) {
                number.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                number.setSelection(number.getText().length());
            }
        }
        if (textLength > 18) {
            number.setText(new StringBuilder(text).substring(0, 18));
            number.setSelection(number.getText().length());
        }
    }

}