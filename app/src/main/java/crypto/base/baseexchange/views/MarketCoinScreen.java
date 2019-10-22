package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.MarketCoinBinder;
import crypto.base.baseexchange.databinding.LayoutMarketcoinListBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class MarketCoinScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutMarketcoinListBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_marketcoin_list, container, false);
        MarketCoinBinder binder = new MarketCoinBinder(getActivity(), binding, getFragmentManager());
        binding.setMarketCoinScreen(binder);

        return binding.getRoot();
    }
}
