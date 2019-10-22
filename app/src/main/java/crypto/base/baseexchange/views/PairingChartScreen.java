package crypto.base.baseexchange.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TradePairingCoinDetail;
import crypto.base.baseexchange.binders.PairingChartBinder;
import crypto.base.baseexchange.databinding.LayoutPairingChartBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class PairingChartScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutPairingChartBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_pairing_chart);

        TradePairingCoinDetail pairingCoinDetail;
        String firstCoinCode, secondCoinCode;
        Intent intent = getIntent();
        if (intent.hasExtra("pairingCoinDetail") && intent.hasExtra("firstCoinCode") && intent.hasExtra("secondCoinCode")) {
            pairingCoinDetail = (TradePairingCoinDetail) intent.getSerializableExtra("pairingCoinDetail");
            firstCoinCode = intent.getStringExtra("firstCoinCode");
            secondCoinCode = intent.getStringExtra("secondCoinCode");
            PairingChartBinder binder = new PairingChartBinder(this,binding,pairingCoinDetail,firstCoinCode,secondCoinCode);
            binding.setPairingChartScreen(binder);
        }
    }
}
