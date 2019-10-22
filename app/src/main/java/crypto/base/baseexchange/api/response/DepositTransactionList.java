package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepositTransactionList {
    @SerializedName("COINCODE")
    @Expose
    private String coinCode;
    @SerializedName("GENERATEDDATEANDTIME")
    @Expose
    private String generatedDateAndTime;
    @SerializedName("FROMWALLET")
    @Expose
    private String fromWallet;
    @SerializedName("TOWALLET")
    @Expose
    private String toWallet;
    @SerializedName("AMOUNT")
    @Expose
    private double amount;
    @SerializedName("FROMADDRESS")
    @Expose
    private String fromAddress;
    @SerializedName("STATUS")
    @Expose
    private String status;
    @SerializedName("EXPLORERLINK")
    @Expose
    private String explorerLink;

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getGeneratedDateAndTime() {
        return generatedDateAndTime;
    }

    public void setGeneratedDateAndTime(String generatedDateAndTime) {
        this.generatedDateAndTime = generatedDateAndTime;
    }

    public String getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(String fromWallet) {
        this.fromWallet = fromWallet;
    }

    public String getToWallet() {
        return toWallet;
    }

    public void setToWallet(String toWallet) {
        this.toWallet = toWallet;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExplorerLink() {
        return explorerLink;
    }

    public void setExplorerLink(String explorerLink) {
        this.explorerLink = explorerLink;
    }
}
