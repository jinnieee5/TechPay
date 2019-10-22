package crypto.base.baseexchange.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import com.google.gson.JsonObject;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.ForgotPasswordReq;
import crypto.base.baseexchange.binders.ForgotPasswordBinder;
import crypto.base.baseexchange.databinding.LayoutForgotPasswordBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordScreen extends BaseActivity {
    private LayoutForgotPasswordBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.layout_forgot_password);
        ForgotPasswordBinder binder = new ForgotPasswordBinder(this,binding);
        binding.setForgotPasswordScreen(binder);

        binding.frameContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (binding.edEmail.getText().toString().isEmpty()) {
                    binding.edEmail.requestFocus();
                    binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(ForgotPasswordScreen.this,"Email ID is required.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.getText().toString()).matches()) {
                    binding.edEmail.requestFocus();
                    binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(ForgotPasswordScreen.this,getResources().getString(R.string.error_invalidEmail));
                } else callForgotPassword();
            }
        });

        binding.edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.frameEmail.setTextColor(getColor(R.color.textColorLight));
                binding.frameEdEmail.setBackground(getDrawable(R.drawable.shape_rectanglefilled_normal));
                binding.edEmail.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void callForgotPassword() {
        dialog = BaseUtils.showProgressDialog(this, getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(getApplicationContext()).create(AuthApiHelper.class);
        ForgotPasswordReq forgotPasswordReq = new ForgotPasswordReq();
        forgotPasswordReq.setEmailID(binding.edEmail.getText().toString());

        Single<JsonObject> observable = authApiHelper.forgotPassword(forgotPasswordReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject forgotPasswordRes) {
                        dialog.dismiss();
                        if (forgotPasswordRes.get("Flag").getAsString().equals("success")) {
                            BaseUtils.customToast(ForgotPasswordScreen.this,"An authentication link has been sent to your Email ID," +
                                    "please check and update your new password.");
                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },2000);
                        } else {
                            binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                            binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                            BaseUtils.customToast(getBaseContext(), forgotPasswordRes.get("Message").getAsString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        BaseUtils.customToast(getBaseContext(), getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
