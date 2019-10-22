package crypto.base.baseexchange.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.OpenOrderBinder;
import crypto.base.baseexchange.databinding.LayoutOpenOrderBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class OpenOrderScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutOpenOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_open_order);

        String firstCoinTpspmid="1", secondCoinTclmid="2";
        Intent intent = getIntent();
        if (intent.hasExtra("firstCoinTpspmid") && intent.hasExtra("secondCoinTclmid")) {
            firstCoinTpspmid=intent.getStringExtra("firstCoinTpspmid");
            secondCoinTclmid=intent.getStringExtra("secondCoinTclmid");
        }
        OpenOrderBinder binder = new OpenOrderBinder(this,binding,firstCoinTpspmid,secondCoinTclmid);
        binding.setOpenOrderScreen(binder);
    }
}
