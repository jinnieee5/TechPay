package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradeHistoryList {
    @SerializedName("TIME")
    @Expose
    private String Time;
    @SerializedName("TRADETYPE")
    @Expose
    private String TradeType;
    @SerializedName("MARKETID")
    @Expose
    private int marketID;
    @SerializedName("MARKETNAME")
    @Expose
    private String marketName;
    @SerializedName("MARKETPRICE")
    @Expose
    private double marketPrice;
    @SerializedName("COINID")
    @Expose
    private int coinID;
    @SerializedName("COINNAME")
    @Expose
    private String coinName;
    @SerializedName("COINPURCHASE")
    @Expose
    private double coinPurchase;
    @SerializedName("COINPRICE")
    @Expose
    private double coinPrice;

    public int getMarketID() {
        return marketID;
    }

    public void setMarketID(int marketID) {
        this.marketID = marketID;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getCoinID() {
        return coinID;
    }

    public void setCoinID(int coinID) {
        this.coinID = coinID;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public double getCoinPurchase() {
        return coinPurchase;
    }

    public void setCoinPurchase(double coinPurchase) {
        this.coinPurchase = coinPurchase;
    }

    public double getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(double coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
    }
}
