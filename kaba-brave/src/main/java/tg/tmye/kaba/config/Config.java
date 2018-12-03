package tg.tmye.kaba.config;

/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */

public class Config {

    public static final String APP_SCHEME = "experta";
    public static final String APP_AUTHORITY = "authority";
    public static final String PATH_MENU = "menu";
    public static final String PATH_RESTAURANT = "restaurant";
    public static final String PARAMS_RESTAURANT_ID = "PARAMS_RESTAURANT_ID";
    public static final String KABA_CUSTOM_NETWORK_THREAD = "KABA_CUSTOM_NETWORK_THREAD";
    public static final String KABA_CUSTOM_DATABASE_THREAD = "KABA_CUSTOM_DATABASE_THREAD";
    public static final String KABA_PARA_THREAD = "KABA_PARA_THREAD";


    /* shared preferences */
    public static final String SYS_SHARED_PREFS = "kb.sp.2017";
    public static final String LAST_HOME_PAGE_JSON = "LAST_HOME_PAGE_JSON";
    public static final String LAST_RESTAURANT_PAGE_JSON = "LAST_RESTAURANT_PAGE_JSON";
    public static final String RESTAURANT_DB_SERIAL = "RESTAURANT_DB_SERIAL";


    public static final String PHONE_FIREBASE_PUSH_TOKEN = "PHONE_FIREBASE_PUSH_TOKEN";
    public static final String FIREBASE_PUSH_SHPF = "FIREBASE_PUSH_SHPF";
    public static final String PHONE_IS_OK_WITH_SERVER = "PHONE_IS_OK_WITH_SERVER";
    public static final String PRE_LOGIN_SHARED_PREFS = "PRE_LOGIN_SHARED_PREFS";
    public static final String BALANCE_FIELD = "BALANCE";
    public static final String PRE_RECOVER_PASSWORD_SHARED_PREFS = "PRE_RECOVER_PASSWORD_SHARED_PREFS";


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

    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    public static final String CUSTOMER_PHONE_NUMBER = "CUSTOMER_PHONE_NUMBER";
    public static final String CUSTOMER_USERNAME = "CUSTOMER_USERNAME";
    public static final String CUSTOMER_PROFILE_PICTURE = "CUSTOMER_PROFILE_PICTURE";
    public static final String CUSTOMER_BIRTHDAY = "CUSTOMER_BIRTHDAY";
    public static final String CUSTOMER_THEME_PICTURE = "CUSTOMER_THEME_PICTURE";
    public static final String CUSTOMER_GENDER = "CUSTOMER_GENDER";
    public static final String CUSTOMER_IS_GENDER_TO_SET = "CUSTOMER_IS_GENDER_TO_SET";
    public static final String CUSTOMER_NICKNAME = "CUSTOMER_NICKNAME";


    /* update home */
    public static final String LINK_HOME_PAGE = Constant.SERVER_ADDRESS+
            "/api/front/get";

    /* get restaurant db */
    public static final String LINK_RESTO_FOOD_DB = Constant.SERVER_ADDRESS+
            "/sample/restaurant_menu_sample.json";

    /* get current command db */
    public static final String LINK_MY_COMMANDS_GET_CURRENT = Constant.SERVER_ADDRESS+
            "/mobile/api/command/get";

    /* get all commands list */
    public static final String LINK_GET_ALL_COMMAND_LIST = Constant.SERVER_ADDRESS+
            "/mobile/api/command/all/get";

    /* create command db */
    public static final String LINK_CREATE_COMMAND = Constant.SERVER_ADDRESS_SECURE +
            "/mobile/api/command/create";

    /* get command details */
    public static final String LINK_GET_COMMAND_DETAILS =  Constant.SERVER_ADDRESS+
            "/mobile/api/command/details/get";

    /* get current basket content */
    public static final String LINK_MY_BASKET_GET = Constant.SERVER_ADDRESS+
            "/mobile/api/basket/get";

    /* get current basket content */
    public static final String LINK_MY_BASKET_CREATE = Constant.SERVER_ADDRESS+
            "/mobile/api/basket/create";

    /* get current horoscope content */
    public static final String LINK_MY_HOROSCOPE = Constant.SERVER_ADDRESS+
            "/sample/horoscope.json";

    /* get current favorite content */
    public static final String LINK_MY_FAVORITE = Constant.SERVER_ADDRESS+
            "/mobile/api/getFavorites";

    /* set favorite */
    public static final String LINK_SET_FAVORITE = Constant.SERVER_ADDRESS + "/mobile/api/setFavorite";

    /* get current adresses content */
    public static final String LINK_GET_ADRESSES = Constant.SERVER_ADDRESS+
            "/mobile/api/getAdresses";

    /* create new adresses content */
    public static final String LINK_CREATE_NEW_ADRESS = Constant.SERVER_ADDRESS+
            "/mobile/api/createAdresses";

