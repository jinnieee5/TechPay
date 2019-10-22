package crypto.base.baseexchange.interfaces;

public interface CoinMarketListHeaderListener {
    void onMarketCoinClick(String firstCoinCode, int pairingID, int tpspmid, boolean spinnerSelect);
}
