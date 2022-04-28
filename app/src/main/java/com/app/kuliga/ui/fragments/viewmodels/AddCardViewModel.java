package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.R;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Validation;

public class AddCardViewModel extends ViewModel {

    private MutableLiveData<EditFormState> editFormState = new MutableLiveData<>();
    private MutableLiveData<AddCardResult> addResult = new MutableLiveData<>();
    private EditFormState profileForm = new EditFormState(false);

    public LiveData<EditFormState> getState() {
        return editFormState;
    }

    public LiveData<AddCardResult> getResult() {
        return addResult;
    }

    public void addCard(String code) {
        ApiCall.getInstance().cardAdd(addResult, code);
    }

    public void profileDataChanged(String name) {
        if (!Validation.isNameValid(name)) {
            profileForm.setNameError(R.string.invalid_name);
        } else {
            profileForm.setNameError(null);
        }
        if (Validation.isNameValid(name)){
            profileForm = new EditFormState(true);
        }
        editFormState.setValue(profileForm);
    }

    public static class EditFormState {
        @Nullable
        private Integer nameError;

        private boolean isDataValid;

        EditFormState(boolean isDataValid) {
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

    public static class AddCardResult {
        @Nullable
        private Boolean success;
        @Nullable
        private String error;

        public AddCardResult(@Nullable String error) {
            this.error = error;
        }

        public AddCardResult(@Nullable Boolean success) {
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