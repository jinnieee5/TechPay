package crypto.base.baseexchange.binders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.WalletBalanceListAdpater;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.response.WalletBalancesList;
import crypto.base.baseexchange.databinding.LayoutWalletBalancesBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import crypto.base.baseexchange.views.HomeScreen;
import crypto.base.baseexchange.views.WalletTransactionScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletBalancesBinder {
    private Context context;
    private LayoutWalletBalancesBinding binding;
    private ProgressDialog dialog;
    private List<WalletBalancesList> walletBalancesList;

    public WalletBalancesBinder(final Context context, final LayoutWalletBalancesBinding binding) {
        this.context = context;
        this.binding = binding;

        setWalletHeader();
        getWalletBalances();

        //for first time
        binding.llFilter.setTag("hide");
        binding.ivFilter.setImageDrawable(context.getDrawable(R.drawable.eye_hide));
        binding.tvFilter.setText(context.getString(R.string.header_hideLowWalletBalances_underline));

        binding.llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.llFilter.getTag().toString().equalsIgnoreCase("show")) {
                    binding.ivFilter.setImageDrawable(context.getDrawable(R.drawable.eye_hide));
                    binding.tvFilter.setText(context.getString(R.string.header_hideLowWalletBalances_underline));
                    binding.llFilter.setTag("hide");

                    if (!walletBalancesList.isEmpty()) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                        binding.rvWalletBalanceList.setLayoutManager(layoutManager);
                        binding.rvWalletBalanceList.setAdapter(new WalletBalanceListAdpater(context, walletBalancesList));
                    }

                } else if (binding.llFilter.getTag().toString().equalsIgnoreCase("hide")) {
                    binding.ivFilter.setImageDrawable(context.getDrawable(R.drawable.eye_show));
                    binding.tvFilter.setText(context.getString(R.string.header_showAllWalletBalances_underline));
                    binding.llFilter.setTag("show");

                    if (!walletBalancesList.isEmpty()) {
                        List<WalletBalancesList> hideWalletBalancesList = new ArrayList<>();
                        for (WalletBalancesList walletBalance : walletBalancesList) {
                            if ((walletBalance.getAvailable())>0) hideWalletBalancesList.add(walletBalance);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                        binding.rvWalletBalanceList.setLayoutManager(layoutManager);
                        binding.rvWalletBalanceList.setAdapter(new WalletBalanceListAdpater(context, hideWalletBalancesList));
                    }
                }
            }
        });
    }

    private void setWalletHeader() {
        TextView tv_header = binding.layoutWalletHeader.findViewById(R.id.tv_header);
        ImageView iv_back = binding.layoutWalletHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutWalletHeader.findViewById(R.id.iv_endIcon);

        tv_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.VISIBLE);

        tv_header.setText(context.getString(R.string.header_wallets));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeScreen.class);
                intent.putExtra("openFragmentName","market");
                context.startActivity(intent);
            }
        });

        iv_endIcon.setImageDrawable(context.getDrawable(R.drawable.list));
        iv_endIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, WalletTransactionScreen.class));
            }
        });
    }

    private void setWalletCardHeader(double estUsdtValue, double estBtcValue) {
        TextView tv_totalWalletBalanceInUsdt = binding.layoutWalletCardHeader.findViewById(R.id.tv_totalWalletBalanceInUsdt);
        TextView tv_totalWalletBalanceInBtc = binding.layoutWalletCardHeader.findViewById(R.id.tv_totalWalletBalanceInBtc);

        tv_totalWalletBalanceInUsdt.setText(String.format("%s USDT", estUsdtValue));
        tv_totalWalletBalanceInBtc.setText(String.format("≈ %s BTC", estBtcValue));
    }

    private void getWalletBalances() {
        dialog = BaseUtils.showProgressDialog(context,"Please wait.");
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.getWalletBalances(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject walletBalanceRes) {
                        dialog.dismiss();
                        if (walletBalanceRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                    && walletBalanceRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            JsonArray jArray = walletBalanceRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<WalletBalancesList>>() {}.getType();
                            walletBalancesList = new Gson().fromJson(jArray, listType);

                            if (!walletBalancesList.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvWalletBalanceList.setLayoutManager(layoutManager);
                                binding.rvWalletBalanceList.setAdapter(new WalletBalanceListAdpater(context,walletBalancesList));

                                double estUsdtValue=0, estBtcValue=0, balance=0, usd=0, btc=0;
                                NumberFormat nf = NumberFormat.getInstance();
                                nf.setMaximumFractionDigits(Integer.MAX_VALUE);

                                for (WalletBalancesList walletBalance : walletBalancesList) {

                                    balance = walletBalance.getAvailable();
                                    usd = walletBalance.getPriceInUSD();
                                    btc = walletBalance.getPriceInBTC();

                                    if (balance<0) balance = Double.parseDouble(nf.format(balance));
                                    if (usd<0) usd = Double.parseDouble(nf.format(usd));
                                    if (btc<0) btc = Double.parseDouble(nf.format(btc));

                                    estUsdtValue = estUsdtValue + balance * usd;
                                    estBtcValue = estBtcValue + balance * btc;
                                }

                                Log.e("estUsdtValue",estUsdtValue+"");

                                setWalletCardHeader(estUsdtValue, estBtcValue);
                            }

                        } else if(walletBalanceRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else {
                            Log.e("Dashboard", "empty user-wallet-balances, error in API");
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(Objects.requireNonNull(context), context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

}
