package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactionFeeReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("TCLMID")
    @Expose
    private int tclmid;

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
}
