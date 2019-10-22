package crypto.base.baseexchange.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.LoginHistoryBinder;
import crypto.base.baseexchange.databinding.LayoutLoginHistoryBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class LoginHistoryScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutLoginHistoryBinding binding = DataBindingUtil.setContentView(this,R.layout.layout_login_history);
        LoginHistoryBinder binder = new LoginHistoryBinder(this,binding);
        binding.setLoginHistoryScreen(binder);
    }
}
