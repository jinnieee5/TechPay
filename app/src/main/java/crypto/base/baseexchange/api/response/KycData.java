package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KycData {
    @SerializedName("SelfPhotoGraphWithIdCard")
    @Expose
    private String selfPhotoGraphWithIdCard;
    @SerializedName("IdCardFrontImage")
    @Expose
    private String idCardFrontImage;
    @SerializedName("IdCardBackImage")
    @Expose
    private String idCardBackImage;
    @SerializedName("PanCardImage")
    @Expose
    private String panCardImage;
    @SerializedName("PANNO")
    @Expose
    private String panNo;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("IdProofType")
    @Expose
    private String idProofType;
    @SerializedName("IdProofRefNo")
    @Expose
    private String idProofRefNo;
    @SerializedName("KycStatus")
    @Expose
    private int kycStatus;

    public String getSelfPhotoGraphWithIdCard() {
        return selfPhotoGraphWithIdCard;
    }

    public void setSelfPhotoGraphWithIdCard(String selfPhotoGraphWithIdCard) {
        this.selfPhotoGraphWithIdCard = selfPhotoGraphWithIdCard;
    }

    public String getIdCardFrontImage() {
        return idCardFrontImage;
    }

    public void setIdCardFrontImage(String idCardFrontImage) {
        this.idCardFrontImage = idCardFrontImage;
    }

    public String getIdCardBackImage() {
        return idCardBackImage;
    }

    public void setIdCardBackImage(String idCardBackImage) {
        this.idCardBackImage = idCardBackImage;
    }

    public String getPanCardImage() {
        return panCardImage;
    }

    public void setPanCardImage(String panCardImage) {
        this.panCardImage = panCardImage;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getIdProofRefNo() {
        return idProofRefNo;
    }

    public void setIdProofRefNo(String idProofRefNo) {
        this.idProofRefNo = idProofRefNo;
    }

    public int getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(int kycStatus) {
        this.kycStatus = kycStatus;
    }
}
