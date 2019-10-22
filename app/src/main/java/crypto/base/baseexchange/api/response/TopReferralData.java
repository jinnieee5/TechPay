package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopReferralData {
    @SerializedName("TRADERSACCOUNTNUMBER")
    @Expose
    private String tradesAccountNumber;

    @SerializedName("TOTALREFERRALMEMBER")
    @Expose
    private int totalReferralMember;

    public String getTradesAccountNumber() {
        return tradesAccountNumber;
    }

    public void setTradesAccountNumber(String tradesAccountNumber) {
        this.tradesAccountNumber = tradesAccountNumber;
    }

    public int getTotalReferralMember() {
        return totalReferralMember;
    }

    public void setTotalReferralMember(int totalReferralMember) {
        this.totalReferralMember = totalReferralMember;
    }
}
