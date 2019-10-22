package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.WalletWithdrawalHistoryBinder;
import crypto.base.baseexchange.databinding.LayoutWalletWithdrawalhistoryBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class WalletWithdrawalHistoryScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutWalletWithdrawalhistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_wallet_withdrawalhistory, container, false);
        WalletWithdrawalHistoryBinder binder = new WalletWithdrawalHistoryBinder(getActivity(), binding);
        binding.setWalletWithdrawalScreen(binder);

        return binding.getRoot();
    }
}
