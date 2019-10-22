package crypto.base.baseexchange.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.TicketListBinder;
import crypto.base.baseexchange.databinding.LayoutTicketListBinding;
import crypto.base.baseexchange.utils.BaseActivity;

public class TicketListScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutTicketListBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_ticket_list);
        TicketListBinder binder = new TicketListBinder(this,binding);
        binding.setSetTicketListScreen(binder);
    }
}
