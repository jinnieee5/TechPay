package crypto.base.baseexchange.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.TicketListAdapter;
import crypto.base.baseexchange.adapters.TicketReplyAdapter;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.GetAllTicketRepliesReq;
import crypto.base.baseexchange.api.request.ReplyTicketReq;
import crypto.base.baseexchange.api.response.TicketChatList;
import crypto.base.baseexchange.api.response.TicketData;
import crypto.base.baseexchange.binders.TicketReplyBinder;
import crypto.base.baseexchange.databinding.LayoutReplyTicketBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TicketReplyScreen extends BaseActivity {
    private LayoutReplyTicketBinding binding;
    private ProgressDialog dialog;
    private String tmid="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_reply_ticket);
        TicketReplyBinder binder = new TicketReplyBinder(this,binding);
        binding.setTicketReplyScreen(binder);

        if (getIntent().hasExtra("tmid")) tmid=getIntent().getStringExtra("tmid");
        getAllTicketReplies(tmid);

        binding.framePostReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyToTicket(tmid);
            }
        });
    }

    private void getAllTicketReplies(String tmid) {
        BaseUtils.hideKeypad(TicketReplyScreen.this);
        dialog = BaseUtils.showProgressDialog(TicketReplyScreen.this,"Please wait");
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(TicketReplyScreen.this).create(AuthApiHelper.class);
        GetAllTicketRepliesReq getAllTicketRepliesReq = new GetAllTicketRepliesReq();
        getAllTicketRepliesReq.setLoginToken(SharedPrefUtils.getFromPrefs(TicketReplyScreen.this,SharedPrefUtils.LoginID));
        getAllTicketRepliesReq.setTmid(tmid);

        Single<JsonObject> observable = authApiHelper.getAllTicketReplies(getAllTicketRepliesReq);
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

                            JsonObject jObject = ticketListRes.getAsJsonObject("TicketData");
                            final Type listType1 = new TypeToken<TicketData>() {}.getType();
                            TicketData ticketData = new Gson().fromJson(jObject, listType1);

                            JsonArray jArray = ticketListRes.getAsJsonArray("ChatData");
                            final Type listType2 = new TypeToken<List<TicketChatList>>() {}.getType();
                            List<TicketChatList> ticketChatList = new Gson().fromJson(jArray, listType2);

                            if (!ticketChatList.isEmpty()) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(TicketReplyScreen.this, RecyclerView.VERTICAL, false);
                                binding.rvTicketChat.setLayoutManager(layoutManager);
                                binding.rvTicketChat.setAdapter(new TicketReplyAdapter(TicketReplyScreen.this, ticketChatList));
                            }
                        } else if(ticketListRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(TicketReplyScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(TicketReplyScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(TicketReplyScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(TicketReplyScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else BaseUtils.customToast(TicketReplyScreen.this, ticketListRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(TicketReplyScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void replyToTicket(String tmid) {
        BaseUtils.hideKeypad(TicketReplyScreen.this);
        dialog = BaseUtils.showProgressDialog(TicketReplyScreen.this,"Please wait");
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(TicketReplyScreen.this).create(AuthApiHelper.class);
        ReplyTicketReq replyTicketReq = new ReplyTicketReq();
        replyTicketReq.setLoginToken(SharedPrefUtils.getFromPrefs(TicketReplyScreen.this,SharedPrefUtils.LoginID));
        replyTicketReq.setTmid(tmid);
        replyTicketReq.setMessage(binding.edMessage.getText().toString());
        replyTicketReq.setImgPreviewFirst("");
        replyTicketReq.setImgPreviewSecond("");
        replyTicketReq.setImgPreviewThird("");

        Single<JsonObject> observable = authApiHelper.replyToTicket(replyTicketReq);
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
                            BaseUtils.customToast(TicketReplyScreen.this, "Reply posted.");
                            finish();
                            startActivity(getIntent());
                        } else if(ticketListRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(TicketReplyScreen.this,getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(TicketReplyScreen.this,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(TicketReplyScreen.this,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(TicketReplyScreen.this, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else BaseUtils.customToast(TicketReplyScreen.this, ticketListRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(TicketReplyScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
