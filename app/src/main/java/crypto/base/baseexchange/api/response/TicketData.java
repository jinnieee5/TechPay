package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketData {
    @SerializedName("TICKETNO")
    @Expose
    private String ticketNo;
    @SerializedName("CATEGORYNAME")
    @Expose
    private String categoryName;
    @SerializedName("SUBJECT")
    @Expose
    private String subject;
    @SerializedName("DATE")
    @Expose
    private String date;
    @SerializedName("LASTUPDATE")
    @Expose
    private String lastUpdate;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("TMIMAGE")
    @Expose
    private String tmImage;
    @SerializedName("STATUS")
    @Expose
    private String status;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTmImage() {
        return tmImage;
    }

    public void setTmImage(String tmImage) {
        this.tmImage = tmImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
