package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrderListReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("TPSPMID")
    @Expose
    private String tpspmid;
    @SerializedName("TCLMID")
    @Expose
    private String tclmid;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getTpspmid() {
        return tpspmid;
    }

    public void setTpspmid(String tpspmid) {
        this.tpspmid = tpspmid;
    }

    public String getTclmid() {
        return tclmid;
    }

    public void setTclmid(String tclmid) {
        this.tclmid = tclmid;
    }
}
