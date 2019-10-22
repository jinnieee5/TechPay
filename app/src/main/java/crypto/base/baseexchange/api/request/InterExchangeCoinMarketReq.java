package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterExchangeCoinMarketReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("PURCHASETYPE")
    @Expose
    private int purchaseType;
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("PURCHASETYPEID")
    @Expose
    private int purchaseTypeID;
    @SerializedName("TCLMID")
    @Expose
    private int tclmid;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
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

    public int getPurchaseTypeID() {
        return purchaseTypeID;
    }

    public void setPurchaseTypeID(int purchaseTypeID) {
        this.purchaseTypeID = purchaseTypeID;
    }

    public int getTclmid() {
        return tclmid;
    }

    public void setTclmid(int tclmid) {
        this.tclmid = tclmid;
    }
}
