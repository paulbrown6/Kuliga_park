package com.app.kuliga.ui.fragments.viewmodels;

import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.data.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private static List<Card> cards = new ArrayList<>();
    private static String payURL = "";

    private static MutableLiveData<LoadCardsResult> loadCardsResult = new MutableLiveData<>();
    public static LiveData<LoadCardsResult> getLoadCardsResult() { return loadCardsResult; }
    private static MutableLiveData<LoadCardResult> loadCardResult = new MutableLiveData<>();
    public static LiveData<LoadCardResult> getLoadCardResult() { return loadCardResult; }
    private static MutableLiveData<OperationResult> payResult = new MutableLiveData<>();
    private static MutableLiveData<OperationResult> timerResult = new MutableLiveData<>();
    public static LiveData<OperationResult> getPayResult() { return payResult; }
    public static void setPayResultOk(Integer num){
        payResult.setValue(new OperationResult(num));
    }
    public static LiveData<OperationResult> getTimerResult() { return timerResult; }

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        if (!isUserFromDB(user)){
            user.save();
        }
        MainViewModel.user = user;
    }

    public static void updateUser(User us){
        user.setLastName(us.getLastName());
        user.setFirstName(us.getFirstName());
        user.setMiddleName(us.getMiddleName());
        user.setBirthday(us.getBirthday());
        user.setGender(us.getGender());
        user.setPhone(us.getPhone());
        user.save();
    }

    public static void deleteUser(){
        if (isUserFromDB(user)) {
            user.delete();
            deleteCardsFromDB();
        }
        user = null;
    }

    private static boolean isUserFromDB(User us){
        List<User> users = User.listAll(User.class);
        if (users == null || users.isEmpty()){
            return false;
        }
        for (User userDB: users){
            if (userDB.getUserId().equals(us.getUserId())){
                return true;
            }
        }
        return false;
    }

    public static void loadCards(){
        List<Card> cards = Card.listAll(Card.class);
        if (cards != null && !cards.isEmpty()){
            loadCardsFromDB();
        } else {
            ApiCall.getInstance().getCards(loadCardsResult);
        }
    }

    public static void updateCards(){
        ApiCall.getInstance().getCards(loadCardsResult);
    }

    private static void loadCardsFromDB(){
        List<Card> cards = Card.listAll(Card.class);
        loadCardsResult.setValue(new LoadCardsResult(cards));
    }

    public static void saveCardsToDB(List<Card> cards){
        for (Card card: cards){
            if(!isCardFromDB(card)){
                card.save();
            }
        }
        List<Card> cs = Card.listAll(Card.class);
        if (cs != null) {
            setCards(cs);
        }
    }

    public static void saveCardToDB(Card card){
        Card newCard = card;
        List<Card> cardsDB = Card.listAll(Card.class);
        if (cardsDB != null && !cardsDB.isEmpty()) {
            for (Card oldCard : cardsDB) {
                if (card.getCardId().equals(oldCard.getCardId())){
                    newCard = oldCard;
                    newCard.setName(card.getName());
                    newCard.setBalance(card.getBalance());
                    break;
                }
            }
        }
        newCard.save();
        List<Card> cs = Card.listAll(Card.class);
        if (cs != null) {
            setCards(cs);
        }
    }

    private static boolean isCardFromDB(Card card){
        List<Card> cardsDB = Card.listAll(Card.class);
        if (cardsDB != null && !cardsDB.isEmpty()){
            return false;
        }
        for (Card oldCard : cardsDB) {
            if (card.getCardId().equals(oldCard.getCardId())){
                return true;
            }
        }
        return false;
    }

    public static void deleteCardsFromDB(){
        List<Card> cardsDB = Card.listAll(Card.class);
        if (cardsDB != null && !cardsDB.isEmpty()) {
            for (Card card : cardsDB) {
                card.delete();
            }
        }
    }

    public static void loadCardBalance(Card card){
        ApiCall.getInstance().getCardBalance(loadCardResult, card.getCode());
    }

    public static List<Card> getCards() {
        return cards;
    }

    public static void setCards(List<Card> cards) {
        MainViewModel.cards = cards;
    }

    public static void pay(String code, String summ){
        ApiCall.getInstance().getPay(code, "Skipass", summ, payResult);
    }

    public static String getPayURL() {
        return payURL;
    }

    public static void setPayURL(String url){
        payURL = url;
    }

    public static void startTimer(){
        new CountDownTimer( 60000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                timerResult.setValue(new OperationResult(1));
            }
        }.start();
    }

    public static class LoadCardsResult {
        @Nullable
        private List<Card> success;
        @Nullable
        private String error;

        public LoadCardsResult(@Nullable String error) {
            this.error = error;
        }

        public LoadCardsResult(@Nullable List<Card> success) {
            this.success = success;
        }

        @Nullable
        public List<Card> getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }

    public static class LoadCardResult{
        @Nullable
        private Card success;
        @Nullable
        private String error;

        public LoadCardResult(@Nullable String error) {
            this.error = error;
        }

        public LoadCardResult(@Nullable Card success) {
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

    public static class OperationResult {
        @Nullable
        private Integer success;
        @Nullable
        private String error;

        public OperationResult(@Nullable String error) {
            this.error = error;
        }

        public OperationResult(@Nullable Integer success) {
            this.success = success;
        }

        @Nullable
        public Integer getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }

}