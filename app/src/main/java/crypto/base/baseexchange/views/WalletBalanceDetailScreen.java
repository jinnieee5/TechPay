package crypto.base.baseexchange.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.WalletBalancesList;
import crypto.base.baseexchange.binders.WalletBalanceDetailBinder;
import crypto.base.baseexchange.databinding.LayoutWalletBalancedetailBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class WalletBalanceDetailScreen extends BaseActivity {
    private WalletBalancesList walletBalance = new WalletBalancesList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutWalletBalancedetailBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_wallet_balancedetail);

        if (getIntent().hasExtra("WalletBalanceObject")) walletBalance = (WalletBalancesList) getIntent().getSerializableExtra("WalletBalanceObject");
        WalletBalanceDetailBinder binder = new WalletBalanceDetailBinder(this,binding,walletBalance);
        binding.setWalletBalanceDetailScreen(binder);
    }
}
