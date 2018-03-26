package tg.experta.kaba._commons.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import tg.experta.kaba._commons.cviews.CustomTabLayout;

/**
 * By abiguime on 2017/12/6.
 * email: 2597434002@qq.com
 */

public class UtilFunctions {

    public static int[] getScreenSize(Context context) {
        int[] screenSize = new int[2];
        screenSize[CustomTabLayout.WIDTH_INDEX] = context.getResources().getDisplayMetrics().widthPixels;
        screenSize[CustomTabLayout.WIDTH_INDEX+1] = context.getResources().getDisplayMetrics().heightPixels;
        return screenSize;
    }

//    public static int dp2Pixels(Context context, int lil_round_size) {
//    }


    public static boolean checkLink(String link) {
        return true;
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation(Context ctx) {
     /*   if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }*/
    }

    public static String randomToken() {
        String token = "";
        for (int i = 0; i < 33; i++) {
            token+= (""+(i%10));
        }
        return token;
    }

    /*
    * if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    * */
}