    /* delete address */
    public static final String LINK_DELETE_ADRESS = Constant.SERVER_ADDRESS+
            "/mobile/api/deleteAdresses";

    /* get current account informations */
    public static final String LINK_MY_ACCOUNT_INFO = Constant.SERVER_ADDRESS+
            "/sample/useraccount.json";

    /* menu food*/
    public static final String LINK_MENU_BY_RESTAURANT_ID = Constant.SERVER_ADDRESS+
            "/api/menu/get";

    public static final String LINK_MENU_BY_ID = Constant.SERVER_ADDRESS+
            "/api/menu/get/id";

    public static final String LINK_RESTO_LIST = Constant.SERVER_ADDRESS+
            "/api/restaurant/get";

    public static final String LINK_RESTO_LIST_V2 = Constant.SERVER_ADDRESS+
            "/api/restaurant/v2/get";

    /* glide config */
    public static String GLIDE_CACHE_FOLDER = "kbCache";


    /* user login */
    public static final String LINK_USER_LOGIN =  Constant.SERVER_ADDRESS_SECURE+
            "/mobile/api/login_check";

    /* user register */
    public static final String LINK_USER_REGISTER =  Constant.SERVER_ADDRESS_SECURE+
            "/api/user/register";

    /* register push token */
    public static final String LINK_REGISTER_PUSH_TOKEN = Constant.SERVER_ADDRESS+
            "/api/device/add";

    /* update phone / user push_token */
    public static final String LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN = "";


    /* get notification food data */
    public static final String LINK_NOTIFICATION_FOOD_DATA =  Constant.SERVER_ADDRESS+
            "/notification/food";

    /* get notification menu data */
    public static final String LINK_NOTIFICATION_RESTAURANT_DATA = Constant.SERVER_ADDRESS+
            "/notification/restaurant";

    /* get notification menu data */
    public static final String LINK_NOTIFICATION_MENU_DATA = Constant.SERVER_ADDRESS+
            "/notification/menu";

    /* delete basket item */
    public static final String LINK_MY_BASKET_DELETE = Constant.SERVER_ADDRESS+
            "/mobile/api/basket/delete";

    /* update user informations */
    public static final String LINK_UPDATE_USER_INFORMATIONS = Constant.SERVER_ADDRESS+
            "/mobile/api/user/change";

    /* get article informations */
    public static final String LINK_ARTICLE_INFORMATIONS = Constant.SERVER_ADDRESS+
            "/api/article/get";

    /* get position details */
    public static final String LINK_GET_LOCATION_DETAILS = Constant.SERVER_ADDRESS+
            "/mobile/api/district/get";

    public static final String LINK_CHECK_RESTAURANT_IS_OPEN = Constant.SERVER_ADDRESS+
            "/api/resto/state/get";

    public static final String LINK_COMPUTE_BILLING = Constant.SERVER_ADDRESS+
            "/mobile/api/commandBilling/get";

    public static final String LINK_GET_LASTEST_FEEDS = Constant.SERVER_ADDRESS+
            "/mobile/api/feeds/get";

    public static final String LINK_SEND_VERIFCATION_SMS = Constant.SERVER_ADDRESS+
            "/api/code/request";

    public static final String LINK_POST_SUGGESTION = Constant.SERVER_ADDRESS +
            "/mobile/api/add/suggestion";

    public static final String LINK_GET_BESTSELLERS_LIST = Constant.SERVER_ADDRESS +
            "/api/food/rating/get";

    /* get notification food data */
    public static final String LINK_GET_FOOD_DETAILS_LOGGED =  Constant.SERVER_ADDRESS+
            "/mobile/api/food/details/get";

    public static final String LINK_GET_FOOD_DETAILS_SIMPLE =  Constant.SERVER_ADDRESS+
            "/api/food/details/get";

    public static final String LINK_GET_EVENEMENTS_LIST = Constant.SERVER_ADDRESS +
            "/api/event/get";

    public static final String LINK_CHECK_VERIFCATION_CODE = Constant.SERVER_ADDRESS +
            "/api/code/check";

    /* pay addresses */
    public static final String LINK_GET_TRANSACTION_HISTORY = Constant.PAY_SERVER_ADDRESS_SECURE +
            "/mobile/api/user/transaction/history";

    public static final String LINK_GET_BALANCE =  Constant.PAY_SERVER_ADDRESS_SECURE +
            "/mobile/api/user/transaction/balance/get";

    /* top up */
    public static final String LINK_GET_TOPUP_CHOICES = LINK_HOME_PAGE;

    /* flooz top up */
    public static final String LINK_TOPUP_FLOOZ = Constant.PAY_SERVER_ADDRESS_SECURE+"/mobile/api/sms/payment/init";

    /* t-money top up */
    public static final String LINK_TOPUP_TMONEY = Constant.PAY_SERVER_ADDRESS_SECURE+"/mobile/api/web/payment/init";

}
