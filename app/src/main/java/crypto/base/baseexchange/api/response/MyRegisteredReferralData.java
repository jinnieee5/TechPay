package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyRegisteredReferralData {
    @SerializedName("TOTALMEMBERPERDAY")
    @Expose
    private int totalMemberPerDay;

    @SerializedName("LINKUPDATE")
    @Expose
    private String linkUpdate;

    public int getTotalMemberPerDay() {
        return totalMemberPerDay;
    }

    public void setTotalMemberPerDay(int totalMemberPerDay) {
        this.totalMemberPerDay = totalMemberPerDay;
    }

    public String getLinkUpdate() {
        return linkUpdate;
    }

    public void setLinkUpdate(String linkUpdate) {
        this.linkUpdate = linkUpdate;
    }
}
