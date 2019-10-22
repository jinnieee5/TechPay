package crypto.base.baseexchange.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.MarketSelectionBinder;
import crypto.base.baseexchange.databinding.LayoutMarketSelectionBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class MarketSelectionScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutMarketSelectionBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_market_selection);

        String callFrom = getIntent().getStringExtra("callFrom");
        MarketSelectionBinder binder = new MarketSelectionBinder(this, binding, getSupportFragmentManager(), callFrom);
        binding.setMarketSelectionScreen(binder);
    }
}
