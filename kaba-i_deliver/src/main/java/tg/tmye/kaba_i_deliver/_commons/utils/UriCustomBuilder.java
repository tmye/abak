package tg.tmye.kaba_i_deliver._commons.utils;

import android.net.Uri;

import tg.tmye.kaba_i_deliver.syscore.Config;


/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */

public class UriCustomBuilder {


    public static Uri buildToRestaurantMenuUri (String restaurantId) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Config.APP_SCHEME)
                .authority(Config.APP_AUTHORITY)
                .appendPath(Config.PATH_RESTAURANT)
                .appendPath(Config.PATH_MENU)
                .appendQueryParameter(Config.PARAMS_RESTAURANT_ID, restaurantId);
        return builder.build();
    }
}
