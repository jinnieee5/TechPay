package crypto.base.baseexchange.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import crypto.base.baseexchange.api.request.LoginReq;
import crypto.base.baseexchange.binders.LoginBinder;
import crypto.base.baseexchange.databinding.LayoutLoginBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginScreen extends BaseActivity {
    private LayoutLoginBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_login);
        LoginBinder binder = new LoginBinder(this,binding);
        binding.setLoginScreen(binder);

        binding.frameLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
                if (binding.edEmail.getText().toString().isEmpty()) {
                    binding.edEmail.requestFocus();
                    binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(LoginScreen.this,"Email ID is required.");
                } else if (binding.edPass.getText().toString().isEmpty()) {
                    binding.edPass.requestFocus();
                    binding.edPass.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.framePass.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(LoginScreen.this,"Password is required.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.getText().toString()).matches()) {
                    binding.edEmail.requestFocus();
                    binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                    binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                    BaseUtils.customToast(LoginScreen.this,getResources().getString(R.string.error_invalidEmail));
                } else callLogin();
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

        binding.edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.framePass.setTextColor(getColor(R.color.textColorLight));
                binding.frameEdPass.setBackground(getDrawable(R.drawable.shape_rectanglefilled_normal));
                binding.edPass.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this,ForgotPasswordScreen.class));
            }
        });
    }

    private void callLogin() {
        dialog = BaseUtils.showProgressDialog(this, getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(getApplicationContext()).create(AuthApiHelper.class);
        LoginReq loginReq = new LoginReq();

        //loginReq.setIMEI(SharedPrefUtils.getFromPrefs(LoginScreen.this, SharedPrefUtils.IMEI));
        loginReq.setIMEI("-");
        loginReq.setEmailID(binding.edEmail.getText().toString());
        loginReq.setPassword(binding.edPass.getText().toString());

        Single<JsonObject> observable = authApiHelper.doLogin(loginReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject loginRes) {
                        dialog.dismiss();
                        if (loginRes.get("Flag").getAsString().equals("success")) {
                            SharedPrefUtils.saveToPrefs(LoginScreen.this,SharedPrefUtils.isLogin,"1");
                            SharedPrefUtils.saveToPrefs(LoginScreen.this,SharedPrefUtils.LoginID,loginRes.get("LoginToken").getAsString());
                            SharedPrefUtils.saveToPrefs(LoginScreen.this,SharedPrefUtils.EmailID,binding.edEmail.getText().toString());

                            Intent intent = new Intent(LoginScreen.this,HomeScreen.class);
                            intent.putExtra("pinMsg","create");
                            startActivity(intent);
                        } else {
                            binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                            binding.framePass.setTextColor(getColor(R.color.deepRed));
                            binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                            binding.edPass.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                            BaseUtils.customToast(getBaseContext(), loginRes.get("Message").getAsString());
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
