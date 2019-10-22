package crypto.base.baseexchange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefUtils {
    public static final String IMEI="IMEI";
    public static final String isLogin="isLogin";   //0-false, 1-true
    public static final String LoginID="LoginID";
    public static final String EmailID="EmailID";
    public static final String ProfilePic="ProfilePic";
    public static final String tempWalletAddress="tempWalletAddress";
    public static final String tempTransferAmount="tempTransferAmount";
    public static final String playStoreLink="playStoreLink";

    //isLogin value
    /*
    * first time install-> 0 (default value)
    * create wallet success-> 1
    * logout click-> 0
    * login success-> 1
    * */

    public static String saveToPrefs(Context context, String key, String value) {
        /*SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);*/

        SharedPreferences prefs = context.getSharedPreferences("MySP",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
        Log.e("pref","key : "+key+" : value : "+value);
        return key;
    }

    public static String getFromPrefs(Context context, String key) {
        /*SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);*/

        SharedPreferences prefs = context.getSharedPreferences("MySP",Context.MODE_PRIVATE);
        try { return prefs.getString(key, ""); }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean containsKeyInPrefs(Context context, String key) {
        /*SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);*/

        SharedPreferences prefs = context.getSharedPreferences("MySP",Context.MODE_PRIVATE);
        return prefs.contains(key);
    }

    public static void logoutFromPrefs(Context context) {
        /*SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);*/

        SharedPreferences prefs = context.getSharedPreferences("MySP",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
