package crypto.base.baseexchange.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.binders.HomeBinder;
import crypto.base.baseexchange.databinding.LayoutHomeBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;

public class HomeScreen extends BaseActivity {
    private LayoutHomeBinding binding;
    private static long sayBackPress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_home);
        HomeBinder binder = new HomeBinder(this,binding);
        binding.setHomeScreen(binder);
        hideKeypad();

        Intent it = getIntent();
        String openFragmentName="";
        if (it.hasExtra("openFragmentName")) openFragmentName=it.getStringExtra("openFragmentName");

        if (openFragmentName.isEmpty()) setDefaultFragment();
        else if (openFragmentName.equalsIgnoreCase("market")) setDefaultFragment();
        else if (openFragmentName.equalsIgnoreCase("trades")) {
            binding.bottomMenu.getMenu().findItem(R.id.menu_trades).setChecked(true);

            if (getIntent().hasExtra("viewPagerSelection") && getIntent().hasExtra("headerSelection") && getIntent().hasExtra("spinnerSelection")) {
                TradeScreen tradeScreen = new TradeScreen();
                Bundle bundle = new Bundle();
                bundle.putString("viewPagerSelection",getIntent().getStringExtra("viewPagerSelection"));
                bundle.putString("headerSelection",getIntent().getStringExtra("headerSelection"));
                bundle.putString("spinnerSelection",getIntent().getStringExtra("spinnerSelection"));
                tradeScreen.setArguments(bundle);
                setFragment(tradeScreen);
            } else setFragment(new TradeScreen());

        } else if (openFragmentName.equalsIgnoreCase("wallets")) {
            if (SharedPrefUtils.getFromPrefs(HomeScreen.this, SharedPrefUtils.isLogin).equals("0")) {
                Intent intent = new Intent(HomeScreen.this,EnterScreen.class);
                startActivity(intent);
            } else {
                binding.bottomMenu.getMenu().findItem(R.id.menu_wallets).setChecked(true);
                setFragment(new WalletBalancesScreen());
            }
        } else if (openFragmentName.equalsIgnoreCase("account")) {
            if (SharedPrefUtils.getFromPrefs(HomeScreen.this, SharedPrefUtils.isLogin).equals("0")) {
                Intent intent = new Intent(HomeScreen.this,EnterScreen.class);
                startActivity(intent);
            } else {
                binding.bottomMenu.getMenu().findItem(R.id.menu_account).setChecked(true);
                setFragment(new AccountMainScreen());
            }
        }

        String pin="";
        if (it.hasExtra("pin")) pin=it.getStringExtra("pin");
        Log.e("home-screen","pin -> "+pin);

        setUpBottomMenu();
    }

    private void setDefaultFragment() {
        binding.bottomMenu.getMenu().findItem(R.id.menu_markets).setChecked(true);
        setFragment(new MarketCoinScreen());
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main,fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (f instanceof MarketCoinScreen || f instanceof TradeScreen || f instanceof WalletBalancesScreen || f instanceof AccountMainScreen) {
            if (sayBackPress + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            }
            else {
                BaseUtils.customToast(getBaseContext(),"Press once again to exit !");
                sayBackPress = System.currentTimeMillis(); } }
        else getSupportFragmentManager().popBackStack();
    }

    private void setUpBottomMenu() {
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_main);
                switch (item.getItemId()) {
                    case R.id.menu_markets:
                        if (!(f instanceof MarketCoinScreen)) setFragment(new MarketCoinScreen());
                        break;
                    case R.id.menu_trades:
                        if (!(f instanceof TradeScreen)) setFragment(new TradeScreen());
                        break;
                    case R.id.menu_wallets:
                        if (!(f instanceof WalletBalancesScreen)) {
                            if (SharedPrefUtils.getFromPrefs(HomeScreen.this, SharedPrefUtils.isLogin).equals("0")) {
                                Intent intent = new Intent(HomeScreen.this,EnterScreen.class);
                                startActivity(intent);
                            } else setFragment(new WalletBalancesScreen());
                        }
                        break;
                    case R.id.menu_account:
                        if (!(f instanceof AccountMainScreen)) {
                            if (SharedPrefUtils.getFromPrefs(HomeScreen.this, SharedPrefUtils.isLogin).equals("0")) {
                                Intent intent = new Intent(HomeScreen.this,EnterScreen.class);
                                startActivity(intent);
                            } else setFragment(new AccountMainScreen());
                        }
                        break;
                    case R.id.menu_referral:
                        if(!(f instanceof ReferralScreen)) {
                            if(SharedPrefUtils.getFromPrefs(HomeScreen.this, SharedPrefUtils.isLogin).equals("0")){
                                Intent intent = new Intent(HomeScreen.this, EnterScreen.class);
                                startActivity(intent);
                            }else setFragment(new ReferralScreen());
                        }

                        break;
                }
                return true;
            }
        });
    }
}
