package com.leo.tea.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by my on 2016/11/16.
 */

public class IsNetConntectedUtils {


            public static boolean isNetConntected(Context context){


                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                int type = networkInfo.getType();

                switch (type){

                    case ConnectivityManager.TYPE_MOBILE:

                        return true;
                    case ConnectivityManager.TYPE_WIFI:

                        return true;

                }
                return false;

            }


}
