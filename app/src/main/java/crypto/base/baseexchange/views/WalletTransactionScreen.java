package crypto.base.baseexchange.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.WalletTransactionBinder;
import crypto.base.baseexchange.databinding.LayoutWalletTransactionBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class WalletTransactionScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutWalletTransactionBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_wallet_transaction);
        WalletTransactionBinder binder = new WalletTransactionBinder(this,binding,getSupportFragmentManager());
        binding.setWalletTransactionScreen(binder);
    }
}
