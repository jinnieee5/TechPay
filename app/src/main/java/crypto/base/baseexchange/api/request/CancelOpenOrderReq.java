package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelOpenOrderReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("TCCBTMID")
    @Expose
    private String tccbtmid;
    @SerializedName("TCCSTMID")
    @Expose
    private String tccstmid;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTccbtmid() {
        return tccbtmid;
    }

    public void setTccbtmid(String tccbtmid) {
        this.tccbtmid = tccbtmid;
    }

    public String getTccstmid() {
        return tccstmid;
    }

    public void setTccstmid(String tccstmid) {
        this.tccstmid = tccstmid;
    }
}
