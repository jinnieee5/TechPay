package crypto.base.baseexchange.interfaces;

import crypto.base.baseexchange.utils.CoinPairSelectionData;

public interface MarketPairSelectionListener {
    void onPairSelectionClick(CoinPairSelectionData coinPairSelectionData);
}
