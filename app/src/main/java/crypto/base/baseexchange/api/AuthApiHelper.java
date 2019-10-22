package crypto.base.baseexchange.api;

import com.google.gson.JsonObject;
import crypto.base.baseexchange.api.request.BuySellReq;
import crypto.base.baseexchange.api.request.CancelOpenOrderReq;
import crypto.base.baseexchange.api.request.ChangePasswordReq;
import crypto.base.baseexchange.api.request.ChangeSecretPinReq;
import crypto.base.baseexchange.api.request.ForgotPasswordReq;
import crypto.base.baseexchange.api.request.GenerateTicketReq;
import crypto.base.baseexchange.api.request.GetAllTicketRepliesReq;
import crypto.base.baseexchange.api.request.GetTransactionFeeReq;
import crypto.base.baseexchange.api.request.InterExchangeCoinMarketReq;
import crypto.base.baseexchange.api.request.InterExchangeParingListReq;
import crypto.base.baseexchange.api.request.LoginReq;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.request.ReplyTicketReq;
import crypto.base.baseexchange.api.request.RequestCoinReq;
import crypto.base.baseexchange.api.request.SendCoinReq;
import crypto.base.baseexchange.api.request.SignupReq;
import crypto.base.baseexchange.api.request.TradeOpenOrderRequest;
import crypto.base.baseexchange.api.request.UpdateProfilePicReq;
import crypto.base.baseexchange.api.request.UpdateProfileReq;
import crypto.base.baseexchange.api.request.UploadKycReq;
import crypto.base.baseexchange.api.request.UserOrderListReq;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/*
* 0- no session check
* 1- session ok
* 2- session expired
* */
public interface AuthApiHelper {

    //app version
    @GET("get-app-details")
    Single<JsonObject> getAppDetails();  //no session check

    //login-registration
    @Headers("Content-Type: application/json")
    @POST("sign-up")
    Single<JsonObject> doSignUp(@Body SignupReq signupReq); //no session check

    @POST("login")
    @Headers("Content-Type: application/json")
    Single<JsonObject> doLogin(@Body LoginReq loginReq);    //no session check

    @Headers("Content-Type: application/json")
    @POST("forgot-password")
    Single<JsonObject> forgotPassword(@Body ForgotPasswordReq forgotPasswordReq);   //no session check

    //dashboard\
    @GET("get-coin-list")
    Single<JsonObject> getAllCoinList();    //no session check

    @GET("get-live-rates")
    Single<JsonObject> getLiveRates();  //no session check

    @Headers("Content-Type: application/json")
    @POST("get-wallet-balance")
    Single<JsonObject> getWalletBalances(@Body LoginTokenReq loginTokenReq);    //session check

    //transfer-receive
    @Headers("Content-Type: application/json")
    @POST("receive-coin")
    Single<JsonObject> depositCoin(@Body RequestCoinReq requestCoinReq);    //session check

    @Headers("Content-Type: application/json")
    @POST("transfer-coin")
    Single<JsonObject> withdrawalCoin(@Body SendCoinReq sendCoinReq);   //session check

    @Headers("Content-Type: application/json")
    @POST("transfer-coin-transaction-list")
    Single<JsonObject> withdrawalHistory(@Body LoginTokenReq loginTokenReq);    //session check

    @Headers("Content-Type: application/json")
    @POST("get-transaction-fee")
    Single<JsonObject> getTransactionFee(@Body GetTransactionFeeReq transactionFeeReq); //session check

    @Headers("Content-Type: application/json")
    @POST("receive-coin-transaction-list")
    Single<JsonObject> depositHistory(@Body LoginTokenReq loginTokenReq);   //session check

    //buy-sell
    @Headers("Content-Type: application/json")
    @POST("get-market-list")
    Single<JsonObject> getInterExchangeCoinList(@Body LoginTokenReq loginTokenReq); //no session check

    @Headers("Content-Type: application/json")
    @POST("get-pair-data")
    Single<JsonObject> getInterExchangeCoinPairingList(@Body InterExchangeParingListReq interExchangeParingListReq);    //no session check

    @Headers("Content-Type: application/json")
    @POST("get-market-details")
    Single<JsonObject> getInterExchangeCoinMarketDetailList(@Body InterExchangeCoinMarketReq interExchangeCoinMarketReq);   //no session check

