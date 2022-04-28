package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.User;
import com.app.kuliga.data.entity.Validation;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginFormState loginForm = new LoginFormState(false);

    public LiveData<LoginFormState> getState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        ApiCall.getInstance().signIn(email, password, loginResult);
    }

    public void loginDataChanged(String login, String password) {
        if (!Validation.isEmailValid(login)) {
            loginForm.setLoginError(R.string.invalid_email);
        } else {
            loginForm.setLoginError(null);
        }
        if (!Validation.isPasswordValid(password)) {
            loginForm.setPasswordError(R.string.invalid_password);
        } else {
            loginForm.setPasswordError(null);
        }
        if (Validation.isEmailValid(login) && Validation.isPasswordValid(password)){
            loginForm = new LoginFormState(true);
        }
        loginFormState.setValue(loginForm);
    }

    public static class LoginFormState {
        @Nullable
        private Integer loginError;
        @Nullable
        private Integer passwordError;
        private boolean isDataValid;

        LoginFormState(boolean isDataValid) {
            this.loginError = null;
            this.passwordError = null;
            this.isDataValid = isDataValid;
        }

        @Nullable
        public Integer getLoginError() {
            return loginError;
        }

        @Nullable
        public Integer getPasswordError() {
            return passwordError;
        }

        public void setLoginError(@Nullable Integer loginError) {
            this.loginError = loginError;
            isDataValid = false;
        }

        public void setPasswordError(@Nullable Integer passwordError) {
            this.passwordError = passwordError;
            isDataValid = false;
        }

        public boolean isDataValid() {
            return isDataValid;
        }
    }

    public static class LoginResult {
        @Nullable
        private User success;
        @Nullable
        private String error;

        public LoginResult(@Nullable String error) {
            this.error = error;
        }

        public LoginResult(@Nullable User success) {
            this.success = success;
        }

        @Nullable
        public User getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}