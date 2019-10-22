package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketList {
    @SerializedName("NUMBER")
    @Expose
    private int number;
    @SerializedName("TMID")
    @Expose
    private int tmid;
    @SerializedName("TICKETNO")
    @Expose
    private String ticketNo;
    @SerializedName("CREATEDATE")
    @Expose
    private String createDate;
    @SerializedName("CATEGORYNAME")
    @Expose
    private String categoryName;
    @SerializedName("LASTUPDATEDATE")
    @Expose
    private String lastUpdateDate;
    @SerializedName("SUBJECT")
    @Expose
    private String subject;
    @SerializedName("STATUS")
    @Expose
    private String status;
    @SerializedName("MESSAGE")
    @Expose
    private String message;
    @SerializedName("TMIMAGE")
    @Expose
    private String tmImage;
    @SerializedName("PRIORITY")
    @Expose
    private String priority;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTmid() {
        return tmid;
    }

    public void setTmid(int tmid) {
        this.tmid = tmid;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