    @Headers("Content-Type: application/json")
    @POST("buy-coin")
    Single<JsonObject> buyCoin(@Body BuySellReq buyCoinReq);    //session check

    @Headers("Content-Type: application/json")
    @POST("sell-coin")
    Single<JsonObject> sellCoin(@Body BuySellReq sellCoinReq);  //session check

    //get all buy/sell and open/closed user's order list
    @Headers("Content-Type: application/json")
    @POST("sell-coin-transaction-list")
    Single<JsonObject> getAllOrdersList(@Body UserOrderListReq userOrderListReq);   //session check

    @Headers("Content-Type: application/json")
    @POST("cancel-open-order")
    Single<JsonObject> cancelOpenOrder(@Body CancelOpenOrderReq cancelOpenOrderReq);    //session check

    //ticket
    @GET("get-heldpdesk-data")
    Single<JsonObject> getCatPriorToGenerateTicket();   //no session check

    @Headers("Content-Type: application/json")
    @POST("generate-ticket")
    Single<JsonObject> generateTicket(@Body GenerateTicketReq generateTicketReq);   //session check

    @Headers("Content-Type: application/json")
    @POST("all-ticket-list")
    Single<JsonObject> getAllTicketList(@Body LoginTokenReq loginTokenReq); //session check

    @Headers("Content-Type: application/json")
    @POST("ticket-reply-details")
    Single<JsonObject> getAllTicketReplies(@Body GetAllTicketRepliesReq getAllTicketRepliesReq);    //session check

    @Headers("Content-Type: application/json")
    @POST("ticket-reply")
    Single<JsonObject> replyToTicket(@Body ReplyTicketReq replyTicketReq);  //session check

    //login history
    @Headers("Content-Type: application/json")
    @POST("get-login-history")
    Single<JsonObject> getLoginHistory(@Body LoginTokenReq loginTokenReq);  //session check

    //settings
    @Headers("Content-Type: application/json")
    @POST("change-password")
    Single<JsonObject> changePassword(@Body ChangePasswordReq changePasswordReq);   //session check

    @Headers("Content-Type: application/json")
    @POST("secret-pin-status")
    Single<JsonObject> checkSecretPINStatus(@Body LoginTokenReq loginTokenReq); //session check

    @Headers("Content-Type: application/json")
    @POST("change-secret-pin")
    Single<JsonObject> changeSecretPIN(@Body ChangeSecretPinReq changeSecretPinReq);    //session check

    //profile
    @Headers("Content-Type: application/json")
    @POST("update-profile-image")
    Single<JsonObject> updateProfilePic(@Body UpdateProfilePicReq updateProfilePicReq); //session check

    @Headers("Content-Type: application/json")
    @POST("get-profile-image")
    Single<JsonObject> getProfilePic(@Body LoginTokenReq loginTokenReq);    //session check

    @Headers("Content-Type: application/json")
    @POST("get-profile-details")
    Single<JsonObject> getProfileDetails(@Body LoginTokenReq loginTokenReq);    //session check

    @Headers("Content-Type: application/json")
    @POST("update-profile-details")
    Single<JsonObject> updateProfileDetails(@Body UpdateProfileReq updateProfileReq);   //session check

    //KYC
    @Headers("Content-Type: application/json")
    @POST("upload-kyc")
    Single<JsonObject> updateKYC(@Body UploadKycReq uploadKycReq);  //session check

    @Headers("Content-Type: application/json")
    @POST("get-kyc-details")
    Single<JsonObject> getKycDetails(@Body LoginTokenReq loginTokenReq);    //session check

    //trade
    @Headers("Content-Type: application/json")
    @POST("system-buy-orders")
    Single<JsonObject> tradeBuyOrders(@Body TradeOpenOrderRequest tradeOpenOrderRequest);   //no session check

    @Headers("Content-Type: application/json")
    @POST("system-sell-orders")
    Single<JsonObject> tradeSellOrders(@Body TradeOpenOrderRequest tradeOpenOrderRequest);  //no session check

    @Headers("Content-Type: application/json")
    @POST("system-trade-history")
    Single<JsonObject> tradeOrderHistory(@Body TradeOpenOrderRequest tradeOpenOrderRequest);    //no session check

    @Headers("Content-Type: application/json")
    @POST("get-referral-details")
    Single<JsonObject> getReferralDetails(@Body LoginTokenReq loginTokenReq);    //no session check
}
