package crypto.base.baseexchange.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyTicketReq {
    @SerializedName("LoginToken")
    @Expose
    private String loginToken;
    @SerializedName("TMID")
    @Expose
    private String tmid;
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

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getTmid() {
        return tmid;
    }

    public void setTmid(String tmid) {
        this.tmid = tmid;
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
}
