package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.WalletBalancesBinder;
import crypto.base.baseexchange.databinding.LayoutWalletBalancesBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class WalletBalancesScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutWalletBalancesBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_wallet_balances, container, false);
        WalletBalancesBinder binder = new WalletBalancesBinder(getActivity(), binding);
        binding.setWalletScreen(binder);

        return binding.getRoot();
    }
}
