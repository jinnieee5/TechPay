package crypto.base.baseexchange.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.ReferralBinder;
import crypto.base.baseexchange.databinding.LayoutReferralBinding;
import crypto.base.baseexchange.utils.BaseFragment;

public class ReferralScreen extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutReferralBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_referral, container, false);
        ReferralBinder binder = new ReferralBinder(getActivity(), binding);
        binding.setReferralScreen(binder);
        return binding.getRoot();
    }
}
