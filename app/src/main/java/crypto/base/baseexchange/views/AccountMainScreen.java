package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.AccountMainBinder;
import crypto.base.baseexchange.databinding.LayoutAccountMainBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class AccountMainScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutAccountMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_account_main, container, false);
        AccountMainBinder binder = new AccountMainBinder(getActivity(), binding);
        binding.setAccountMainScreen(binder);

        return binding.getRoot();
    }
}
