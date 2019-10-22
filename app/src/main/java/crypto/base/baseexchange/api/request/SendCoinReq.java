package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendCoinReq {
    @SerializedName("DestinationAddress")
    @Expose
    private String destinationAddress;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("SecretPin")
    @Expose
    private String secretPin;
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("TCLMID")
    @Expose
    private String tCLMID;

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSecretPin() {
        return secretPin;
    }

    public void setSecretPin(String secretPin) {
        this.secretPin = secretPin;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getTCLMID() {
        return tCLMID;
    }

    public void setTCLMID(String tCLMID) {
        this.tCLMID = tCLMID;
    }
}
