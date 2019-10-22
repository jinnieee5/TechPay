package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.MarketCoin;
import crypto.base.baseexchange.binders.MarketPairingBinder;
import crypto.base.baseexchange.databinding.LayoutMarketPairingBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class MarketPairingScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutMarketPairingBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_market_pairing, container, false);

        Bundle bundle = getArguments();
        if (bundle!=null) {
            MarketCoin marketCoin = (MarketCoin) bundle.getSerializable("marketCoin");

            MarketPairingBinder binder = new MarketPairingBinder(getActivity(), binding, marketCoin);
            binding.setMarketPairingScreen(binder);
        }

        return binding.getRoot();
    }
}
