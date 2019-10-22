package crypto.base.baseexchange.interfaces;

import crypto.base.baseexchange.api.response.TradeOpenOrderList;

public interface TradeOpenOrdersListener {
    void onTradeOpenOrdersClick(TradeOpenOrderList openOrder, String mode);
}
