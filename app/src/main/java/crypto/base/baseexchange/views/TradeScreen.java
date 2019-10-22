package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.TradeScreenBinder;
import crypto.base.baseexchange.databinding.LayoutTradeMainBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class TradeScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutTradeMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_trade_main, container, false);

        String viewPagerSelection="buy";
        String headerSelection="";
        String spinnerSelection="";
        Bundle bundle = getArguments();
        if (bundle!=null) {
            viewPagerSelection = bundle.getString("viewPagerSelection","buy");
            headerSelection = bundle.getString("headerSelection","");
            spinnerSelection = bundle.getString("spinnerSelection","");
        }

        TradeScreenBinder binder = new TradeScreenBinder(getActivity(), binding, getFragmentManager(), viewPagerSelection, headerSelection, spinnerSelection);
        binding.setTradeScreen(binder);

        return binding.getRoot();
    }
}
