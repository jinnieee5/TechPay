package crypto.base.baseexchange.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.GetTransactionFeeReq;
import crypto.base.baseexchange.api.request.SendCoinReq;
import crypto.base.baseexchange.api.response.WalletBalancesList;
import crypto.base.baseexchange.binders.CoinWithdrawalBinder;
import crypto.base.baseexchange.databinding.LayoutCoinWithdrawalBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CoinWithdrawalScreen extends BaseActivity {
    private LayoutCoinWithdrawalBinding binding;
    private ProgressDialog dialog;
    private WalletBalancesList walletBalance = new WalletBalancesList();
    private double transactionFee=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.layout_coin_withdrawal);
        final CoinWithdrawalBinder binder = new CoinWithdrawalBinder(this, binding);
        binding.setCoinWithdrawalScreen(binder);

        if (getIntent().hasExtra("WalletBalanceObject")) walletBalance = (WalletBalancesList) getIntent().getSerializableExtra("WalletBalanceObject");
        binding.tvCoinNameHeader.setText(String.format("%s (%s)", walletBalance.getCoinName(), walletBalance.getCoinCode()));
        String url = getResources().getString(R.string.coinImageUrl)+""+(walletBalance.getCoinName().toLowerCase()).replaceAll("\\s","")+".png";
        Log.e("url",url);
        Picasso.get().load(url).error(getResources().getDrawable(R.drawable.default_coin)).into(binding.ivLogo);

        getTransactionFee();

        final DecimalFormat df = new DecimalFormat("#.00000000");
        binding.tvTransaferAmountHeader.setText(String.format("Transfer Amount (Max: %s %s)", df.format(walletBalance.getAvailable()), walletBalance.getCoinCode()));

        binding.ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (CoinWithdrawalScreen.this.checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(CoinWithdrawalScreen.this,ScanQRCodeScreen.class));
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        binding.frameSendNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (binding.edToWalletAddress.getText().toString().isEmpty()) {
                    binding.edToWalletAddress.requestFocus();
                    binding.edToWalletAddress.setBackground(CoinWithdrawalScreen.this.getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.tvTo.setTextColor(getResources().getColor(R.color.deepRed));
                    BaseUtils.customToast(CoinWithdrawalScreen.this,"Wallet address is required.");
                } else if (binding.edTransferAmount.getText().toString().isEmpty()) {
                    binding.edTransferAmount.requestFocus();
                    binding.edTransferAmount.setBackground(CoinWithdrawalScreen.this.getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.tvTransaferAmountHeader.setTextColor(getResources().getColor(R.color.deepRed));
                    BaseUtils.customToast(CoinWithdrawalScreen.this,"Enter coin amount to transfer.");
                } else openTransactionPinDialog();
            }
        });

        binding.edToWalletAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvTo.setTextColor(CoinWithdrawalScreen.this.getColor(R.color.textColorLight));
                binding.frameToWalletAddress.setBackground(CoinWithdrawalScreen.this.getDrawable(R.drawable.shape_rectanglefilled_normal));
                binding.edToWalletAddress.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvTransaferAmountHeader.setTextColor(CoinWithdrawalScreen.this.getColor(R.color.textColorLight));
                binding.frameTransferAmount.setBackground(CoinWithdrawalScreen.this.getDrawable(R.drawable.shape_rectanglefilled_normal));
                binding.edTransferAmount.setBackground(null);

                if (s.length()>0) {
                    double transferAmount = Double.parseDouble(binding.edTransferAmount.getText().toString().trim());
                    if (transferAmount>(walletBalance.getAvailable())) {
                        BaseUtils.customToast(CoinWithdrawalScreen.this,"Transfer amount should be less than or equals to wallet balance");
                        hideKeypad();
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.edTransferAmount.setText("");
                                binding.edTransactionFee.setText("");
                                binding.edNetTotal.setText("");
                                binding.edUsdtValue.setText("");
                            }
                        },1000);
                    } else {
                        double transactionFeeAmount = (transferAmount*transactionFee)/100;
                        binding.edTransactionFee.setText(df.format(transactionFeeAmount));

                        double netTotal = transferAmount-transactionFeeAmount;
                        binding.edNetTotal.setText(df.format(netTotal));

                        double marketRate = walletBalance.getPriceInUSD();
                        double usdtValue = netTotal * marketRate;
                        binding.edUsdtValue.setText(df.format(usdtValue));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && permissions[0].equalsIgnoreCase(Manifest.permission.CAMERA) && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(CoinWithdrawalScreen.this,ScanQRCodeScreen.class));
        } else {
            BaseUtils.customToast(CoinWithdrawalScreen.this,"can't access camera.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String address = SharedPrefUtils.getFromPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.tempWalletAddress);
        String amount = SharedPrefUtils.getFromPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.tempTransferAmount);
        if (address!=null && !address.isEmpty()) {
            binding.edToWalletAddress.setText(address);
            binding.edTransferAmount.setText(amount);
            SharedPrefUtils.saveToPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.tempWalletAddress,"");
            SharedPrefUtils.saveToPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.tempTransferAmount,"");
            Toast.makeText(CoinWithdrawalScreen.this, "address - "+address, Toast.LENGTH_SHORT).show();
        }
    }

    private void openTransactionPinDialog() {
        hideKeypad();
        final AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_transaction_pin,null);
        final EditText ed_pin = v.findViewById(R.id.ed_pin);
        FrameLayout frame_continue = v.findViewById(R.id.frame_continue);
        AlertDialog.Builder builder = new AlertDialog.Builder(CoinWithdrawalScreen.this);
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();

        frame_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (ed_pin.getText().toString().isEmpty()) BaseUtils.customToast(CoinWithdrawalScreen.this,"PIN is required.");
                else {
                    String pin = ed_pin.getText().toString();
                    alertDialog.dismiss();
                    withdrawalCoin(pin);
                }
            }
        });
    }

    private void getTransactionFee() {
        hideKeypad();
        dialog = BaseUtils.showProgressDialog(CoinWithdrawalScreen.this, getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(CoinWithdrawalScreen.this).create(AuthApiHelper.class);
        GetTransactionFeeReq transactionFeeReq = new GetTransactionFeeReq();
        transactionFeeReq.setLoginToken(SharedPrefUtils.getFromPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.LoginID));
        transactionFeeReq.setTclmid(walletBalance.getTclmid());

        Single<JsonObject> observable = authApiHelper.getTransactionFee(transactionFeeReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject sendCoinRes) {
                        dialog.dismiss();
                        if (sendCoinRes.get("Flag").getAsString().equals("success")
                                && sendCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            transactionFee = Double.parseDouble(sendCoinRes.get("TransactionFeePer").getAsString().trim());
                        } else if(sendCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(CoinWithdrawalScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(CoinWithdrawalScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            BaseUtils.customToast(CoinWithdrawalScreen.this, sendCoinRes.get("Message").getAsString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(CoinWithdrawalScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void withdrawalCoin(String pin) {
        hideKeypad();
        dialog = BaseUtils.showProgressDialog(CoinWithdrawalScreen.this, getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(CoinWithdrawalScreen.this).create(AuthApiHelper.class);
        SendCoinReq sendCoinReq = new SendCoinReq();
        sendCoinReq.setLoginToken(SharedPrefUtils.getFromPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.LoginID));
        sendCoinReq.setSecretPin(pin);
        sendCoinReq.setTCLMID(walletBalance.getTclmid()+"");
        sendCoinReq.setAmount(binding.edTransferAmount.getText().toString());
        sendCoinReq.setDestinationAddress(binding.edToWalletAddress.getText().toString());

        Single<JsonObject> observable = authApiHelper.withdrawalCoin(sendCoinReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject sendCoinRes) {
                        dialog.dismiss();
                        if (sendCoinRes.get("Flag").getAsString().equals("success")
                                && sendCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {

                            BaseUtils.customToast(CoinWithdrawalScreen.this, sendCoinRes.get("Message").getAsString());
                            onBackPressed();

                        } else if(sendCoinRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(CoinWithdrawalScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(CoinWithdrawalScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(CoinWithdrawalScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            BaseUtils.customToast(CoinWithdrawalScreen.this, sendCoinRes.get("Message").getAsString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(CoinWithdrawalScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
