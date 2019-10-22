package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeInForceData {
    @SerializedName("TTIFMID")
    @Expose
    private int ttifmid;

    @SerializedName("TIMEINFORCE")
    @Expose
    private String timeInForce;

    public int getTtifmid() {
        return ttifmid;
    }

    public void setTtifmid(int ttifmid) {
        this.ttifmid = ttifmid;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }
}
