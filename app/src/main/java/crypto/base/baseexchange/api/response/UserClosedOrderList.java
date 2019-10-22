package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserClosedOrderList {
    @SerializedName("ROWNUMBER")
    @Expose
    private int rowNumber;
    @SerializedName("ORDERDATE")
    @Expose
    private String orderDate;
    @SerializedName("MARKET")
    @Expose
    private String market;
    @SerializedName("TYPE")
    @Expose
    private String type;
    @SerializedName("BID")
    @Expose
    private double bid;
    @SerializedName("FILLED")
    @Expose
    private double filled;
    @SerializedName("UNITSTOTAL")
    @Expose
    private double unitsTotal;
    @SerializedName("ACTUALRATE")
    @Expose
    private double actualRate;
    @SerializedName("MARKETPRICE")
    @Expose
    private double marketPrice;
    @SerializedName("APPROVALSTATUS")
    @Expose
    private String approvalStatus;
    @SerializedName("APPROVALDATE")
    @Expose
    private String approvalDate;
    @SerializedName("TRADESTATUS")
    @Expose
    private Object tradetatus;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getFilled() {
        return filled;
    }

    public void setFilled(double filled) {
        this.filled = filled;
    }

    public double getUnitsTotal() {
        return unitsTotal;
    }

    public void setUnitsTotal(double unitsTotal) {
        this.unitsTotal = unitsTotal;
    }

    public double getActualRate() {
        return actualRate;
    }

    public void setActualRate(double actualRate) {
        this.actualRate = actualRate;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Object getTradetatus() {
        return tradetatus;
    }

    public void setTradetatus(Object tradetatus) {
        this.tradetatus = tradetatus;
    }
}
