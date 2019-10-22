package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateTicketReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("CategoryId")
    @Expose
    private String categoryId;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ImgPreviewFirst")
    @Expose
    private String imgPreviewFirst;
    @SerializedName("ImgPreviewSecond")
    @Expose
    private String imgPreviewSecond;
    @SerializedName("ImgPreviewThird")
    @Expose
    private String imgPreviewThird;
    @SerializedName("Priority")
    @Expose
    private String priority;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgPreviewFirst() {
        return imgPreviewFirst;
    }

    public void setImgPreviewFirst(String imgPreviewFirst) {
        this.imgPreviewFirst = imgPreviewFirst;
    }

    public String getImgPreviewSecond() {
        return imgPreviewSecond;
    }

    public void setImgPreviewSecond(String imgPreviewSecond) {
        this.imgPreviewSecond = imgPreviewSecond;
    }

    public String getImgPreviewThird() {
        return imgPreviewThird;
    }

    public void setImgPreviewThird(String imgPreviewThird) {
        this.imgPreviewThird = imgPreviewThird;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
