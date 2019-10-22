package crypto.base.baseexchange.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinPairingList {
    @SerializedName("COINID")
    @Expose
    private int coinID;
    @SerializedName("ICOID")
    @Expose
    private int icoID;
    @SerializedName("PURCHASETYPEID")
    @Expose
    private int purchaseTypeID;
    @SerializedName("TILMID")
    @Expose
    private int tilmid;
    @SerializedName("TCLMID")
    @Expose
    private int tclmid;
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("COINNAME")
    @Expose
    private String coinName;
    @SerializedName("COINCODE")
    @Expose
    private String coinCode;
    @SerializedName("COINLOGOBASE64CODE")
    @Expose
    private String coinLogoBase64Code;
    @SerializedName("PRICE")
    @Expose
    private double price;
    @SerializedName("VOLUME")
    @Expose
    private double volume;
    @SerializedName("CHANGEPRICE")
    @Expose
    private double changePrice;
    @SerializedName("SOURCE")
    @Expose
    private String source;
    @SerializedName("VOLUME24H")
    @Expose
    private double volume24H;
    @SerializedName("VOLUMEPERCENTAGE")
    @Expose
    private int volumePercentange;

    public int getCoinID() {
        return coinID;
    }

    public void setCoinID(int coinID) {
        this.coinID = coinID;
    }

    public int getIcoID() {
        return icoID;
    }

    public void setIcoID(int icoID) {
        this.icoID = icoID;
    }

    public int getPurchaseTypeID() {
        return purchaseTypeID;
    }

    public void setPurchaseTypeID(int purchaseTypeID) {
        this.purchaseTypeID = purchaseTypeID;
    }

    public int getTilmid() {
        return tilmid;
    }

    public void setTilmid(int tilmid) {
        this.tilmid = tilmid;
    }

    public int getTclmid() {
        return tclmid;
    }

    public void setTclmid(int tclmid) {
        this.tclmid = tclmid;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        this.ID = ID;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinCode(){
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinLogoBase64Code() {
        return coinLogoBase64Code;
    }

    public void setCoinLogoBase64Code(String coinLogoBase64Code) {
        this.coinLogoBase64Code = coinLogoBase64Code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(double changePrice) {
        this.changePrice = changePrice;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getVolume24H() {
        return volume24H;
    }

    public void setVolume24H(double volume24H) {
        this.volume24H = volume24H;
    }

    public int getVolumePercentange() {
        return volumePercentange;
    }

    public void setVolumePercentange(int volumePercentange) {
        this.volumePercentange = volumePercentange;
    }
}
