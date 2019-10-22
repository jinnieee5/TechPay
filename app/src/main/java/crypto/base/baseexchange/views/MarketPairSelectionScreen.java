package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.MarketPairSelectionBinder;
import crypto.base.baseexchange.databinding.LayoutMarketPairselectionBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class MarketPairSelectionScreen extends BaseFragment {
    private static String callFrom="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutMarketPairselectionBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_market_pairselection, container, false);
        MarketPairSelectionBinder binder = new MarketPairSelectionBinder(getActivity(), binding, callFrom);
        binding.setMarketPairSelectionScreen(binder);

        /*Bundle bundle = getArguments();
        if (bundle!=null) {
            String firstCoinCode = bundle.getString("firstCoinCode");
            int pairingID = bundle.getInt("pairingID");
            MarketPairingBinder binder = new MarketPairingBinder(getActivity(), binding, firstCoinCode, pairingID);
            binding.setMarketPairingScreen(binder);
        }*/

        return binding.getRoot();
    }

    public static MarketPairSelectionScreen newInstance(String callFrom1, String firstCoinCode, int pairingID) {
        callFrom = callFrom1;
        MarketPairSelectionScreen fragmentFirst = new MarketPairSelectionScreen();
        Bundle args = new Bundle();
        args.putInt("pairingID", pairingID);
        args.putString("firstCoinCode", firstCoinCode);
        fragmentFirst.setArguments(args);

        Log.e("firstCoinCode"," - "+firstCoinCode);
        return fragmentFirst;
    }
}
