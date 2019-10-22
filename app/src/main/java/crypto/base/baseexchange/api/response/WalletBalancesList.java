package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class WalletBalancesList implements Serializable {

    @SerializedName("REGNO")
    @Expose
    private int regNo;
    @SerializedName("COINNAME")
    @Expose
    private String coinName;
    @SerializedName("COINCODE")
    @Expose
    private String coinCode;
    @SerializedName("RESERVED")
    @Expose
    private double reserved;
    @SerializedName("AVAILABLE")
    @Expose
    private double available;
    @SerializedName("Status")
    @Expose
    private int status;
    @SerializedName("PENDINGDEPOSIT")
    @Expose
    private double pendingDeposit;
    @SerializedName("PERCENTAGECHANGE")
    @Expose
    private double percentageChange;
    @SerializedName("TOTALINCOME")
    @Expose
    private double totalIncome;
    @SerializedName("PriceInBTC")
    @Expose
    private double priceInBTC;
    @SerializedName("PriceInUSD")
    @Expose
    private double priceInUSD;
    @SerializedName("TradeValue")
    @Expose
    private double tradeValue;
    @SerializedName("CurrentIncome")
    @Expose
    private double currentIncome;
    @SerializedName("PurchaseType")
    @Expose
    private int purchaseType;
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("TCLMID")
    @Expose
    private int tclmid;
    @SerializedName("CoinLogoBase64Code")
    @Expose
    private String coinLogoBase64Code;

    public int getRegNo() {
        return regNo;
    }

    public void setRegNo(int regNo) {
        this.regNo = regNo;
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

    public double getReserved() {
        return reserved;
    }

    public void setReserved(double reserved) {
        this.reserved = reserved;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPendingDeposit() {
        return pendingDeposit;
    }

    public void setPendingDeposit(double pendingDeposit) {
        this.pendingDeposit = pendingDeposit;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getPriceInBTC() {
        return priceInBTC;
    }

    public void setPriceInBTC(double priceInBTC) {
        this.priceInBTC = priceInBTC;
    }

    public double getPriceInUSD() {
        return priceInUSD;
    }

    public void setPriceInUSD(double priceInUSD) {
        this.priceInUSD = priceInUSD;
    }

    public double getTradeValue() {
        return tradeValue;
    }

    public void setTradeValue(double tradeValue) {
        this.tradeValue = tradeValue;
    }

    public double getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(double currentIncome) {
        this.currentIncome = currentIncome;
    }

    public int getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCoinLogoBase64Code() {
        return coinLogoBase64Code;
    }

    public void setCoinLogoBase64Code(String coinLogoBase64Code) {
        this.coinLogoBase64Code = coinLogoBase64Code;
    }

    public int getTclmid() {
        return tclmid;
    }

    public void setTclmid(int tclmid) {
        this.tclmid = tclmid;
    }
}
