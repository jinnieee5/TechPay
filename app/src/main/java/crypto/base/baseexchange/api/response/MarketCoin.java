package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MarketCoin implements Serializable {
    @SerializedName("PURCHASETYPE")
    @Expose
    private int purchaseType;

    @SerializedName("ID")
    @Expose
    private int pairingID;

    @SerializedName("TCLMID")
    @Expose
    private int tclmid;

    @SerializedName("BuyPrice")
    @Expose
    private double buyPrice;

    @SerializedName("SellPrice")
    @Expose
    private double sellPrice;

    @SerializedName("TPSPMID")
    @Expose
    private int tpspmid;

    @SerializedName("CoinName")
    @Expose
    private String coinName;

    @SerializedName("CoinCode")
    @Expose
    private String coinCode;

    @SerializedName("CoinLogoBase64Code")
    @Expose
    private String coinLogoBase64Code;

    @SerializedName("PERCOINRATEINUSD")
    @Expose
    private double perCoinRateInUSD;

    public int getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public int getPairingID() {
        return pairingID;
    }

    public void setPairingID(int pairingID) {
        this.pairingID = pairingID;
    }

    public int getTpspmid() {
        return tpspmid;
    }

    public void setTpspmid(int tpspmid) {
        this.tpspmid = tpspmid;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinLogoBase64Code() {
        return coinLogoBase64Code;
    }

    public void setCoinLogoBase64Code(String coinLogoBase64Code) {
        this.coinLogoBase64Code = coinLogoBase64Code;
    }

    public double getPerCoinRateInUSD() {
        return perCoinRateInUSD;
    }

    public void setPerCoinRateInUSD(double perCoinRateInUSD) {
        this.perCoinRateInUSD = perCoinRateInUSD;
    }

    public int getTclmid() {
        return tclmid;
    }

    public void setTclmid(int tclmid) {
        this.tclmid = tclmid;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }
}
