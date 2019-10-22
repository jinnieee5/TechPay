package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuySellReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("TCLMID")
    @Expose
    private int tclmid;
    @SerializedName("TradeVolume")
    @Expose
    private double tradeVolume;
    @SerializedName("TradeRate")
    @Expose
    private double tradeRate;
    @SerializedName("TPSPMID")
    @Expose
    private int tpspmid;
    @SerializedName("TradeFeeVolume")
    @Expose
    private double tradeFeeVolume;
    @SerializedName("TradeFeePercentage")
    @Expose
    private double tradeFeePercentage;
    @SerializedName("TTIFMID")
    @Expose
    private int ttifmid;
    @SerializedName("AUTOCLOSEPROFITPERCENTAGE")
    @Expose
    private double autoCloseProfitPercentage;
    @SerializedName("AUTOCLOSELOSSPERCENTAGE")
    @Expose
    private double autoCloseLossPercentage;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public int getTclmid() {
        return tclmid;
    }

    public void setTclmid(int tclmid) {
        this.tclmid = tclmid;
    }

    public double getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(double tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public double getTradeRate() {
        return tradeRate;
    }

    public void setTradeRate(double tradeRate) {
        this.tradeRate = tradeRate;
    }

    public int getTpspmid() {
        return tpspmid;
    }

    public void setTpspmid(int tpspmid) {
        this.tpspmid = tpspmid;
    }

    public double getTradeFeeVolume() {
        return tradeFeeVolume;
    }

    public void setTradeFeeVolume(double tradeFeeVolume) {
        this.tradeFeeVolume = tradeFeeVolume;
    }

    public double getTradeFeePercentage() {
        return tradeFeePercentage;
    }

    public void setTradeFeePercentage(double tradeFeePercentage) {
        this.tradeFeePercentage = tradeFeePercentage;
    }

    public int getTtifmid() {
        return ttifmid;
    }

    public void setTtifmid(int ttifmid) {
        this.ttifmid = ttifmid;
    }

    public double getAutoCloseProfitPercentage() {
        return autoCloseProfitPercentage;
    }

    public void setAutoCloseProfitPercentage(double autoCloseProfitPercentage) {
        this.autoCloseProfitPercentage = autoCloseProfitPercentage;
    }

    public double getAutoCloseLossPercentage() {
        return autoCloseLossPercentage;
    }

    public void setAutoCloseLossPercentage(double autoCloseLossPercentage) {
        this.autoCloseLossPercentage = autoCloseLossPercentage;
    }
}
