package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TradePairingCoinDetail implements Serializable {

    @SerializedName("MARKETID")
    @Expose
    private int marketID;

    @SerializedName("COINID")
    @Expose
    private int coinID;

    @SerializedName("COINFULLNAME")
    @Expose
    private String coinFullName;

    @SerializedName("MARKETPRICE")
    @Expose
    private double marketPrice;

    @SerializedName("COINPRICE")
    @Expose
    private double coinPrice;

    @SerializedName("PERCOINPRICEINUSD")
    @Expose
    private double perCoinPriceInUSD;

    @SerializedName("PERMARKETCOINPRICEINUSD")
    @Expose
    private double perMarketCoinPriceInUSD;

    @SerializedName("SELLINGMAXRATE")
    @Expose
    private double sellingMaxRate;

    @SerializedName("SELLINGMINRATE")
    @Expose
    private double sellingMinRate;

    @SerializedName("BUYINGMAXRATE")
    @Expose
    private double buyingMaxRate;

    @SerializedName("BUYINGMINRATE")
    @Expose
    private double buyingMinRate;

    @SerializedName("SELLINGMAXVOLUME")
    @Expose
    private double sellingMaxVolume;

    @SerializedName("SELLINGMINVOLUME")
    @Expose
    private double sellingMinVolume;

    @SerializedName("BUYINGMAXVOLUME")
    @Expose
    private int buyingMaxVolume;

    @SerializedName("BUYINGMINVOLUME")
    @Expose
    private double buyingMinVolume;

    @SerializedName("TRADINGFEES")
    @Expose
    private double tradingFees;

    @SerializedName("BUYPERCOINPRICEINUSD")
    @Expose
    private double buyPerCoinPriceInUSD;

    @SerializedName("SELLPERCOINPRICEINUSD")
    @Expose
    private double sellPerCoinPriceInUSD;

    @SerializedName("BUYPERMARKETCOINPRICEINUSD")
    @Expose
    private double buyPerMarketCoinPriceInUSD;

    @SerializedName("SELLPERMARKETCOINPRICEINUSD")
    @Expose
    private double sellPerMarketCoinPriceInUSD;

    @SerializedName("BuyAvailableBalance")
    @Expose
    private double buyAvailableBalance;

    @SerializedName("SellAvailableBalance")
    @Expose
    private double sellAvailableBalance;

    public double getBuyAvailableBalance() {
        return buyAvailableBalance;
    }

    public void setBuyAvailableBalance(double buyAvailableBalance) {
        this.buyAvailableBalance = buyAvailableBalance;
    }

    public double getSellAvailableBalance() {
        return sellAvailableBalance;
    }

    public void setSellAvailableBalance(double sellAvailableBalance) {
        this.sellAvailableBalance = sellAvailableBalance;
    }

    public int getMarketID() {
        return marketID;
    }

    public void setMarketID(int marketID) {
        this.marketID = marketID;
    }

    public int getCoinID() {
        return coinID;
    }

    public void setCoinID(int coinID) {
        this.coinID = coinID;
    }

    public String getCoinFullName() {
        return coinFullName;
    }

    public void setCoinFullName(String coinFullName) {
        this.coinFullName = coinFullName;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(double coinPrice) {
        this.coinPrice = coinPrice;
    }

    public double getPerCoinPriceInUSD() {
        return perCoinPriceInUSD;
    }

    public void setPerCoinPriceInUSD(double perCoinPriceInUSD) {
        this.perCoinPriceInUSD = perCoinPriceInUSD;
    }

    public double getPerMarketCoinPriceInUSD() {
        return perMarketCoinPriceInUSD;
    }

    public void setPerMarketCoinPriceInUSD(double perMarketCoinPriceInUSD) {
        this.perMarketCoinPriceInUSD = perMarketCoinPriceInUSD;
    }

    public double getSellingMaxRate() {
        return sellingMaxRate;
    }

    public void setSellingMaxRate(double sellingMaxRate) {
        this.sellingMaxRate = sellingMaxRate;
    }

    public double getSellingMinRate() {
        return sellingMinRate;
    }

    public void setSellingMinRate(double sellingMinRate) {
        this.sellingMinRate = sellingMinRate;
    }

    public double getBuyingMaxRate() {
        return buyingMaxRate;
    }

    public void setBuyingMaxRate(double buyingMaxRate) {
        this.buyingMaxRate = buyingMaxRate;
    }

    public double getBuyingMinRate() {
        return buyingMinRate;
    }

    public void setBuyingMinRate(double buyingMinRate) {
        this.buyingMinRate = buyingMinRate;
    }

    public double getSellingMaxVolume() {
        return sellingMaxVolume;
    }

    public void setSellingMaxVolume(double sellingMaxVolume) {
        this.sellingMaxVolume = sellingMaxVolume;
    }

    public double getSellingMinVolume() {
        return sellingMinVolume;
    }

    public void setSellingMinVolume(double sellingMinVolume) {
        this.sellingMinVolume = sellingMinVolume;
    }

    public int getBuyingMaxVolume() {
        return buyingMaxVolume;
    }

    public void setBuyingMaxVolume(int buyingMaxVolume) {
        this.buyingMaxVolume = buyingMaxVolume;
    }

    public double getBuyingMinVolume() {
        return buyingMinVolume;
    }

    public void setBuyingMinVolume(double buyingMinVolume) {
        this.buyingMinVolume = buyingMinVolume;
    }

    public double getTradingFees() {
        return tradingFees;
    }

    public void setTradingFees(double tradingFees) {
        this.tradingFees = tradingFees;
    }

    public double getBuyPerCoinPriceInUSD() {
        return buyPerCoinPriceInUSD;
    }

    public void setBuyPerCoinPriceInUSD(double buyPerCoinPriceInUSD) {
        this.buyPerCoinPriceInUSD = buyPerCoinPriceInUSD;
    }

    public double getSellPerCoinPriceInUSD() {
        return sellPerCoinPriceInUSD;
    }

    public void setSellPerCoinPriceInUSD(double sellPerCoinPriceInUSD) {
        this.sellPerCoinPriceInUSD = sellPerCoinPriceInUSD;
    }

    public double getBuyPerMarketCoinPriceInUSD() {
        return buyPerMarketCoinPriceInUSD;
    }

    public void setBuyPerMarketCoinPriceInUSD(double buyPerMarketCoinPriceInUSD) {
        this.buyPerMarketCoinPriceInUSD = buyPerMarketCoinPriceInUSD;
    }

    public double getSellPerMarketCoinPriceInUSD() {
        return sellPerMarketCoinPriceInUSD;
    }

    public void setSellPerMarketCoinPriceInUSD(double sellPerMarketCoinPriceInUSD) {
        this.sellPerMarketCoinPriceInUSD = sellPerMarketCoinPriceInUSD;
    }
}
