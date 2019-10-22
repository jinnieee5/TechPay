package crypto.base.baseexchange.binders;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import crypto.base.baseexchange.databinding.LayoutAccountMainBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.CreateTicketScreen;
import crypto.base.baseexchange.views.HomeScreen;
import crypto.base.baseexchange.views.LoginHistoryScreen;
import crypto.base.baseexchange.views.MyAccountScreen;
import crypto.base.baseexchange.views.SettingsScreen;

public class AccountMainBinder {
    private Context context;
    private LayoutAccountMainBinding binding;

    public AccountMainBinder(Context context, LayoutAccountMainBinding binding) {
        this.context = context;
        this.binding = binding;

        callLinks();
        binding.tvEmailID.setText(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.EmailID));
    }

    private void callLinks() {
        binding.llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CreateTicketScreen.class));
            }
        });

        binding.llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyAccountScreen.class));
            }
        });

        binding.llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SettingsScreen.class));
            }
        });

        binding.llLoginHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LoginHistoryScreen.class));
            }
        });

        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                //SharedPrefUtils.logoutFromPrefs(context);

                BaseUtils.customToast(context,"logging out...");

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                },2000);
            }
        });
    }
}
