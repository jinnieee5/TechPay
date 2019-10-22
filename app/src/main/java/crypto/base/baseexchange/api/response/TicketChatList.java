package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketChatList {
    @SerializedName("MESSAGE")
    @Expose
    private String message;
    @SerializedName("DATE")
    @Expose
    private String date;
    @SerializedName("TRID")
    @Expose
    private int trid;
    @SerializedName("ADMINUSER")
    @Expose
    private String adminUser;
    @SerializedName("IMAGEURL")
    @Expose
    private String imageURL;
    @SerializedName("FROMTYPE")
    @Expose
    private String fromType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTrid() {
        return trid;
    }

    public void setTrid(int trid) {
        this.trid = trid;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
}
