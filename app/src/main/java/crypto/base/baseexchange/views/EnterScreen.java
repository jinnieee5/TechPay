package crypto.base.baseexchange.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;

public class EnterScreen extends BaseActivity {
    private LinearLayout ll_main;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_enter_screen);
        hideKeypad();

        ll_main = findViewById(R.id.ll_main);
        ImageView iv_cross = findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FrameLayout frame_createWallet = findViewById(R.id.frame_createWallet);
        frame_createWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (!isOnline()) BaseUtils.showNoInternetSnackbar(ll_main,getResources().getString(R.string.error_noInternet));
                else startActivity(new Intent(EnterScreen.this,CreateWalletScreen.class));
            }
        });

        FrameLayout frame_login =

    findViewById(R.id.frame_login);
        frame_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (!isOnline()) BaseUtils.showNoInternetSnackbar(ll_main,getResources().getString(R.string.error_noInternet));
                else {
                    Intent intent = new Intent(EnterScreen.this,LoginScreen.class);
                    startActivity(intent);
                }
            }
        });
    }
}
