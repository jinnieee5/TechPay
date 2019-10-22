package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCoinReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("TCLMID")
    @Expose
    private String tCLMID;
    @SerializedName("CoinCode")
    @Expose
    private String coinCode;
    @SerializedName("CoinName")
    @Expose
    private String coinName;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getTCLMID() {
        return tCLMID;
    }

    public void setTCLMID(String tCLMID) {
        this.tCLMID = tCLMID;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}
