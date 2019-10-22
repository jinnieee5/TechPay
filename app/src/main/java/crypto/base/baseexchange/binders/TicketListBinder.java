package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.TicketListAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.api.response.TicketList;
import crypto.base.baseexchange.databinding.LayoutTicketListBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TicketListBinder {
    private Context context;
    private LayoutTicketListBinding binding;
    private ProgressDialog dialog;

    public TicketListBinder(Context context, LayoutTicketListBinding binding) {
        this.context = context;
        this.binding = binding;

        setHeader();
        getAllTickets();
    }

    private void getAllTickets() {
        BaseUtils.hideKeypad((Activity) context);
        dialog = BaseUtils.showProgressDialog(context,"Please wait");
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.getAllTicketList(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject ticketListRes) {
                        dialog.dismiss();
                        if (ticketListRes.get("Flag").getAsString().equals("success")
                                && ticketListRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            JsonArray jArray = ticketListRes.getAsJsonArray("Data");
                            final Type listType = new TypeToken<List<TicketList>>() {}.getType();
                            List<TicketList> ticketList = new Gson().fromJson(jArray, listType);

                            if (!ticketList.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                binding.rvTicketList.setLayoutManager(layoutManager);
                                binding.rvTicketList.setAdapter(new TicketListAdapter(context, ticketList));
                            }
                        } else if(ticketListRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, ticketListRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_allTicketList));

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
}
