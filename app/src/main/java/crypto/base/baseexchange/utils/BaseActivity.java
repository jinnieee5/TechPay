package crypto.base.baseexchange.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeypad();
    }

    public void hideKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public boolean isOnline() {
        boolean isOnline;
        Log.e("splash", "isOnline() called.");
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.e("splash", "ConnectivityManager - " + cm.toString());
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Log.e("splash", "NetworkInfo - " + netInfo);
//        Log.e("splash", "first - " + (netInfo!=null) + ", second - " + netInfo.isConnectedOrConnecting());
//        return netInfo != null && netInfo.isConnectedOrConnecting();
        if(netInfo == null){
            isOnline = false;
        }else {
            isOnline = true;
        }
//        return netInfo != null && netInfo.isConnectedOrConnecting();
        return isOnline;
    }
}
