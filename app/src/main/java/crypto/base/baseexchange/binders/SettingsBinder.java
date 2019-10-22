package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonObject;
import java.security.SecureRandom;
import androidx.appcompat.app.AlertDialog;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.api.request.ChangePasswordReq;
import crypto.base.baseexchange.api.request.ChangeSecretPinReq;
import crypto.base.baseexchange.api.request.LoginTokenReq;
import crypto.base.baseexchange.databinding.LayoutSettingsBinding;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.RandomString;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import crypto.base.baseexchange.views.EnterScreen;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsBinder {
    private Context context;
    private LayoutSettingsBinding binding;
    private ProgressDialog dialog1, dialog2, dialog3;
    private AlertDialog alertDialogChangePass, alertDialogNewPin, alertDialogChangePin, alertDialogBackUp1, alertDialogBackUp2;

    public SettingsBinder(Context context, LayoutSettingsBinding binding) {
        this.context = context;
        this.binding = binding;

        setHeader();
        callLinks();
    }

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_settings));

        ImageView iv_back = binding.includeHeader.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });

        ImageView iv_endIcon = binding.includeHeader.findViewById(R.id.iv_endIcon);
        iv_endIcon.setVisibility(View.GONE);
    }

    private void callLinks() {
        binding.llChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSecretPINStatus();
            }
        });

        binding.llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordDialog();
            }
        });

        binding.llTwoStepVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.llAmlCft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://techpay.io/aml-cft")
                );
                context.startActivity(i);
            }
        });
        binding.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://techpay.io/privacy-policy")
                );
                context.startActivity(i);
            }
        });
        binding.llTermsService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://techpay.io/terms-of-use")
                );
                context.startActivity(i);
            }
        });
        binding.llFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://techpay.io/fees")
                );
                context.startActivity(i);
            }
        });
        binding.llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://techpay.io/news")
                );
                context.startActivity(i);
            }
        });
    }

    private void openChangeSecretPinDialog() {
        BaseUtils.hideKeypad((Activity) context);
        View changeSecretPinLayout = LayoutInflater.from(context).inflate(R.layout.dialog_secretpin_change,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(changeSecretPinLayout);

        alertDialogChangePin = builder.create();
        alertDialogChangePin.show();
        //Objects.requireNonNull(alertDialogChangePin.getWindow()).setLayout(700,1000);

        final TextView frame_oldSecretPin = changeSecretPinLayout.findViewById(R.id.frame_oldSecretPin);
        final TextView frame_oldSecretBackUpKey = changeSecretPinLayout.findViewById(R.id.frame_oldSecretBackUpKey);
        final TextView frame_newSecretPin = changeSecretPinLayout.findViewById(R.id.frame_newSecretPin);
        final TextView frame_confirmNewSecretPin = changeSecretPinLayout.findViewById(R.id.frame_confirmNewSecretPin);

        final FrameLayout frame_edOldSecretPin = changeSecretPinLayout.findViewById(R.id.frame_edOldSecretPin);
        final FrameLayout frame_edOldSecretBackUpKey = changeSecretPinLayout.findViewById(R.id.frame_edOldSecretBackUpKey);
        final FrameLayout frame_edNewSecretPin = changeSecretPinLayout.findViewById(R.id.frame_edNewSecretPin);
        final FrameLayout frame_edConfirmNewSecretPin = changeSecretPinLayout.findViewById(R.id.frame_edConfirmNewSecretPin);

        final EditText ed_oldSecretPin = changeSecretPinLayout.findViewById(R.id.ed_oldSecretPin);
        final EditText ed_oldSecretBackUpKey = changeSecretPinLayout.findViewById(R.id.ed_oldSecretBackUpKey);
        final EditText ed_newSecretPin = changeSecretPinLayout.findViewById(R.id.ed_newSecretPin);
        final EditText ed_confirmNewSecretPin = changeSecretPinLayout.findViewById(R.id.ed_confirmNewSecretPin);

        ImageView iv_cross = changeSecretPinLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogChangePin.dismiss();
            }
        });

        FrameLayout frame_submit = changeSecretPinLayout.findViewById(R.id.frame_submit);
        frame_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_oldSecretPin.getText().toString().isEmpty() && ed_oldSecretBackUpKey.getText().toString().isEmpty()) {
                    ed_oldSecretPin.requestFocus();
                    ed_oldSecretPin.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    ed_oldSecretBackUpKey.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_oldSecretPin.setTextColor(context.getColor(R.color.deepRed));
                    frame_oldSecretBackUpKey.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Either 'Old Secret PIN' or 'Old Secret PIN Backup Key' is required.");
                } else if (ed_newSecretPin.getText().toString().isEmpty()) {
                    ed_newSecretPin.requestFocus();
                    ed_newSecretPin.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_newSecretPin.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Please enter your new 'Secret PIN' ");
                } else if (ed_confirmNewSecretPin.getText().toString().isEmpty()) {
                    ed_confirmNewSecretPin.requestFocus();
                    ed_confirmNewSecretPin.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_confirmNewSecretPin.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Please confirm New Secret PIN.");
                } else if (!ed_newSecretPin.getText().toString().equals(ed_confirmNewSecretPin.getText().toString())) {
                    ed_confirmNewSecretPin.setText("");
                    ed_confirmNewSecretPin.requestFocus();
                    ed_confirmNewSecretPin.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_confirmNewSecretPin.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"PINs don't match.");
                } else openNewSecretBackUpKeyDialog(ed_oldSecretPin.getText().toString(), ed_oldSecretBackUpKey.getText().toString(), ed_newSecretPin.getText().toString());
            }
        });

        ed_oldSecretPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_oldSecretPin.setTextColor(context.getColor(R.color.textColorLight));
                frame_edOldSecretPin.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_oldSecretPin.setBackground(null);

                frame_oldSecretBackUpKey.setTextColor(context.getColor(R.color.textColorLight));
                frame_edOldSecretBackUpKey.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_oldSecretBackUpKey.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_oldSecretBackUpKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_oldSecretPin.setTextColor(context.getColor(R.color.textColorLight));
                frame_edOldSecretPin.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_oldSecretPin.setBackground(null);

                frame_oldSecretBackUpKey.setTextColor(context.getColor(R.color.textColorLight));
                frame_edOldSecretBackUpKey.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_oldSecretBackUpKey.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_newSecretPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_newSecretPin.setTextColor(context.getColor(R.color.textColorLight));
                frame_edNewSecretPin.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_newSecretPin.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_confirmNewSecretPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_confirmNewSecretPin.setTextColor(context.getColor(R.color.textColorLight));
                frame_edConfirmNewSecretPin.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_confirmNewSecretPin.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void openNewSecretPinDialog() {
        BaseUtils.hideKeypad((Activity) context);
        View newSecretPinLayout = LayoutInflater.from(context).inflate(R.layout.dialog_secretpin_new,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(newSecretPinLayout);

        alertDialogNewPin = builder.create();
        alertDialogNewPin.show();
        //Objects.requireNonNull(alertDialogNewPin.getWindow()).setLayout(700,800);

        final TextView frame_secretPin = newSecretPinLayout.findViewById(R.id.frame_secretPin);
        final TextView frame_confirmSecretPin = newSecretPinLayout.findViewById(R.id.frame_confirmSecretPin);

        final FrameLayout frame_edSecretPin = newSecretPinLayout.findViewById(R.id.frame_edSecretPin);
        final FrameLayout frame_edConfirmSecretPin = newSecretPinLayout.findViewById(R.id.frame_edConfirmSecretPin);

        final EditText ed_secretPin = newSecretPinLayout.findViewById(R.id.ed_secretPin);
        final EditText ed_confirmSecretPin = newSecretPinLayout.findViewById(R.id.ed_confirmSecretPin);

        ImageView iv_cross = newSecretPinLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogNewPin.dismiss();
            }
        });

        FrameLayout frame_submit = newSecretPinLayout.findViewById(R.id.frame_submit);
        frame_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_secretPin.getText().toString().isEmpty()) {
                    ed_secretPin.requestFocus();
                    ed_secretPin.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_secretPin.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Please enter Secret PIN");
                } else if (ed_confirmSecretPin.getText().toString().isEmpty()) {
                    ed_confirmSecretPin.requestFocus();
                    BaseUtils.customToast(context,"Please confirm Secret PIN");
                } else if (!ed_secretPin.getText().toString().equals(ed_confirmSecretPin.getText().toString())) {
                    ed_confirmSecretPin.setText("");
                    ed_confirmSecretPin.requestFocus();
                    ed_confirmSecretPin.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_confirmSecretPin.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"PINs don't match.");
                } else openNewSecretBackUpKeyDialog(ed_secretPin.getText().toString());
            }
        });

        ed_secretPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_secretPin.setTextColor(context.getColor(R.color.textColorLight));
                frame_edSecretPin.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_secretPin.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_confirmSecretPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_confirmSecretPin.setTextColor(context.getColor(R.color.textColorLight));
                frame_edConfirmSecretPin.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_confirmSecretPin.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //if new PIN
    private void openNewSecretBackUpKeyDialog(final String newSecretPin) {
        BaseUtils.hideKeypad((Activity) context);
        View secretBackUpKeyLayout = LayoutInflater.from(context).inflate(R.layout.dialog_secretpin_backupkey,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(secretBackUpKeyLayout);

        alertDialogBackUp1 = builder.create();
        alertDialogBackUp1.show();
        //Objects.requireNonNull(alertDialogBackUp1.getWindow()).setLayout(700,580);

        final TextView tv_key = secretBackUpKeyLayout.findViewById(R.id.tv_key);
        RandomString tickets = new RandomString(13, new SecureRandom());
        tv_key.setText(tickets.nextString());

        final ImageView iv_copy = secretBackUpKeyLayout.findViewById(R.id.iv_copy);
        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("wallet address",tv_key.getText().toString());
                clipboard.setPrimaryClip(clip);
                BaseUtils.customToast(context,"copied...");
            }
        });

        FrameLayout frame_cancel = secretBackUpKeyLayout.findViewById(R.id.frame_cancel);
        frame_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBackUp1.dismiss();
            }
        });

        FrameLayout frame_submit = secretBackUpKeyLayout.findViewById(R.id.frame_submit);
        frame_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = 1;
                String newSecretBackUpKey = tv_key.getText().toString();
                changeSecretPIN("","",newSecretPin,newSecretBackUpKey,mode);
            }
        });
    }

    //if change PIN
    private void openNewSecretBackUpKeyDialog(final String oldSecretPin, final String oldSecretBackUpKey, final String newSecretPin) {
        BaseUtils.hideKeypad((Activity) context);
        View secretBackUpKeyLayout = LayoutInflater.from(context).inflate(R.layout.dialog_secretpin_backupkey,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(secretBackUpKeyLayout);

        alertDialogBackUp2 = builder.create();
        alertDialogBackUp2.show();
        //Objects.requireNonNull(alertDialogBackUp2.getWindow()).setLayout(700,580);

        final TextView tv_key = secretBackUpKeyLayout.findViewById(R.id.tv_key);
        RandomString tickets = new RandomString(13, new SecureRandom());
        tv_key.setText(tickets.nextString());

        final ImageView iv_copy = secretBackUpKeyLayout.findViewById(R.id.iv_copy);
        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("wallet address",tv_key.getText().toString());
                clipboard.setPrimaryClip(clip);
                BaseUtils.customToast(context,"copied...");
            }
        });

        FrameLayout frame_cancel = secretBackUpKeyLayout.findViewById(R.id.frame_cancel);
        frame_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBackUp2.dismiss();
            }
        });

        FrameLayout frame_submit = secretBackUpKeyLayout.findViewById(R.id.frame_submit);
        frame_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = 2;
                String newSecretBackUpKey = tv_key.getText().toString();
                changeSecretPIN(oldSecretPin,oldSecretBackUpKey,newSecretPin,newSecretBackUpKey,mode);
            }
        });
    }

    private void openChangePasswordDialog() {
        BaseUtils.hideKeypad((Activity) context);
        View changePassLayout = LayoutInflater.from(context).inflate(R.layout.dialog_change_password,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(changePassLayout);

        alertDialogChangePass = builder.create();
        alertDialogChangePass.show();
        //Objects.requireNonNull(alertDialogChangePass.getWindow()).setLayout(700,1000);

        final TextView frame_oldPass = changePassLayout.findViewById(R.id.frame_oldPass);
        final TextView frame_newPass = changePassLayout.findViewById(R.id.frame_newPass);
        final TextView frame_conPass = changePassLayout.findViewById(R.id.frame_conPass);

        final FrameLayout frame_edOldPass = changePassLayout.findViewById(R.id.frame_edOldPass);
        final FrameLayout frame_edNewPass = changePassLayout.findViewById(R.id.frame_edNewPass);
        final FrameLayout frame_edConPass = changePassLayout.findViewById(R.id.frame_edConPass);

        final EditText ed_oldPass = changePassLayout.findViewById(R.id.ed_oldPass);
        final EditText ed_newPass = changePassLayout.findViewById(R.id.ed_newPass);
        final EditText ed_conPass = changePassLayout.findViewById(R.id.ed_conPass);

        ImageView iv_cross = changePassLayout.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogChangePass.dismiss();
            }
        });

        FrameLayout frame_changePass = changePassLayout.findViewById(R.id.frame_changePass);
        frame_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_oldPass.getText().toString().isEmpty()) {
                    ed_oldPass.requestFocus();
                    ed_oldPass.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_oldPass.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Old password is required.");
                } else if (ed_newPass.getText().toString().isEmpty()) {
                    ed_newPass.requestFocus();
                    ed_newPass.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_newPass.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"New password is required.");
                } else if (ed_conPass.getText().toString().isEmpty()) {
                    ed_conPass.requestFocus();
                    ed_conPass.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_conPass.setTextColor(context.getColor(R.color.deepRed));
                    BaseUtils.customToast(context,"Please Confirm your password.");
                } else if (!ed_newPass.getText().toString().equals(ed_conPass.getText().toString())) {
                    BaseUtils.customToast(context,"Passwords must be same.");
                    ed_newPass.setText("");
                    ed_conPass.setText("");
                    ed_newPass.requestFocus();

                    ed_newPass.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_newPass.setTextColor(context.getColor(R.color.deepRed));

                    ed_conPass.setBackground(context.getDrawable(R.drawable.shape_bottomline_deepred));
                    frame_conPass.setTextColor(context.getColor(R.color.deepRed));
                } else {
                    String oldPass = ed_oldPass.getText().toString();
                    String newPass = ed_newPass.getText().toString();
                    String conPass = ed_conPass.getText().toString();

                    doChangePassword(oldPass, newPass, conPass);
                }
            }
        });

        ed_oldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_oldPass.setTextColor(context.getColor(R.color.textColorLight));
                frame_edOldPass.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_oldPass.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_newPass.setTextColor(context.getColor(R.color.textColorLight));
                frame_edNewPass.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_newPass.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ed_conPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frame_conPass.setTextColor(context.getColor(R.color.textColorLight));
                frame_edConPass.setBackground(context.getDrawable(R.drawable.shape_rectanglefilled_normal));
                ed_conPass.setBackground(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void checkSecretPINStatus() {
        BaseUtils.hideKeypad((Activity) context);
        dialog1 = BaseUtils.showProgressDialog(context,"Please wait");
        dialog1.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        LoginTokenReq loginTokenReq = new LoginTokenReq();
        loginTokenReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));

        Single<JsonObject> observable = authApiHelper.checkSecretPINStatus(loginTokenReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject checkSecretPinStatusRes) {
                        dialog1.dismiss();
                        if (checkSecretPinStatusRes.get("Flag").getAsString().equals("success")
                                && checkSecretPinStatusRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            if (checkSecretPinStatusRes.get("SecretPinStatus").getAsInt()==0) openNewSecretPinDialog();
                            else if (checkSecretPinStatusRes.get("SecretPinStatus").getAsInt()==1) openChangeSecretPinDialog();
                        } else if(checkSecretPinStatusRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, checkSecretPinStatusRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog1.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void changeSecretPIN(String oldSecretPin, String oldSecretBackUpKey, String newSecretPin, String newSecretBackUpKey, final int mode) {
        BaseUtils.hideKeypad((Activity) context);
        dialog2 = BaseUtils.showProgressDialog(context,"Please wait");
        dialog2.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        ChangeSecretPinReq changeSecretPinReq = new ChangeSecretPinReq();
        changeSecretPinReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        changeSecretPinReq.setOldSecretPin(oldSecretPin);
        changeSecretPinReq.setOldSecretBackUpKey(oldSecretBackUpKey);
        changeSecretPinReq.setNewSecretPin(newSecretPin);
        changeSecretPinReq.setNewSecretBackUpKey(newSecretBackUpKey);
        changeSecretPinReq.setMode(mode);

        Single<JsonObject> observable = authApiHelper.changeSecretPIN(changeSecretPinReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject changeSecretPinRes) {
                        dialog2.dismiss();
                        if (mode==1) alertDialogBackUp1.dismiss();
                        else if (mode==2) alertDialogBackUp2.dismiss();

                        if (changeSecretPinRes.get("Flag").getAsString().equalsIgnoreCase("success")
                                && changeSecretPinRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            BaseUtils.customToast(context, "New PIN updated.");
                            if (mode==1) alertDialogNewPin.dismiss();
                            else if (mode==2) alertDialogChangePin.dismiss();
                        } else if(changeSecretPinRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, changeSecretPinRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog2.dismiss();
                        if (mode==1) alertDialogBackUp1.dismiss();
                        else if (mode==2) alertDialogBackUp2.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void doChangePassword(String oldPass, String newPass, String conPass) {
        BaseUtils.hideKeypad((Activity) context);
        dialog3 = BaseUtils.showProgressDialog(context,"Please wait");
        dialog3.show();
        AuthApiHelper authApiHelper = ApiClient.getClient(context).create(AuthApiHelper.class);
        ChangePasswordReq changePasswordReq = new ChangePasswordReq();
        changePasswordReq.setLoginToken(SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.LoginID));
        changePasswordReq.setOldPassword(oldPass);
        changePasswordReq.setNewPassword(newPass);
        changePasswordReq.setConfirmNewPassword(conPass);

        Single<JsonObject> observable = authApiHelper.changePassword(changePasswordReq);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(JsonObject changePasswordRes) {
                        dialog3.dismiss();
                        if (changePasswordRes.get("Flag").getAsString().equals("success")
                                && changePasswordRes.get("SessionFlag").getAsString().equalsIgnoreCase("1")) {
                            alertDialogChangePass.dismiss();
                            BaseUtils.customToast(context, changePasswordRes.get("Message").getAsString());
                        } else if(changePasswordRes.get("SessionFlag").getAsString().equalsIgnoreCase("2")) {
                            BaseUtils.customToast(context,context.getResources().getString(R.string.error_sessionExpire));
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.isLogin,"0");
                            SharedPrefUtils.saveToPrefs(context,SharedPrefUtils.LoginID,"");

                            Intent intent = new Intent(context, EnterScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else BaseUtils.customToast(context, changePasswordRes.get("Message").getAsString());
                    }
                    @Override
                    public void onError(Throwable e) {
                        dialog3.dismiss();
                        BaseUtils.customToast(context, context.getResources().getString(R.string.errorOccur));
                    }
                });
    }
}
