package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.User;
import com.app.kuliga.data.entity.Validation;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<ProfileFormState> profileFormState = new MutableLiveData<>();
    private MutableLiveData<ProfileResult> profileResult = new MutableLiveData<>();
    private ProfileFormState profileForm = new ProfileFormState(false);

    public LiveData<ProfileFormState> getState() {
        return profileFormState;
    }

    public LiveData<ProfileResult> getResult() {
        return profileResult;
    }

    public void setProfile(String lastName, String name, String middleName,
                           String birthdate, String gender, String number) {
        ApiCall.getInstance().setProfile(lastName, name, middleName,
                birthdate, gender, number, profileResult);
    }

    public void profileDataChanged(String lastName, String name, String middleName,
                               String birthdate, String number, String email, boolean gender) {
        if (!Validation.isNameValid(lastName)) {
            profileForm.setLastNameError(R.string.invalid_name);
        } else {
            profileForm.setLastNameError(null);
        }
        if (!Validation.isNameValid(name)) {
            profileForm.setNameError(R.string.invalid_name);
        } else {
            profileForm.setNameError(null);
        }
        if (!Validation.isNameValid(middleName)) {
            profileForm.setMiddleNameError(R.string.invalid_name);
        } else {
            profileForm.setMiddleNameError(null);
        }
        if (!Validation.isBirthdateValid(birthdate)) {
            profileForm.setBirthdateError(R.string.invalid_date);
        } else {
            profileForm.setBirthdateError(null);
        }
        if (!Validation.isNumberValid(number)) {
            profileForm.setNumberError(R.string.invalid_number);
        } else {
            profileForm.setNumberError(null);
        }
        if (!Validation.isEmailValid(email)) {
            profileForm.setEmailError(R.string.invalid_email);
        } else {
            profileForm.setEmailError(null);
        }
        if (!gender) {
            profileForm.setDataValid(false);
        }
        if (Validation.isNameValid(lastName) && Validation.isNameValid(name) &&
                Validation.isNameValid(middleName) && Validation.isBirthdateValid(birthdate) &&
                Validation.isNumberValid(number) && Validation.isEmailValid(email)){
            profileForm = new ProfileFormState(true);
        }
        profileFormState.setValue(profileForm);
    }

    public static class ProfileFormState {
        @Nullable
        private Integer lastNameError;
        @Nullable
        private Integer nameError;
        @Nullable
        private Integer middleNameError;
        @Nullable
        private Integer birthdateError;
        @Nullable
        private Integer numberError;
        @Nullable
        private Integer emailError;
        private boolean isDataValid;

        ProfileFormState(boolean isDataValid) {
            this.lastNameError = null;
            this.nameError = null;
            this.middleNameError = null;
            this.birthdateError = null;
            this.numberError = null;
            this.isDataValid = isDataValid;
        }

        @Nullable
        public Integer getLastNameError() {
            return lastNameError;
        }

        @Nullable
        public Integer getNameError() {
            return nameError;
        }

        @Nullable
        public Integer getMiddleNameError() {
            return middleNameError;
        }

        @Nullable
        public Integer getBirthdateError() {
            return birthdateError;
        }

        @Nullable
        public Integer getNumberError() {
            return numberError;
        }

        @Nullable
        public Integer getEmailError() {
            return emailError;
        }

        public void setLastNameError(@Nullable Integer lastNameError) {
            this.lastNameError = lastNameError;
            isDataValid = false;
        }

        public void setNameError(@Nullable Integer nameError) {
            this.nameError = nameError;
            isDataValid = false;
        }

        public void setMiddleNameError(@Nullable Integer middleNameError) {
            this.middleNameError = middleNameError;
            isDataValid = false;
        }

        public void setBirthdateError(@Nullable Integer birthdateError) {
            this.birthdateError = birthdateError;
            isDataValid = false;
        }

        public void setNumberError(@Nullable Integer numberError) {
            this.numberError = numberError;
            isDataValid = false;
        }

        public void setEmailError(@Nullable Integer emailError) {
            this.emailError = emailError;
            isDataValid = false;
        }

        public void setDataValid(boolean dataValid) {
            isDataValid = dataValid;
        }

        public boolean isDataValid() {
            return isDataValid;
        }
    }

    public static class ProfileResult {
        @Nullable
        private User success;
        @Nullable
        private String error;

        public ProfileResult(@Nullable String error) {
            this.error = error;
        }

        public ProfileResult(@Nullable User success) {
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