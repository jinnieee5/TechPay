package crypto.base.baseexchange.views;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.RequestCoinReq;
import crypto.base.baseexchange.api.response.WalletBalancesList;
import crypto.base.baseexchange.binders.CoinDepositBinder;
import crypto.base.baseexchange.databinding.LayoutCoinDepositBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CoinDepositScreen extends BaseActivity {
    private LayoutCoinDepositBinding binding;
    private ProgressDialog dialog;
    private WalletBalancesList walletBalance = new WalletBalancesList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.layout_coin_deposit);
        CoinDepositBinder binder = new CoinDepositBinder(this, binding);
        binding.setCoinDepositScreen(binder);

        if (getIntent().hasExtra("WalletBalanceObject")) walletBalance = (WalletBalancesList) getIntent().getSerializableExtra("WalletBalanceObject");
        getCoinAddress(walletBalance.getTclmid(), walletBalance.getCoinName(), walletBalance.getCoinCode());

        binding.tvCoinName.setText(String.format("%s (%s)", walletBalance.getCoinName(), walletBalance.getCoinCode()));

        String url = getResources().getString(R.string.coinImageUrl)+""+(walletBalance.getCoinName().toLowerCase()).replaceAll("\\s","")+".png";
        Log.e("url",url);
        Picasso.get().load(url).error(getResources().getDrawable(R.drawable.default_coin)).into(binding.ivLogo);

        binding.ivQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyWalletAddress();
                return false;
            }
        });
        binding.tvAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyWalletAddress();
                return false;
            }
        });
        binding.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyWalletAddress();
            }
        });

        binding.frameShareAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "TechPay-"+walletBalance.getCoinCode()+" Wallet");
                i.putExtra(Intent.EXTRA_TEXT, "My "+walletBalance.getCoinName()+" Wallet address is \n"+binding.tvAddress.getText().toString());
                startActivity(Intent.createChooser(i, "choose one"));
            }
        });
    }

    private void getCoinAddress(int tclmid, String coinName, String coinCode) {
        hideKeypad();
        dialog = BaseUtils.showProgressDialog(CoinDepositScreen.this, getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(CoinDepositScreen.this).create(AuthApiHelper.class);
        RequestCoinReq requestCoinReq = new RequestCoinReq();

        requestCoinReq.setLoginToken(SharedPrefUtils.getFromPrefs(CoinDepositScreen.this,SharedPrefUtils.LoginID));
        requestCoinReq.setTCLMID(tclmid+"");
        requestCoinReq.setCoinName(coinName);
        requestCoinReq.setCoinCode(coinCode);

        Single<JsonObject> observable = authApiHelper.depositCoin(requestCoinReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject requestCoinRes) {
                        dialog.dismiss();
                        if (requestCoinRes.get("Flag").getAsString().equals("success")
                                && requestCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            binding.layoutMain.setVisibility(View.VISIBLE);
                            binding.tvError.setVisibility(View.GONE);

                            binding.tvAddress.setText(requestCoinRes.get("WalletAddress").getAsString());
                            Picasso.get().load("https://api.qrserver.com/v1/create-qr-code/?data="+requestCoinRes.get("WalletAddress").getAsString()).into(binding.ivQrCode);

                        } else if(requestCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(CoinDepositScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(CoinDepositScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(CoinDepositScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(CoinDepositScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            binding.layoutMain.setVisibility(View.GONE);
                            binding.tvError.setVisibility(View.VISIBLE);

                            binding.tvError.setText(requestCoinRes.get("Message").getAsString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        binding.layoutMain.setVisibility(View.GONE);
                        binding.tvError.setVisibility(View.GONE);
                        BaseUtils.customToast(Objects.requireNonNull(CoinDepositScreen.this), getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void copyWalletAddress() {
        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(CoinDepositScreen.this).getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("wallet address",binding.tvAddress.getText().toString());
        clipboard.setPrimaryClip(clip);
        BaseUtils.customToast(CoinDepositScreen.this,"copied...");
    }
}
