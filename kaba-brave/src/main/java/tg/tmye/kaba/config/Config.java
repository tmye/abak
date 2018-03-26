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


    /* shared preferences */
    public static final String SYS_SHARED_PREFS = "kb.sp.2017";
    public static final String LAST_HOME_PAGE_JSON = "LAST_HOME_PAGE_JSON";
    public static String USER_SHARED_PREFS = "kb.user";
    public static final String DAILY_RESTAURANTS_SP_VAL = "DAILY_RESTAURANTS_SP_VAL";
    public static final String HOMEPAGE_SP_VAL = "HOMEPAGE_SP_VAL";
    public static final String SYSTOKEN = "SYSTOKEN";
    public static final String HOME_SWITCH_FRAG_PREVIOUS = "HOME_SWITCH_FRAG_PREVIOUS";
    public static final String HOME_SWITCH_FRAG_DESTINATION = "HOME_SWITCH_FRAG_DESTINATION";


    // for result codes
    public static final int LOGIN_SUCCESS = 3;
    public static final int LOGIN_FAILURE = 4;


    /* update home */
    public static final String LINK_HOME_PAGE = Constant.SERVER_ADDRESS+
            "/sample/home_page_sample.json";

    /* get restaurant db */
    public static final String LINK_RESTO_FOOD_DB = Constant.SERVER_ADDRESS+
            "/sample/restaurant_menu_sample.json";

    /* get current command db */
    public static final String LINK_MY_COMMANDS = Constant.SERVER_ADDRESS+
                "/sample/commands.json";

    /* get current basket content */
    public static final String LINK_MY_BASKET = Constant.SERVER_ADDRESS+
            "/sample/shoppingcart.json";

    /* get current horoscope content */
    public static final String LINK_MY_HOROSCOPE = Constant.SERVER_ADDRESS+
            "/sample/horoscope.json";

    /* get current favorite content */
    public static final String LINK_MY_FAVORITE = Constant.SERVER_ADDRESS+
            "/sample/favorite.json";

    /* get current adresses content */
    public static final String LINK_MY_ADRESSES = Constant.SERVER_ADDRESS+
            "/sample/myaddresses.json";

    /* get current account informations */
    public static final String LINK_MY_ACCOUNT_INFO = Constant.SERVER_ADDRESS+
            "/sample/useraccount.json";

    /* user login */
    public static final String LINK_LOGIN_USER =  Constant.SERVER_ADDRESS+
            "/mobile/api/login_check";

    /* glide config */
    public static String GLIDE_CACHE_FOLDER = "kbCache";

}
