package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.OpenOrderHistoryAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.CancelOpenOrderReq;
import crypto.base.baseexchange.api.request.UserOrderListReq;
import crypto.base.baseexchange.api.response.UserOpenOrderList;
import crypto.base.baseexchange.databinding.LayoutOpenOrderBinding;
import crypto.base.baseexchange.interfaces.OpenOrderCancelListener;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OpenOrderBinder implements OpenOrderCancelListener {
    private Context context;
    private LayoutOpenOrderBinding binding;
    private ProgressDialog allOrderDialog, cancelOrderDialog;
    private String firstCoinTpspmid, secondCoinTclmid;

    public OpenOrderBinder(Context context, LayoutOpenOrderBinding binding, String firstCoinTpspmid, String secondCoinTclmid) {
        this.context = context;
        this.binding = binding;
        this.firstCoinTpspmid = firstCoinTpspmid;
        this.secondCoinTclmid = secondCoinTclmid;

        setHeader();
        getAllOpenOrders(firstCoinTpspmid,secondCoinTclmid);

        binding.layoutEmptyOrderList.setVisibility(View.GONE);
        binding.rvOpenOrderList.setVisibility(View.GONE);
    }

    private void setHeader() {
        TextView tv_header = binding.layoutOrderHistoryHeader.findViewById(R.id.tv_header);
        ImageView iv_back = binding.layoutOrderHistoryHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutOrderHistoryHeader.findViewById(R.id.iv_endIcon);

        tv_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.GONE);

        tv_header.setText(context.getString(R.string.header_openOrders));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    private void getAllOpenOrders(String firstCoinTpspmid, String secondCoinTclmid) {
        allOrderDialog = BaseUtils.showProgressDialog(context,"Please wait");
        allOrderDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        UserOrderListReq userOrderListReq = new UserOrderListReq();
        userOrderListReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        userOrderListReq.setTpspmid(firstCoinTpspmid);
        userOrderListReq.setTclmid(secondCoinTclmid);

        Single<JsonObject> observable = authApiHelper.getAllOrdersList(userOrderListReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject userOpenOrderListRes) {
                        allOrderDialog.dismiss();
                        if (userOpenOrderListRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && userOpenOrderListRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {

                            JsonArray jArray = userOpenOrderListRes.getAsJsonArray("SellOpenOrderData");
                            final Type listType = new TypeToken<List<UserOpenOrderList>>() {}.getType();
                            final List<UserOpenOrderList> openOrderList = new Gson().fromJson(jArray, listType);

                            if (!openOrderList.isEmpty()) {
                                binding.layoutEmptyOrderList.setVisibility(View.GONE);
                                binding.rvOpenOrderList.setVisibility(View.VISIBLE);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvOpenOrderList.setLayoutManager(layoutManager);
                                binding.rvOpenOrderList.setAdapter(new OpenOrderHistoryAdapter(context, openOrderList, OpenOrderBinder.this));
                            } else {
                                Log.e("tradeBuyOrderLists","empty");
                                binding.layoutEmptyOrderList.setVisibility(View.VISIBLE);
                                binding.rvOpenOrderList.setVisibility(View.GONE);
                            }

                        } else if(userOpenOrderListRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, userOpenOrderListRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        allOrderDialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void cancelOpenOrder(String type, String tccbtmid, String tccstmid) {
        cancelOrderDialog = BaseUtils.showProgressDialog(context,"Please wait");
        cancelOrderDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        CancelOpenOrderReq cancelOpenOrderReq = new CancelOpenOrderReq();
        cancelOpenOrderReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        cancelOpenOrderReq.setType(type);
        cancelOpenOrderReq.setTccbtmid(tccbtmid);
        cancelOpenOrderReq.setTccstmid(tccstmid);

        Single<JsonObject> observable = authApiHelper.cancelOpenOrder(cancelOpenOrderReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject cancelOpenOrderRes) {
                        cancelOrderDialog.dismiss();
                        if (cancelOpenOrderRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && cancelOpenOrderRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            BaseUtils.customToast(context, "Order has being cancelled successfully.");
                            getAllOpenOrders(firstCoinTpspmid,secondCoinTclmid);
                        } else if(cancelOpenOrderRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, cancelOpenOrderRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        cancelOrderDialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    @Override
    public void onOpenOrderClick(String type, String tccbtmid, String tccstmid) {
        cancelOpenOrder(type, tccbtmid, tccstmid);
    }
}
