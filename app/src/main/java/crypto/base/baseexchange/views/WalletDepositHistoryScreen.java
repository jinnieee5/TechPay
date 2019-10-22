package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.WalletDepositHistoryBinder;
import crypto.base.baseexchange.databinding.LayoutWalletDeposithistoryBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class WalletDepositHistoryScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutWalletDeposithistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_wallet_deposithistory, container, false);
        WalletDepositHistoryBinder binder = new WalletDepositHistoryBinder(getActivity(), binding);
        binding.setWalletDepositScreen(binder);

        return binding.getRoot();
    }
}
