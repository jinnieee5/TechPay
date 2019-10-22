package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.MarketPairingAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.InterExchangeParingListReq;
import crypto.base.baseexchange.api.response.CoinPairingList;
import crypto.base.baseexchange.api.response.MarketCoin;
import crypto.base.baseexchange.databinding.LayoutMarketPairingBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MarketPairingBinder {
    private Context context;
    private LayoutMarketPairingBinding binding;
    private ProgressDialog pairingListProgressDialog;
    private MarketCoin marketCoin;

    public MarketPairingBinder(Context context, LayoutMarketPairingBinding binding, MarketCoin marketCoin) {
        this.context = context;
        this.binding = binding;
        this.marketCoin = marketCoin;

        coinPairingList(marketCoin.getCoinCode(), marketCoin.getPairingID());
    }

    private void coinPairingList(final String firstCoinCode, final int pairingID) {
        BaseUtils.hideKeypad((Activity) context);
        pairingListProgressDialog = BaseUtils.showProgressDialog(context,"Please wait");
        pairingListProgressDialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        InterExchangeParingListReq paringListReq = new InterExchangeParingListReq();
        paringListReq.setLoginToken("");
        paringListReq.setID(pairingID);

        Single<JsonObject> observable = authApiHelper.getInterExchangeCoinPairingList(paringListReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject pairingRes) {
                        pairingListProgressDialog.dismiss();
                        if (pairingRes.get("Flag").getAsString().equals("success")) {
                            JsonArray jArray = pairingRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<CoinPairingList>>() {}.getType();
                            List<CoinPairingList> coinPairingList = new Gson().fromJson(jArray, listType);

                            LinearLayoutManager sellLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            binding.rvCoinPairingList.setLayoutManager(sellLayoutManager);
                            binding.rvCoinPairingList.setAdapter(new MarketPairingAdapter(context, coinPairingList, firstCoinCode));
                        } else BaseUtils.customToast(context, pairingRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        pairingListProgressDialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
