package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;

public class NoPassViewModel extends ViewModel {

    private MutableLiveData<NoPassFormState> noPassFormState = new MutableLiveData<>();
    private MutableLiveData<NoPassResult> noPassResult = new MutableLiveData<>();
    private NoPassFormState noPassForm = new NoPassFormState(false);

    public LiveData<NoPassFormState> getState() {
        return noPassFormState;
    }

    public LiveData<NoPassResult> getResult() {
        return noPassResult;
    }

    public void passResurrection(String login) {
        ApiCall.getInstance().restorePassword(login, noPassResult);
    }

    public void loginDataChanged(String login) {
        if (!isLoginValid(login)) {
            noPassForm.setLoginError(R.string.invalid_email);
        } else {
            noPassForm = new NoPassFormState(true);
        }
        noPassFormState.setValue(noPassForm);
    }

    private boolean isLoginValid(String username) {
        return username != null && username.trim().length() > 3;
    }

    public static class NoPassFormState {
        @Nullable
        private Integer loginError;
        private boolean isDataValid;

        NoPassFormState(boolean isDataValid) {
            this.loginError = null;
            this.isDataValid = isDataValid;
        }

        @Nullable
        public Integer getLoginError() {
            return loginError;
        }

        public void setLoginError(@Nullable Integer loginError) {
            this.loginError = loginError;
            isDataValid = false;
        }

        public boolean isDataValid() {
            return isDataValid;
        }
    }

    public static class NoPassResult {
        @Nullable
        private Boolean success;
        @Nullable
        private String error;

        public NoPassResult(@Nullable String error) {
            this.error = error;
        }

        public NoPassResult(@Nullable Boolean success) {
            this.success = success;
        }

        @Nullable
        public Boolean getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}