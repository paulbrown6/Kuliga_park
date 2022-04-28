package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.data.entity.Validation;

public class CardProfileViewModel extends ViewModel {

    private MutableLiveData<CardFormState> profileFormState = new MutableLiveData<>();
    private MutableLiveData<CardResult> profileResult = new MutableLiveData<>();
    private CardFormState profileForm = new CardFormState(false);

    public LiveData<CardFormState> getState() {
        return profileFormState;
    }

    public LiveData<CardResult> getResult() {
        return profileResult;
    }

    public void renameCard(String name, Card card) {
        ApiCall.getInstance().cardRename(profileResult, card, name);
    }

    public void profileDataChanged(String name) {
        if (!Validation.isNameValid(name)) {
            profileForm.setNameError(R.string.invalid_name);
        } else {
            profileForm.setNameError(null);
        }
        if (Validation.isNameValid(name)){
            profileForm = new CardFormState(true);
        }
        profileFormState.setValue(profileForm);
    }

    public static class CardFormState {
        @Nullable
        private Integer nameError;

        private boolean isDataValid;

        CardFormState(boolean isDataValid) {
            this.nameError = null;
            this.isDataValid = isDataValid;
        }

        @Nullable
        public Integer getNameError() {
            return nameError;
        }

        public void setNameError(@Nullable Integer nameError) {
            this.nameError = nameError;
            isDataValid = false;
        }

        public boolean isDataValid() {
            return isDataValid;
        }
    }

    public static class CardResult {
        @Nullable
        private Card success;
        @Nullable
        private String error;

        public CardResult(@Nullable String error) {
            this.error = error;
        }

        public CardResult(@Nullable Card success) {
            this.success = success;
        }

        @Nullable
        public Card getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}