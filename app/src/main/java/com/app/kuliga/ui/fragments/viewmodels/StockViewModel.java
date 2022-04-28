package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Stock;

import java.util.List;

public class StockViewModel extends ViewModel {

    private MutableLiveData<StockResult> listResult = new MutableLiveData<>();

    public LiveData<StockResult> getResult() {
        return listResult;
    }

    public void loadStocks() {
        ApiCall.getInstance().getStocks(listResult);
    }


    public static class StockResult {
        @Nullable
        private List<Stock> success;
        @Nullable
        private String error;

        public StockResult(@Nullable String error) {
            this.error = error;
        }

        public StockResult(@Nullable List<Stock> success) {
            this.success = success;
        }

        @Nullable
        public List<Stock> getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}