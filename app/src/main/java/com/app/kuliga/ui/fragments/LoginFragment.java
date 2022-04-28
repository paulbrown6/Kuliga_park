package com.app.kuliga.ui.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.kuliga.R;
import com.app.kuliga.ui.activitys.MainActivity;
import com.app.kuliga.ui.fragments.viewmodels.LoginViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment_fulljava, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        View mView = getView();
        ConstraintLayout loadingProgress = mView.findViewById(R.id.login_progress);
        TextView textError = mView.findViewById(R.id.login_text_error);
        Button loginButton = mView.findViewById(R.id.login_button);
        Button regButton = mView.findViewById(R.id.login_reg_button);
        Button noPassButton = mView.findViewById(R.id.login_nopass_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_registrationFragment);
            }
        });
        noPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_noPassFragment);
            }
        });
        loginButton.setEnabled(false);
        EditText login = mView.findViewById(R.id.login_input_login);
        EditText password = mView.findViewById(R.id.login_input_password);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.loginDataChanged(login.getText().toString(),
                        password.getText().toString());
            }
        };
        login.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<LoginViewModel.LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginViewModel.LoginFormState state) {
                if (state == null) {
                    return;
                }
                loginButton.setEnabled(state.isDataValid());
                if (state.getLoginError() != null) {
                    login.setError(getString(state.getLoginError()));
                }
                if (state.getPasswordError() != null) {
                    password.setError(getString(state.getPasswordError()));
                }
            }
        });

        mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<LoginViewModel.LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginViewModel.LoginResult loginResult) {
                loadingProgress.setVisibility(View.GONE);
                textError.setVisibility(View.INVISIBLE);
                loginButton.setEnabled(true);
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    textError.setText(loginResult.getError());
                    textError.setVisibility(View.VISIBLE);
                }
                if (loginResult.getSuccess() != null) {
                    MainViewModel.setUser(loginResult.getSuccess());
                    startActivity(new Intent(LoginFragment.this.getActivity(), MainActivity.class));
                    LoginFragment.this.getActivity().finish();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setEnabled(false);
                loadingProgress.setVisibility(View.VISIBLE);
                mViewModel.login(login.getText().toString(), password.getText().toString());
            }
        });
    }

}