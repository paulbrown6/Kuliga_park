package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.History;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<HistoryResult> listResult = new MutableLiveData<>();

    public LiveData<HistoryResult> getResult() {
        return listResult;
    }

    public void loadHistory(String code) {
        ApiCall.getInstance().getCardHistory(listResult, code);
    }


    public static class HistoryResult {
        @Nullable
        private List<History> success;
        @Nullable
        private String error;

        public HistoryResult(@Nullable String error) {
            this.error = error;
        }

        public HistoryResult(@Nullable List<History> success) {
            this.success = success;
        }

        @Nullable
        public List<History> getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}