package tg.tmye.kaba.partner.syscore;

import android.Manifest;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class Config {

    public static final String APP_SCHEME = "experta";
    public static final String APP_AUTHORITY = "authoricty";
    public static final String PATH_MENU = "menu";
    public static final String PATH_RESTAURANT = "restaurant";
    public static final String PARAMS_RESTAURANT_ID = "PARAMS_RESTAURANT_ID";
    public static final String KABA_CUSTOM_NETWORK_THREAD = "KABA_CUSTOM_NETWORK_THREAD";
    public static final String KABA_CUSTOM_DATABASE_THREAD = "KABA_CUSTOM_DATABASE_THREAD";


    /* shared preferences */
    public static final String SYS_SHARED_PREFS = "kb.sp.2017";
    public static final String LAST_HOME_PAGE_JSON = "LAST_HOME_PAGE_JSON";
    public static final String LAST_RESTAURANT_PAGE_JSON = "LAST_RESTAURANT_PAGE_JSON";
    public static final String RESTAURANT_DB_SERIAL = "RESTAURANT_DB_SERIAL";


    public static final String PHONE_FIREBASE_PUSH_TOKEN = "PHONE_FIREBASE_PUSH_TOKEN";
    public static final String FIREBASE_PUSH_SHPF = "FIREBASE_PUSH_SHPF";
    public static final String PHONE_IS_OK_WITH_SERVER = "PHONE_IS_OK_WITH_SERVER";
    public static final String GLIDE_CACHE_FOLDER = "restaurantCache";
    public static final String RESTAURANT_SHARED_PREFS = "RESTAURANT_SHARED_PREFS";



    public static String USER_SHARED_PREFS = "kb.user";
    public static final String RESTAURANT_LIST_SP_VAL = "RESTAURANT_LIST_SP_VAL";
    public static final String DAILY_RESTAURANTS_SP_VAL = "DAILY_RESTAURANTS_SP_VAL";
    public static final String HOMEPAGE_SP_VAL = "HOMEPAGE_SP_VAL";
    public static final String SYSTOKEN = "SYSTOKEN";
    public static final String HOME_SWITCH_FRAG_PREVIOUS = "HOME_SWITCH_FRAG_PREVIOUS";
    public static final String HOME_SWITCH_FRAG_DESTINATION = "HOME_SWITCH_FRAG_DESTINATION";

    // for result codes
    public static final int LOGIN_SUCCESS = 3;
    public static final int LOGIN_FAILURE = 4;

    public static final String RESTAURANT_ID = "RESTAURANT_ID";
    public static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    public static final String RESTAURANT_ICON_PICTURE = "RESTAURANT_PICTURE";
    public static final String RESTAURANT_EMAIL = "RESTAURANT_PICTURE";
    public static final String RESTAURANT_DESCRIPTION = "RESTAURANT_PICTURE";
    public static final String RESTAURANT_WORKING_HOUR = "RESTAURANT_PICTURE";
    public static final String RESTAURANT_THEME = "RESTAURANT_PICTURE";
    public static final String RESTAURANT_ADDRESS = "RESTAURANT_PICTURE";

    // login for restaurant
    public static final String LINK_RESTAURANT_LOGIN = Constant.SERVER_HTTPS_ADDRESS+"/resto/api/login_check";

    public static final String LINK_RESTAURANT_GET_MY_COMMANDS = Constant.SERVER_ADDRESS+"/resto/api/command/get"; ;

    public static final String LINK_GET_COMMAND_DETAILS = Constant.SERVER_ADDRESS + "/resto/api/command/details/get";

    public static final String LINK_ACCEPT_COMMAND = Constant.SERVER_ADDRESS + "/resto/api/command/accept";

    // not used recently
    public static final String LINK_SENDTOSHIPPING_COMMAND = Constant.SERVER_ADDRESS + "/resto/api/command/start-shipping";


    public static final String LINK_GET_RESTAURANT_STATS = Constant.SERVER_ADDRESS + "/resto/api/recipes/get";

//    public static final String LINK_REJECT_COMMAND = Constant.SERVER_ADDRESS + "/resto/api/command/start-shipping";
    public static final String LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN = Constant.SERVER_ADDRESS + "/resto/api/device/add";

    public static final String LINK_GET_RESTAURANT_7_STATS = Constant.SERVER_ADDRESS + "/resto/api/stat/get";

    public static final String LINK_RESTAURANT_CANCEL_COMMAND = Constant.SERVER_ADDRESS + "/resto/api/command/reject";

    public static final String LINK_RESTAURANT_CHECK_OPENED = Constant.SERVER_ADDRESS + "/resto/api/open_close";

    /* menu food*/
    public static final String LINK_MENU_BY_RESTAURANT_ID = Constant.SERVER_ADDRESS+
            "/api/menu/get";

    public static final String LINK_MENU_ADD = Constant.SERVER_ADDRESS+
            "/resto/api/menu/add";

    public static final String LINK_MENU_EDIT = Constant.SERVER_ADDRESS+
            "/resto/api/menu/edit";

    public static final String LINK_MENU_DELETE = Constant.SERVER_ADDRESS+
            "/resto/api/menu/delete";

    // restaurant specific interface
    public static final String LINK_GET_MENU_BY_ID_SPECIFIC = Constant.SERVER_ADDRESS+
            "/resto/api/menu/get";

    // restaurant specific interface
    public static final String LINK_GET_FOOD_BY_ID_SPECIFIC = Constant.SERVER_ADDRESS+
            "/resto/api/food/get";

    public static final String LINK_FOOD_ADD = Constant.SERVER_ADDRESS+
            "/resto/api/food/add";

    public static final String LINK_FOOD_EDIT = Constant.SERVER_ADDRESS+
            "/resto/api/food/edit";

    public static final String LINK_FOOD_DELETE = Constant.SERVER_ADDRESS+
            "/resto/api/food/delete";

    // get delivery districts
    public static final String LINK_GET_DELIVERY_DISTRICTS = Constant.SERVER_ADDRESS+ "/resto/api/hsn/district/list";

    // compute delivery fees
    public static final String LINK_COMPUTE_DELIVERY_FEES = Constant.SERVER_ADDRESS+ "/resto/api/hsn/billing";

    // create hsn
    public static final String LINK_CREATE_HSN = Constant.SERVER_ADDRESS+ "/resto/api/hsn/create";

    // update hsn
    public static final String LINK_UPDATE_HSN = Constant.SERVER_ADDRESS+ "/resto/api/hsn/update";


    public static final String LINK_RESTAURANT_GET_MY_HSN = Constant.SERVER_ADDRESS+"/resto/api/hsn/daily"; ;

    // get restaurant stats on a range
    public static final String LINK_GET_RESTAURANT_STATS_FROM_TO_DATE = Constant.SERVER_ADDRESS + "/resto/api/command/history";

    // update restaurant calendar
    public static final String LINK_UPDATE_RESTAURANT_CALENDAR = Constant.SERVER_ADDRESS + "/resto/api/schedule/update";

    // get restaurant calendar
    public static final String LINK_GET_RESTAURANT_CALENDAR =  Constant.SERVER_ADDRESS + "/resto/api/schedule/get";

    public static final String RESTAURANT_PROFILE_ENDPOINT_UPDATE = Constant.SERVER_ADDRESS + "/resto/api/profil/update";

    public static final String RESTAURANT_PROFILE_ENDPOINT_GET_INFOS = Constant.SERVER_ADDRESS + "/resto/api/profil";

}
