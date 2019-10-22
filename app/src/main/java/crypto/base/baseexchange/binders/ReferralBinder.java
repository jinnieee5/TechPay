package crypto.base.baseexchange.binders;

import android.app.Activity;
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
import java.util.ArrayList;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.ReferralRegisteredAdapter;
import crypto.base.baseexchange.adapters.TopReferralPartnersAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.response.MyRegisteredReferralData;
import crypto.base.baseexchange.api.response.TopReferralData;
import crypto.base.baseexchange.databinding.LayoutReferralBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReferralBinder {
    private Context context;
    private LayoutReferralBinding binding;

    public ReferralBinder(Context context, LayoutReferralBinding binding){
        this.context = context;
        this.binding = binding;
        setHeader();
        getReferralDetails();
       // getRecyclerAdapter();
    }

    /*private void getRecyclerAdapter() {
        ArrayList
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.rvTopReferralPartners.setAdapter(new ReferralRegisteredAdapter(context, list));
    }*/

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_referral));

        ImageView iv_endIcon = binding.includeHeader.findViewById(R.id.iv_endIcon);
        iv_endIcon.setVisibility(View.GONE);

        ImageView iv_back = binding.includeHeader.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }

    private void getReferralDetails() {
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context, SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.getReferralDetails(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject loginRes) {
                        if (loginRes.get("Flag").getAsString().equalsIgnoreCase("success") && loginRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {

                            String referredFriends = loginRes.get("ReferredFriends").getAsString();
                            String activeReferrals = loginRes.get("ActiveReferrals").getAsString();
                            String estimatedEarnings = loginRes.get("EstimatedEarnings").getAsString();
                            String referralLink = loginRes.get("ReferralLink").getAsString();
                            String referralCode = loginRes.get("ReferralCode").getAsString();

                            binding.tvReferralLink.setText(referralLink);
                            binding.tvMyReferralId.setText(referralCode);

                            binding.tvReferralPoints.setText(referredFriends);
                            binding.tvActiveMembers.setText(activeReferrals);
                            binding.tvEstimatedEarning.setText(estimatedEarnings);

                            JsonArray jArray1 = loginRes.getAsJsonArray("TopReferralPartners");
                            final Type listType1 = new TypeToken<List<TopReferralData>>() {}.getType();
                            final List<TopReferralData> topReferralDataList = new Gson().fromJson(jArray1, listType1);
                            if (!topReferralDataList.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvRegisteredReferral.setLayoutManager(layoutManager);
                                binding.rvRegisteredReferral.setAdapter(new TopReferralPartnersAdapter(context, topReferralDataList));
                            }

                            JsonArray jArray2 = loginRes.getAsJsonArray("LatestReferralUser");
                            final Type listType2 = new TypeToken<List<MyRegisteredReferralData>>() {}.getType();
                            final List<MyRegisteredReferralData> myRegisteredReferralDataList = new Gson().fromJson(jArray2, listType2);
                            if (!myRegisteredReferralDataList.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvTopReferralPartners.setLayoutManager(layoutManager);
                                binding.rvTopReferralPartners.setAdapter(new ReferralRegisteredAdapter(context, myRegisteredReferralDataList));

                            } else Log.e("rvTradeMarketSellOrders","empty");

                        } else BaseUtils.customToast(context, loginRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
