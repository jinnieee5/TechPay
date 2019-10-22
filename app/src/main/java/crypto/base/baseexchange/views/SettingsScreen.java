package crypto.base.baseexchange.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.SettingsBinder;
import crypto.base.baseexchange.databinding.LayoutSettingsBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class SettingsScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutSettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_settings);
        SettingsBinder binder = new SettingsBinder(this,binding);
        binding.setSettingsScreen(binder);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
