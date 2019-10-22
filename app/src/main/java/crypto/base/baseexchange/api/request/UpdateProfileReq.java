package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("FNAME")
    @Expose
    private String fName;
    @SerializedName("LNAME")
    @Expose
    private String lName;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("ADDRESS")
    @Expose
    private String address;
    @SerializedName("CID")
    @Expose
    private String cID;
    @SerializedName("PINCODE")
    @Expose
    private String pincode;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCID() {
        return cID;
    }

    public void setCID(String cID) {
        this.cID = cID;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
