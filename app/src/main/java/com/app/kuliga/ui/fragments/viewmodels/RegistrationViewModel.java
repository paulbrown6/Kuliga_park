package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Validation;

public class RegistrationViewModel extends ViewModel {

    private MutableLiveData<RegFormState> regFormState = new MutableLiveData<>();
    private MutableLiveData<RegResult> regResult = new MutableLiveData<>();
    private RegFormState regForm = new RegFormState(false);

    public LiveData<RegFormState> getState() {
        return regFormState;
    }

    public LiveData<RegResult> getResult() {
        return regResult;
    }

    public void registration(String regCode, String email, String password) {
        ApiCall.getInstance().signUp(regCode, email, password, regResult);
    }

    public void regDataChanged(String login, String password, String passwordR,
                               String code, String number, boolean agree) {
        if (!Validation.isEmailValid(login)) {
            regForm.setLoginError(R.string.invalid_email);
        } else {
            regForm.setLoginError(null);
        }
        if (!Validation.isPasswordValid(password)) {
            regForm.setPasswordError(R.string.invalid_password);
        } else {
            regForm.setPasswordError(null);
        }
        if (!Validation.isPasswordRValid(password, passwordR)) {
            regForm.setPasswordRError(R.string.invalid_passwordR);
        } else {
            regForm.setPasswordRError(null);
        }
        if (!Validation.isCodeValid(code)) {
            regForm.setCodeError(R.string.invalid_code);
        } else {
            regForm.setCodeError(null);
        }
        if (!Validation.isNumberValid(number)) {
            regForm.setNumberError(R.string.invalid_number);
        } else {
            regForm.setNumberError(null);
        }
        if (!agree) {
            regForm.setDataValid(false);
        }
        if (Validation.isEmailValid(login) && Validation.isPasswordValid(password) &&
                Validation.isPasswordRValid(password, passwordR) && Validation.isCodeValid(code) &&
                Validation.isNumberValid(number) && agree){
            regForm = new RegFormState(true);
        }
        regFormState.setValue(regForm);
    }

    public static class RegFormState {
        @Nullable
        private Integer loginError;
        @Nullable
        private Integer passwordError;
        @Nullable
        private Integer passwordRError;
        @Nullable
        private Integer codeError;
        @Nullable
        private Integer numberError;
        private boolean isDataValid;

        RegFormState(boolean isDataValid) {
            this.loginError = null;
            this.passwordError = null;
            this.passwordRError = null;
            this.codeError = null;
            this.numberError = null;
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

        @Nullable
        public Integer getPasswordRError() {
            return passwordRError;
        }

        @Nullable
        public Integer getCodeError() {
            return codeError;
        }

        @Nullable
        public Integer getNumberError() {
            return numberError;
        }

        public void setLoginError(@Nullable Integer loginError) {
            this.loginError = loginError;
            isDataValid = false;
        }

        public void setPasswordError(@Nullable Integer passwordError) {
            this.passwordError = passwordError;
            isDataValid = false;
        }

        public void setPasswordRError(@Nullable Integer passwordRError) {
            this.passwordRError = passwordRError;
            isDataValid = false;
        }

        public void setCodeError(@Nullable Integer codeError) {
            this.codeError = codeError;
            isDataValid = false;
        }

        public void setNumberError(@Nullable Integer numberError) {
            this.numberError = numberError;
            isDataValid = false;
        }

        public void setDataValid(boolean dataValid) {
            isDataValid = dataValid;
        }

        public boolean isDataValid() {
            return isDataValid;
        }
    }

    public static class RegResult {
        @Nullable
        private Boolean success;
        @Nullable
        private String error;

        public RegResult(@Nullable String error) {
            this.error = error;
        }

        public RegResult(@Nullable Boolean success) {
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