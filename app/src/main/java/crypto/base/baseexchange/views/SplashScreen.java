package crypto.base.baseexchange.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.gson.JsonObject;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.ApiClient;
import crypto.base.baseexchange.api.AuthApiHelper;
import crypto.base.baseexchange.utils.BaseActivity;
import crypto.base.baseexchange.utils.BaseUtils;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashScreen extends BaseActivity {
    private FrameLayout frame_splash;
    private String currentVersion, latestVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        hideKeypad();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation splashAnimation = AnimationUtils.loadAnimation(this,R.anim.splash_transition);
        frame_splash = findViewById(R.id.frame_splash);
        frame_splash.startAnimation(splashAnimation);

        /*
         * handler - 3 seconds
         * check internet after handler
         * if no internet - handler (2sec)-> loop again
         * if yes internet - check login-> destroy splash-> proceed
         * */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    getCurrentVersion();
                    getCurrentVer();
                } else {
                    Log.e("splash", "no internet");
                    BaseUtils.showNoInternetSnackbar(frame_splash, getResources().getString(R.string.error_noInternet));
                }
            }
        }, 2000);
    }

    public String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            Log.e("getUniqueIMEIId", "imei =" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && permissions[0].equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE) && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            String imei = getUniqueIMEIId(SplashScreen.this);
            SharedPrefUtils.saveToPrefs(SplashScreen.this, SharedPrefUtils.IMEI, imei);
        } else finish();
    }

    public void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        try { pInfo = pm.getPackageInfo(this.getPackageName(), 0); }
        catch (PackageManager.NameNotFoundException e1) { e1.printStackTrace(); }
        currentVersion = pInfo.versionName; }

    private void getCurrentVer() {
        AuthApiHelper authApiHelper = ApiClient.getClient(SplashScreen.this).create(AuthApiHelper.class);

        Single<JsonObject> observable = authApiHelper.getAppDetails();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(JsonObject appDetailsRes) {
                        if (appDetailsRes.get("Flag").getAsString().equalsIgnoreCase("success")) {
                            latestVersion = appDetailsRes.get("Version").getAsString();
                            SharedPrefUtils.saveToPrefs(getApplicationContext(), SharedPrefUtils.playStoreLink, appDetailsRes.get("PlayStoreLink").getAsString());
                            Log.e("playStoreLink",SharedPrefUtils.getFromPrefs(getApplicationContext(),SharedPrefUtils.playStoreLink));

                            Log.e("current version",currentVersion);
                            Log.e("latest version",latestVersion);
                            if (!currentVersion.equals(latestVersion)) showUpdateDialog();
                            else {
                                if (!SharedPrefUtils.containsKeyInPrefs(SplashScreen.this, SharedPrefUtils.IMEI)
                                        || SharedPrefUtils.getFromPrefs(SplashScreen.this, SharedPrefUtils.IMEI).isEmpty()) {
                                    SharedPrefUtils.saveToPrefs(SplashScreen.this, SharedPrefUtils.IMEI, "-");
                                    SharedPrefUtils.saveToPrefs(SplashScreen.this, SharedPrefUtils.isLogin, "0");

                                    startActivity(new Intent(SplashScreen.this, HomeScreen.class));
                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashScreen.this, HomeScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else BaseUtils.customToast(SplashScreen.this, appDetailsRes.get("Message").getAsString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseUtils.customToast(SplashScreen.this, getResources().getString(R.string.errorOccur));
                    }
                });
    }

    private void showUpdateDialog() {
        Log.e("version","current version : "+currentVersion+"\nplay store version : "+latestVersion);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.updateMsg));
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        (SharedPrefUtils.getFromPrefs(getApplicationContext(),SharedPrefUtils.playStoreLink))));
                dialog.dismiss();
            }
        });
        /*builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SplashScreen.this,HomeScreen.class));
                finish();
            }
        });*/
        builder.setCancelable(false);
        builder.show();
    }
}
