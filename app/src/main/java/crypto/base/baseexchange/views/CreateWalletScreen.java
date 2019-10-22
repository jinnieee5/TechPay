package crypto.base.baseexchange.views;

import android.app.ProgressDialog;
import android.content.Intent;
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
import crypto.base.baseexchange.api.request.SignupReq;
import crypto.base.baseexchange.binders.CreateWalletBinder;
import crypto.base.baseexchange.databinding.LayoutCreateWalletBinding;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletScreen extends BaseActivity {
    private LayoutCreateWalletBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_create_wallet);
        final CreateWalletBinder binder = new CreateWalletBinder(this, binding);
        binding.setCreateWalletScreen(binder);

        binding.frameCreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeypad();
               if (binding.edEmail.getText().toString().isEmpty()) {
                   binding.edEmail.requestFocus();
                   binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                   binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                   BaseUtils.customToast(CreateWalletScreen.this,"Email ID is required.");
               } else if (binding.edPass.getText().toString().isEmpty()) {
                   binding.edPass.requestFocus();
                   binding.edPass.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                   binding.framePass.setTextColor(getColor(R.color.deepRed));
                   BaseUtils.customToast(CreateWalletScreen.this,"Password is required.");
               } else if (binding.edConPass.getText().toString().isEmpty()) {
                   binding.edConPass.requestFocus();
                   binding.edConPass.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                   binding.frameConPass.setTextColor(getColor(R.color.deepRed));
                   BaseUtils.customToast(CreateWalletScreen.this,"Confirm password is required.");
               } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.getText().toString()).matches()) {
                   binding.edEmail.requestFocus();
                   binding.edEmail.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                   binding.frameEmail.setTextColor(getColor(R.color.deepRed));
                   BaseUtils.customToast(CreateWalletScreen.this,getResources().getString(R.string.error_invalidEmail));
               } else if (!binding.edPass.getText().toString().equals(binding.edConPass.getText().toString())) {
                   binding.edConPass.setText("");
                   binding.edConPass.requestFocus();
                   binding.edConPass.setBackground(getDrawable(R.drawable.shape_bottomline_deepred));
                   binding.frameConPass.setTextColor(getColor(R.color.deepRed));
                   BaseUtils.customToast(CreateWalletScreen.this,getResources().getString(R.string.error_invalidConfirmPassword));
               } else callSignUp();
            }
        });

        binding.edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    binding.frameEmail.setTextColor(getColor(R.color.textColorLight));
                    binding.frameEdEmail.setBackground(getDrawable(R.drawable.shape_rectanglefilled_normal));
                    binding.edEmail.setBackground(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    binding.framePass.setTextColor(getColor(R.color.textColorLight));
                    binding.frameEdPass.setBackground(getDrawable(R.drawable.shape_rectanglefilled_normal));
                    binding.edPass.setBackground(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edConPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    binding.frameConPass.setTextColor(getColor(R.color.textColorLight));
                    binding.frameEdConPass.setBackground(getDrawable(R.drawable.shape_rectanglefilled_normal));
                    binding.edConPass.setBackground(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void callSignUp() {
        dialog = BaseUtils.showProgressDialog(this, getResources().getString(R.string.msg_wait));
        dialog.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(getApplicationContext()).create(AuthApiHelper.class);
        final SignupReq signupReq = new SignupReq();

        //signupReq.setIMEI(SharedPrefUtils.getFromPrefs(CreateWalletScreen.this, SharedPrefUtils.IMEI));
        signupReq.setIMEI("-");
        signupReq.setEmailID(binding.edEmail.getText().toString());
        signupReq.setPassword(binding.edPass.getText().toString());

        Single<JsonObject> observable = authApiHelper.doSignUp(signupReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(JsonObject signupRes) {
                        dialog.dismiss();

                        if (signupRes.get("Flag").getAsString().equals("success")) {
                            BaseUtils.customToast(getBaseContext(), getResources().getString(R.string.msg_mailVerification));

                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(CreateWalletScreen.this,LoginScreen.class));
                                }
                            },3000);
                        } else {
                            BaseUtils.customToast(getBaseContext(), signupRes.get("Message").getAsString());
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
