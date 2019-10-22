package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import java.lang.reflect.Type;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.WalletDepositHistoryAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.response.DepositTransactionList;
import crypto.base.baseexchange.databinding.LayoutWalletDeposithistoryBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletDepositHistoryBinder {
    private Context context;
    private LayoutWalletDeposithistoryBinding binding;
    private ProgressDialog dialog;

    public WalletDepositHistoryBinder(Context context, LayoutWalletDeposithistoryBinding binding) {
        this.context = context;
        this.binding = binding;

        getDepositHistory();
        binding.layoutEmptyTransaction.setVisibility(View.GONE);
        binding.rvDepositHistory.setVisibility(View.GONE);
    }

    private void getDepositHistory() {
        BaseUtils.hideKeypad((Activity) context);
        dialog = BaseUtils.showProgressDialog(context, context.getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.depositHistory(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject transactionHistoryRes) {
                        dialog.dismiss();
                        if (transactionHistoryRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && transactionHistoryRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            JsonArray jArray = transactionHistoryRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<DepositTransactionList>>() {}.getType();
                            final List<DepositTransactionList> transactionList = new Gson().fromJson(jArray, listType);

                            if (transactionList.isEmpty()) {
                                binding.layoutEmptyTransaction.setVisibility(View.VISIBLE);
                                binding.rvDepositHistory.setVisibility(View.GONE);
                            } else if(transactionHistoryRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                                BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                                SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                                SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                                Intent intent = new Intent(context, EnterScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }  else {
                                binding.layoutEmptyTransaction.setVisibility(View.GONE);
                                binding.rvDepositHistory.setVisibility(View.VISIBLE);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvDepositHistory.setLayoutManager(layoutManager);
                                binding.rvDepositHistory.setAdapter(new WalletDepositHistoryAdapter(context, transactionList));
                            }
                        } else BaseUtils.customToast(context, transactionHistoryRes.get("Message").getAsString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
