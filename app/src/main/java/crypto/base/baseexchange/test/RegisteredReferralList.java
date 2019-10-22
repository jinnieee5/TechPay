package crypto.base.baseexchange.test;

public class RegisteredReferralList {
    private String date, registeredReferral;

    public RegisteredReferralList(String date, String registeredReferral) {
        this.date = date;
        this.registeredReferral = registeredReferral;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRegisteredReferral() {
        return registeredReferral;
    }

    public void setRegisteredReferral(String registeredReferral) {
        this.registeredReferral = registeredReferral;
    }
}
