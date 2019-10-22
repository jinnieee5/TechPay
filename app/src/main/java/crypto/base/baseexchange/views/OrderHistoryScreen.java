package crypto.base.baseexchange.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.OrderHistoryBinder;
import crypto.base.baseexchange.databinding.LayoutOrderHistoryBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class OrderHistoryScreen extends BaseActivity {
    private LayoutOrderHistoryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_order_history);
        String firstCoinTpspmid="1", secondCoinTclmid="2";
        Intent intent = getIntent();
        if (intent.hasExtra("firstCoinTpspmid") && intent.hasExtra("secondCoinTclmid")) {
            firstCoinTpspmid=intent.getStringExtra("firstCoinTpspmid");
            secondCoinTclmid=intent.getStringExtra("secondCoinTclmid");
        }
        OrderHistoryBinder binder = new OrderHistoryBinder(this,binding,firstCoinTpspmid,secondCoinTclmid);
        binding.setOrderHistoryScreen(binder);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        binding.drawerLayout.closeDrawer(binding.drawerFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.drawerLayout.closeDrawer(binding.drawerFilter);
    }
}
