package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawalTransactionList {
    @SerializedName("COINCODE")
    @Expose
    private String coinCode;
    @SerializedName("COINIMAGE")
    @Expose
    private String coinImage;
    @SerializedName("TOWALLETADDRESS")
    @Expose
    private String toWalletAddress;
    @SerializedName("AMOUNT")
    @Expose
    private double amount;
    @SerializedName("TransactionFee")
    @Expose
    private double transactionFee;
    @SerializedName("NetPayable")
    @Expose
    private double netPayable;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("ActivityDateAndTime")
    @Expose
    private String activityDateAndTime;
    @SerializedName("STATUS")
    @Expose
    private String status;

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinImage() {
        return coinImage;
    }

    public void setCoinImage(String coinImage) {
        this.coinImage = coinImage;
    }

    public String getToWalletAddress() {
        return toWalletAddress;
    }

    public void setToWalletAddress(String toWalletAddress) {
        this.toWalletAddress = toWalletAddress;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        this.transactionFee = transactionFee;
    }

    public double getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(double netPayable) {
        this.netPayable = netPayable;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getActivityDateAndTime() {
        return activityDateAndTime;
    }

    public void setActivityDateAndTime(String activityDateAndTime) {
        this.activityDateAndTime = activityDateAndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
