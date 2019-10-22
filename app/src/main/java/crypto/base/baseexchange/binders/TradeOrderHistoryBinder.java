package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.TradeOrderHistoryAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.TradeOpenOrderRequest;
import crypto.base.baseexchange.api.response.TradeHistoryList;
import crypto.base.baseexchange.databinding.LayoutTradeOrderhistoryBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TradeOrderHistoryBinder {
    private Context context;
    private LayoutTradeOrderhistoryBinding binding;
    private ProgressDialog dialog;

    public TradeOrderHistoryBinder(Context context, LayoutTradeOrderhistoryBinding binding, String firstCoinTpspmid, String secondCoinTclmid) {
        this.context = context;
        this.binding = binding;

        setHeader();
        getTradeOrderHistoryList(firstCoinTpspmid, secondCoinTclmid);
        binding.tvEmptyTradeOrderHistoryList.setVisibility(View.GONE);
        binding.rvTradeOrderHistory.setVisibility(View.GONE);
    }

    private void setHeader() {
        ImageView iv_back = binding.layoutHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutHeader.findViewById(R.id.iv_endIcon);
        TextView tv_header = binding.layoutHeader.findViewById(R.id.tv_header);

        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.GONE);
        tv_header.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });

        tv_header.setText("Trade History");
    }

    private void getTradeOrderHistoryList(String firstCoinTpspmid, String secondCoinTclmid) {
        dialog = BaseUtils.showProgressDialog(context,"Please wait");
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        TradeOpenOrderRequest tradeOpenOrderRequest = new TradeOpenOrderRequest();
        tradeOpenOrderRequest.setLoginToken("");
        tradeOpenOrderRequest.setTpspmid(firstCoinTpspmid);
        tradeOpenOrderRequest.setTclmid(secondCoinTclmid);

        Single<JsonObject> observable = authApiHelper.tradeOrderHistory(tradeOpenOrderRequest);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject getKycDetailRes) {
                        dialog.dismiss();
                        if (getKycDetailRes.get("Flag").getAsString().equalsIgnoreCase("success")) {

                            JsonArray jArray = getKycDetailRes.getAsJsonArray("AllTradeHistoryData");
                            final Type listType = new TypeToken<List<TradeHistoryList>>() {}.getType();
                            final List<TradeHistoryList> orderHistoryList = new Gson().fromJson(jArray, listType);

                            if (!orderHistoryList.isEmpty()) {
                                binding.tvEmptyTradeOrderHistoryList.setVisibility(View.GONE);
                                binding.rvTradeOrderHistory.setVisibility(View.VISIBLE);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvTradeOrderHistory.setLayoutManager(layoutManager);
                                binding.rvTradeOrderHistory.setAdapter(new TradeOrderHistoryAdapter(context, orderHistoryList));
                            } else {
                                Log.e("tradeBuyOrderLists","empty");
                                binding.tvEmptyTradeOrderHistoryList.setVisibility(View.VISIBLE);
                                binding.rvTradeOrderHistory.setVisibility(View.GONE);
                            }

                        } else BaseUtils.customToast(context, getKycDetailRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
