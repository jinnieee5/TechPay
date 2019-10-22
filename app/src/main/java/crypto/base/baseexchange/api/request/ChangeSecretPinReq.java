package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeSecretPinReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("OldSecretPin")
    @Expose
    private String oldSecretPin;
    @SerializedName("OldSecretBackUpKey")
    @Expose
    private String oldSecretBackUpKey;
    @SerializedName("NewSecretPin")
    @Expose
    private String newSecretPin;
    @SerializedName("NewSecretBackUpKey")
    @Expose
    private String newSecretBackUpKey;
    @SerializedName("Mode")
    @Expose
    private int mode;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getOldSecretPin() {
        return oldSecretPin;
    }

    public void setOldSecretPin(String oldSecretPin) {
        this.oldSecretPin = oldSecretPin;
    }

    public String getOldSecretBackUpKey() {
        return oldSecretBackUpKey;
    }

    public void setOldSecretBackUpKey(String oldSecretBackUpKey) {
        this.oldSecretBackUpKey = oldSecretBackUpKey;
    }

    public String getNewSecretPin() {
        return newSecretPin;
    }

    public void setNewSecretPin(String newSecretPin) {
        this.newSecretPin = newSecretPin;
    }

    public String getNewSecretBackUpKey() {
        return newSecretBackUpKey;
    }

    public void setNewSecretBackUpKey(String newSecretBackUpKey) {
        this.newSecretBackUpKey = newSecretBackUpKey;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
