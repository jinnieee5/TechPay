package crypto.base.baseexchange.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.TradeOrderHistoryBinder;
import crypto.base.baseexchange.databinding.LayoutTradeOrderhistoryBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class TradeOrderHistoryScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutTradeOrderhistoryBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_trade_orderhistory);

        String firstCoinTpspmid="", secondCoinTclmid="", firstCoinCode="", secondCoinCode="";
        Intent intent = getIntent();
        if (intent.hasExtra("firstCoinTpspmid") && intent.hasExtra("secondCoinTclmid")) {
            firstCoinTpspmid = intent.getStringExtra("firstCoinTpspmid");
            secondCoinTclmid = intent.getStringExtra("secondCoinTclmid");
            firstCoinCode = intent.getStringExtra("firstCoinCode");
            secondCoinCode = intent.getStringExtra("secondCoinCode");

            TradeOrderHistoryBinder binder = new TradeOrderHistoryBinder(this,binding,firstCoinTpspmid,secondCoinTclmid);
            binding.setTradeOrderHistoryScreen(binder);

            binding.tvSecondCoinQty.setText(String.format("Qty %s", secondCoinCode));
            binding.tvFirstCoinPrice.setText(String.format("Total %s", firstCoinCode));
        }
    }
}
