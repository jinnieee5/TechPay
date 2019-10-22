package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginHistoryList {
    @SerializedName("ROWNUMBER")
    @Expose
    private int rowNumber;
    @SerializedName("NAME")
    @Expose
    private String name;
    @SerializedName("EMAILID")
    @Expose
    private String emailID;
    @SerializedName("DATE")
    @Expose
    private String date;
    @SerializedName("IPAddress")
    @Expose
    private String IPAddress;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("BROWSERNAME")
    @Expose
    private String browserName;
    @SerializedName("DEVICENAME")
    @Expose
    private String deviceName;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
