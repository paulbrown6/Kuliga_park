package com.app.kuliga.data.api.retrofit;

import com.app.kuliga.data.api.entity.AuthEntity;
import com.app.kuliga.data.api.entity.ResponseEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("auth/login")
    Call<AuthEntity> authorisation(@Query("Login") String login,
                                   @Query("Password") String password);

    @GET("nologin/srv/Baloon/Person/Register_API")
    Call<ResponseEntity> registration(@Query("regcode") String regcode,
                                      @Query("email") String login,
                                      @Query("password") String password,
                                      @Query("password2") String password2);

    @GET("nologin/srv/Basicsite/AuthConfirmationToken/SubmitRestorePassword")
    Call<ResponseEntity> restorePass(@Query("login") String login);

    @GET("srv/Baloon/Person/GetCurrent")
    Call<ResponseEntity> getProfile(@Header("Cookie") String cookie);

    @POST("srv/Baloon/Person/UpdateCurrent")
    Call<ResponseEntity> setProfile(@Header("Cookie") String cookie,
                                    @Query("last_name") String lastName,
                                    @Query("first_name") String name,
                                    @Query("middle_name") String middleName,
                                    @Query("birthdate") String birthday,
                                    @Query("gender") String gender,
                                    @Query("phone") String phone);

    @GET("srv/Baloon/Identifier/CurrentIdentifierListGet_FE")
    Call<ResponseEntity> getCardsList(@Header("Cookie") String cookie);

    @GET("srv/Baloon/Identifier/GetBalance_FE")
    Call<ResponseEntity> getCardBalance(@Header("Cookie") String cookie,
                                        @Query("code") String code);

    @GET("srv/Baloon/Transaction/IdentifierTransactionListGet_FE")
    Call<ResponseEntity> getCardHistory(@Header("Cookie") String cookie,
                                        @Query("code") String code);

    @POST("srv/Baloon/Identifier/IdentifierNameUpdate_FE")
    Call<ResponseEntity> renameCard(@Header("Cookie") String cookie,
                                    @Query("code") String code,
                                    @Query("name") String name);

    @POST("srv/Baloon/Identifier/BindIdentifierToCurrentPerson_FE")
    Call<ResponseEntity> addCard(@Header("Cookie") String cookie,
                                 @Query("code") String code);

    @GET("srv/Baloon/IdentifierInvoice/RaiseInvoice_FE")
    Call<ResponseEntity> buyService(@Header("Cookie") String cookie,
                                    @Query("amount") String amount,
                                    @Query("code") String card,
                                    @Query("contents") String contents,
                                    @Query("fail_url") String fail_url,
                                    @Query("success_url") String success_url);

    @GET("srv/Baloon/PaidServiceCategory/PaidServiceCategoryListGet_FE")
    Call<ResponseEntity> getTariffsCategory(@Header("Cookie") String cookie);

    @GET("srv/Baloon/PaidService/PaidServiceListGet_API")
    Call<ResponseEntity> getTariffPrice(@Header("Cookie") String cookie,
                                        @Query("categoryid") String id);

    @GET("srv/Baloon/Advertisement/VisualItemListGet_FE_1_13")
    Call<ResponseEntity> getStocks(@Header("Cookie") String cookie);

    @GET("srv/Baloon/SpdServiceRule/SpdServiceRuleListGet_API")
    Call<ResponseEntity> getServices(@Header("Cookie") String cookie,
                                     @Query("identifier_rulename") String rulename);

    @GET("srv/Baloon/Identifier/GetDetails_FE")
    Call<ResponseEntity> getCardInfo(@Header("Cookie") String cookie,
                                     @Query("code") String code);


    @GET("srv/Baloon/PackageOrder/MyPackageListGet_API")
    Call<ResponseEntity> getCardOrders(@Header("Cookie") String cookie,
                                       @Query("code") String code);
}