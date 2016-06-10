package com.cedrotech.traveling.util;

/**
 * Created by ivo on 11/15/14.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.cedrotech.traveling.R;
import com.facebook.AccessToken;

public class Utils {

    public static void closeApp(Activity a){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        a.startActivity(intent);
    }

    public static boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static Boolean isConnectInternet(Activity a) {
        ConnectivityManager cm =
                (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Toast.makeText(a, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        return isConnected;
    }
}

