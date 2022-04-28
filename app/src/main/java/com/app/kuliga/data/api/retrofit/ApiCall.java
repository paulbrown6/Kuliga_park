package com.app.kuliga.data.api.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.kuliga.data.api.entity.AuthEntity;
import com.app.kuliga.data.api.entity.ResponseEntity;
import com.app.kuliga.data.api.entity.Weather;
import com.app.kuliga.data.entity.Card;
import com.app.kuliga.data.entity.History;
import com.app.kuliga.data.entity.Service;
import com.app.kuliga.data.entity.Stock;
import com.app.kuliga.data.entity.User;
import com.app.kuliga.ui.fragments.viewmodels.AddCardViewModel;
import com.app.kuliga.ui.fragments.viewmodels.CardProfileViewModel;
import com.app.kuliga.ui.fragments.viewmodels.HistoryViewModel;
import com.app.kuliga.ui.fragments.viewmodels.LoginViewModel;
import com.app.kuliga.ui.fragments.viewmodels.MainViewModel;
import com.app.kuliga.ui.fragments.viewmodels.NoPassViewModel;
import com.app.kuliga.ui.fragments.viewmodels.ProfileViewModel;
import com.app.kuliga.ui.fragments.viewmodels.RegistrationViewModel;
import com.app.kuliga.ui.fragments.viewmodels.ServicesViewModel;
import com.app.kuliga.ui.fragments.viewmodels.StockViewModel;
import com.app.kuliga.ui.fragments.viewmodels.WeatherViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {

    private static ApiCall instance;
    private String TAG = "API";
    private String cookie;
    private final String MESSAGE_ERROR_LOGIN = "Неверный логин или пароль!";
    private final String MESSAGE_ERROR_REG = "Неизвестная ошибка, попробуйте ввести другие данные!";
    private final String MESSAGE_ERROR_NO_PASS = "E-mail не найден, попробуйте ввести другой!";
    private final String MESSAGE_ERROR_SERVER = "Ошибка соединения с сервером!";
    private final String MESSAGE_ERROR_ADD = "Вы ввели не правильный идендификатор!";
    private String returnUrl = App.getInstance().getServerURL() + "public/mobile-payment-successfull.html";
    private String failUrl = App.getInstance().getServerURL() + "https://kuliga-mobile.kuliga-park.ru/public/mobile-payment-failed";

    public static ApiCall getInstance(){
        if(instance == null) {
            instance = new ApiCall();
        }
        return instance;
    }


    private void logMessage(String method, String message, Integer code){
        Log.d(TAG, method + " || " + code + " || " + message);
    }

    public static void clear(){
        instance = new ApiCall();
    }

    public void signIn(String login, String password,
                       MutableLiveData<LoginViewModel.LoginResult> result){
        App.getInstance().getApi().authorisation(
                QueryConvert.valueToString(login),
                QueryConvert.valueToString(password)).enqueue(new Callback<AuthEntity>() {
            @Override
            public void onResponse(Call<AuthEntity> call, Response<AuthEntity> response) {
                if (response.body() != null){
                    String s = response.headers().values("Set-Cookie").toString();
                    cookie = s.substring(1, s.length()-1);
                    getProfile(result);
                } else {
                    result.setValue(new LoginViewModel.LoginResult(MESSAGE_ERROR_LOGIN));
                }
                logMessage("signIn", response.toString(), response.code());
                logMessage("signIn", call.request().toString(), response.code());
            }

            @Override
            public void onFailure(Call<AuthEntity> call, Throwable t) {
                result.setValue(new LoginViewModel.LoginResult(MESSAGE_ERROR_SERVER));
                logMessage("signIn", t.getMessage(), 0);
            }
        });
    }

    public void signUp(String regcode, String login, String password,
                       MutableLiveData<RegistrationViewModel.RegResult> result){
        App.getInstance().getApi().registration(
                QueryConvert.valueToString(regcode),
                QueryConvert.valueToString(login),
                QueryConvert.valueToString(password),
                QueryConvert.valueToString(password)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.body() != null){
                    result.setValue(new RegistrationViewModel.RegResult(true));
                } else {
                    result.setValue(new RegistrationViewModel.RegResult(MESSAGE_ERROR_REG));
                }
                logMessage("signUp", response.toString(), response.code());
                logMessage("signUp", call.request().toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new RegistrationViewModel.RegResult(MESSAGE_ERROR_SERVER));
                logMessage("signUp", t.getMessage(), 0);
            }
        });
    }

    public void restorePassword(String login,
                       MutableLiveData<NoPassViewModel.NoPassResult> result){
        App.getInstance().getApi().restorePass(
                QueryConvert.valueToString(login)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.body() != null){
                    result.setValue(new NoPassViewModel.NoPassResult(true));
                } else {
                    result.setValue(new NoPassViewModel.NoPassResult(MESSAGE_ERROR_NO_PASS));
                }
                logMessage("restorePassword", response.toString(), response.code());
                logMessage("restorePassword", call.request().toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new NoPassViewModel.NoPassResult(MESSAGE_ERROR_SERVER));
                logMessage("restorePassword", t.getMessage(), 0);
            }
        });
    }

    public void getProfile(MutableLiveData<LoginViewModel.LoginResult> result){
        App.getInstance().getApi().getProfile(
                cookie).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    User user = User.createFromXML(response.body());
                    user.setCookie(cookie);
                    result.setValue(new LoginViewModel.LoginResult(user));
                } else {
                    result.setValue(new LoginViewModel.LoginResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getProfile", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new LoginViewModel.LoginResult(MESSAGE_ERROR_SERVER));
                logMessage("getProfile", t.getMessage(), 0);
            }
        });
    }

    public void setProfile(String lastName, String name, String middleName,
                           String birthdate, String gender, String phone,
                           MutableLiveData<ProfileViewModel.ProfileResult> result){
        App.getInstance().getApi().setProfile(cookie,
                QueryConvert.valueToString(lastName),
                QueryConvert.valueToString(name),
                QueryConvert.valueToString(middleName),
                QueryConvert.valueToDate(birthdate) + "&",
                QueryConvert.valueToString(gender),
                QueryConvert.valueToString(phone)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    User user = new User();
                    user.setLastName(lastName);
                    user.setFirstName(name);
                    user.setMiddleName(middleName);
                    user.setBirthday(dateFormat(birthdate));
                    user.setGender(gender);
                    user.setPhone(phone);
                    result.setValue(new ProfileViewModel.ProfileResult(user));
                } else {
                    result.setValue(new ProfileViewModel.ProfileResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("setProfile", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new ProfileViewModel.ProfileResult(MESSAGE_ERROR_SERVER));
                logMessage("setProfile", t.getMessage(), 0);
            }
        });
    }

    public void getCards(MutableLiveData<MainViewModel.LoadCardsResult> result){
        App.getInstance().getApi().getCardsList(
                cookie).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    List<Card> cards = Card.createListFromXML(response.body());
                    result.setValue(new MainViewModel.LoadCardsResult(cards));
                } else {
                    result.setValue(new MainViewModel.LoadCardsResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getCards", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new MainViewModel.LoadCardsResult(MESSAGE_ERROR_SERVER));
                logMessage("getCards", t.getMessage(), 0);
            }
        });
    }

    public void getCardBalance(MutableLiveData<MainViewModel.LoadCardResult> result,
                               String code){
        App.getInstance().getApi().getCardBalance(cookie,
                QueryConvert.valueToString(code)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    Card card = new Card();
                    card.setCode(code);
                    card.setBalanceFromXML(response.body());
                    result.setValue(new MainViewModel.LoadCardResult(card));
                } else {
                    result.setValue(null);
                }
                logMessage("getCardBalance", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new MainViewModel.LoadCardResult(MESSAGE_ERROR_SERVER));
                logMessage("getCardBalance", t.getMessage(), 0);
            }
        });
    }

    public void getCardHistory(MutableLiveData<HistoryViewModel.HistoryResult> result,
                               String code){
        App.getInstance().getApi().getCardHistory(cookie,
                QueryConvert.valueToString(code)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    List<History> histories = History.createListFromXML(response.body());
                    result.setValue(new HistoryViewModel.HistoryResult(histories));
                } else {
                    result.setValue(new HistoryViewModel.HistoryResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getCardHistory", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new HistoryViewModel.HistoryResult(MESSAGE_ERROR_SERVER));
                logMessage("getCardHistory", t.getMessage(), 0);
            }
        });
    }


    public void cardRename(MutableLiveData<CardProfileViewModel.CardResult> result,
                               Card card, String name){
        App.getInstance().getApi().renameCard(cookie,
                QueryConvert.valueToString(card.getCode()),
                QueryConvert.valueToString(name)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    card.setName(name);
                    result.setValue(new CardProfileViewModel.CardResult(card));
                } else {
                    result.setValue(new CardProfileViewModel.CardResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("cardRename", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new CardProfileViewModel.CardResult(MESSAGE_ERROR_SERVER));
                logMessage("cardRename", t.getMessage(), 0);
            }
        });
    }

    public void cardAdd(MutableLiveData<AddCardViewModel.AddCardResult> result,
                           String code){
        App.getInstance().getApi().addCard(cookie,
                QueryConvert.valueToString(code)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    result.setValue(new AddCardViewModel.AddCardResult(true));
                } else {
                    result.setValue(new AddCardViewModel.AddCardResult(MESSAGE_ERROR_ADD));
                }
                logMessage("cardRename", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new AddCardViewModel.AddCardResult(MESSAGE_ERROR_SERVER));
                logMessage("cardRename", t.getMessage(), 0);
            }
        });
    }

    public void getPay(String code, String contents, String price,
                       MutableLiveData<MainViewModel.OperationResult> result){
        Log.e(TAG, "buyService: " + code + " || " + contents + " || " + price);
        App.getInstance().getApi().buyService(cookie,
                QueryConvert.valueToDecimal(price) + "&",
                QueryConvert.valueToString(code),
                QueryConvert.valueToString(contents),
                QueryConvert.valueToString(failUrl),
                QueryConvert.valueToString(returnUrl)
        ).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null) {
                        String url = "";
                        try {
                            url = response.body().getaPacket().Value.get(1).
                                    Structure.getValue("action_url").string;
                        } catch (Exception e) {
                            Log.d(TAG, "getPay onResponse: " + e.getMessage());
                        }
                        MainViewModel.setPayURL(url);
                        result.setValue(new MainViewModel.OperationResult(0));
                } else {
                    result.setValue(new MainViewModel.OperationResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getPay", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new MainViewModel.OperationResult(MESSAGE_ERROR_SERVER));
                logMessage("getProfile", t.getMessage(), 0);
            }
        });
    }

    public void getServices(MutableLiveData<ServicesViewModel.LoadServicesResult> result){
        App.getInstance().getApi().getTariffsCategory(
                cookie).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    List<Service> services = Service.createListFromXML(response.body());
                    result.setValue(new ServicesViewModel.LoadServicesResult(services));
                } else {
                    result.setValue(new ServicesViewModel.LoadServicesResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getServices", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new ServicesViewModel.LoadServicesResult(MESSAGE_ERROR_SERVER));
                logMessage("getServices", t.getMessage(), 0);
            }
        });
    }

    public void getServiceTariff(MutableLiveData<ServicesViewModel.LoadServiceResult> result,
                               String id){
        App.getInstance().getApi().getTariffPrice(cookie,
                QueryConvert.valueToInt(id)).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    Service service = new Service();
                    service.setServiceId(id);
                    service.setTariffFromXML(response.body());
                    result.setValue(new ServicesViewModel.LoadServiceResult(service));
                } else {
                    result.setValue(null);
                }
                logMessage("getServiceTariff", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new ServicesViewModel.LoadServiceResult(MESSAGE_ERROR_SERVER));
                logMessage("getServiceTariff", t.getMessage(), 0);
            }
        });
    }

    public void getStocks(MutableLiveData<StockViewModel.StockResult> result){
        App.getInstance().getApi().getStocks(
                cookie).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.code() == 200 && response.body() != null){
                    List<Stock> stocks = Stock.createListFromXML(response.body());
                    result.setValue(new StockViewModel.StockResult(stocks));
                } else {
                    result.setValue(new StockViewModel.StockResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getCards", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                result.setValue(new StockViewModel.StockResult(MESSAGE_ERROR_SERVER));
                logMessage("getCards", t.getMessage(), 0);
            }
        });
    }

    public void getWeather(MutableLiveData<WeatherViewModel.WeatherResult> result){
        AppWeather.getInstance().getApi().getCurrent(
                "1b92e9edd2e947a6802223019220602", "56.9634,66.9482",
                "yes", "ru").enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.code() == 200 && response.body() != null){
                    result.setValue(new WeatherViewModel.WeatherResult(response.body()));
                } else {
                    result.setValue(new WeatherViewModel.WeatherResult(MESSAGE_ERROR_SERVER));
                }
                logMessage("getWeather", response.toString(), response.code());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                result.setValue(new WeatherViewModel.WeatherResult(MESSAGE_ERROR_SERVER));
                logMessage("getWeather", t.getMessage(), 0);
            }
        });
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getFailUrl() {
        return failUrl;
    }

    private String dateFormat(String s){
        return s.substring(6, 8) + "." + s.substring(4, 6) + "." + s.substring(0, 4);
    }

    static class QueryConvert{

        @NonNull
        static String valueToString(String value){
            return "s:" + value;
        }

        @NonNull
        static String valueToDecimal(String value){
            return "decimal:s:" + value;
        }

        @NonNull
        static String valueToDate(String value){
            return "ADate:s:" + value;
        }

        @NonNull
        static String valueToInt(String value){
            return "i:" + value;
        }
    }
}
