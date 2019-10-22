package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.response.MarketCoin;
import crypto.base.baseexchange.databinding.LayoutMarketcoinListBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.MarketPairingScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MarketCoinBinder {
    private Context context;
    private LayoutMarketcoinListBinding binding;
    private FragmentManager fragmentManager;
    private ProgressDialog marketListProgressDialog;

    public MarketCoinBinder(Context context, LayoutMarketcoinListBinding binding, FragmentManager fragmentManager) {
        this.context = context;
        this.binding = binding;
        this.fragmentManager = fragmentManager;

        setMarketHeader();
        getCoinMarketList();
    }

    private void setMarketHeader() {
        TextView tv_header = binding.layoutMarketHeader.findViewById(R.id.tv_header);
        ImageView iv_back = binding.layoutMarketHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutMarketHeader.findViewById(R.id.iv_endIcon);

        tv_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.GONE);
        iv_endIcon.setVisibility(View.GONE);

        tv_header.setText(context.getString(R.string.header_markets));

        iv_endIcon.setImageDrawable(context.getDrawable(R.drawable.search));
        iv_endIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setViewPagerTabs(final List<MarketCoin> marketCoinList) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);

        for (MarketCoin marketCoin : marketCoinList) {
            adapter.addFragment(new Fragment(), marketCoin.getCoinCode());
        }

        binding.vpMarketCoins.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.vpMarketCoins.setOffscreenPageLimit(0);
        binding.tabsMarketCoins.setupWithViewPager(binding.vpMarketCoins);

        int i=0;
        for (MarketCoin marketCoin : marketCoinList) {
            TextView tab = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            tab.setText(marketCoin.getCoinCode().toUpperCase());
            tab.setTextColor(context.getColor(R.color.white));
            tab.setTextSize(14);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tab.setTypeface(context.getResources().getFont(R.font.gotham_rounded_medium));
            }
            binding.tabsMarketCoins.getTabAt(i).setCustomView(tab);
            i++;
        }

        binding.tabsMarketCoins.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i=0;
                for (MarketCoin marketCoin : marketCoinList) {
                    if (tab.getPosition()==i) {
                        MarketPairingScreen marketPairingScreen = new MarketPairingScreen();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("marketCoin",marketCoin);
                        marketPairingScreen.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.frame_marketContent,marketPairingScreen).commit();
                    }
                    i++;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) { super(manager); }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() { return mFragmentList.size(); }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title); }

        @Override
        public CharSequence getPageTitle(int position) { return mFragmentTitleList.get(position); }


    }

    private void getCoinMarketList() {
        BaseUtils.hideKeypad((Activity) context);
        marketListProgressDialog = BaseUtils.showProgressDialog(context,"Please wait");
        marketListProgressDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken("");

        Single<JsonObject> observable = authApiHelper.getInterExchangeCoinList(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject coinLiveRatesRes) {
                        marketListProgressDialog.dismiss();
                        if (coinLiveRatesRes.get("Flag").getAsString().equalsIgnoreCase("success")) {
                            JsonArray jArray = coinLiveRatesRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<MarketCoin>>() {}.getType();
                            List<MarketCoin> marketCoinList = new Gson().fromJson(jArray, listType);

                            MarketPairingScreen marketPairingScreen = new MarketPairingScreen();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("marketCoin",marketCoinList.get(0));
                            marketPairingScreen.setArguments(bundle);
                            fragmentManager.beginTransaction().replace(R.id.frame_marketContent,marketPairingScreen).commit();
                            setViewPagerTabs(marketCoinList);

                        } else Log.e("Dashboard", "empty coin-live-rate-list, error in API");
                    }
                    @Override
                    public void onError(Throwable e) {
                        marketListProgressDialog.dismiss();
                        BaseUtils.customToast(context,context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
