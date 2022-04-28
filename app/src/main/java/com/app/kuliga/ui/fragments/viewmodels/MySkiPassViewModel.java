package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.data.entity.Card;

public class MySkiPassViewModel extends ViewModel {

    private MutableLiveData<CardSelected> cardSelected = new MutableLiveData<>();

    public LiveData<CardSelected> getResult() {
        return cardSelected;
    }

    public void selectCard(Card card){
        cardSelected.setValue(new CardSelected(card));
    }

    public void unselectCard(){
        cardSelected.setValue(new CardSelected("card"));
    }

    public static class CardSelected {
        @Nullable
        private Card selected;
        @Nullable
        private String unselected;

        public CardSelected(@Nullable String unselected) {
            this.unselected = unselected;
        }

        public CardSelected(@Nullable Card selected) {
            this.selected = selected;
        }

        @Nullable
        public Card getSelected() {
            return selected;
        }

        @Nullable
        public String getUnselected() {
            return unselected;
        }
    }
}