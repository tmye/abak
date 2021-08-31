package tg.tmye.kaba_i_deliver.syscore;

/**
 * By abiguime on 24/05/2018.
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


    public static final String PHONE_FIREBASE_PUSH_TOKEN = "PHONE_FIREBASE_PUSH_TOKEN";
    public static final String FIREBASE_PUSH_SHPF = "FIREBASE_PUSH_SHPF";
    public static final String PHONE_IS_OK_WITH_SERVER = "PHONE_IS_OK_WITH_SERVER";
    public static final String GLIDE_CACHE_FOLDER = "restaurantCache";
    public static final String DELIVERMAN_SHARED_PREFS = "DELIVERMAN_SHARED_PREFS" ;

    public static final String DELIVERMAN_ID = "DELIVERMAN_ID";
    public static final String DELIVERMAN_NAME = "DELIVERMAN_NAME";
    public static final String DELIVERMAN_ICON_PICTURE = "DELIVERMAN_PICTURE";
    public static final String DELIVERMAN_EMAIL = "DELIVERMAN_PICTURE";
    public static final String DELIVERMAN_DESCRIPTION = "DELIVERMAN_PICTURE";
    public static final String DELIVERMAN_WORKING_HOUR = "DELIVERMAN_PICTURE";
    public static final String DELIVERMAN_THEME = "DELIVERMAN_PICTURE";
    public static final String DELIVERMAN_ADDRESS = "DELIVERMAN_PICTURE";
    public static final String VEHICULE_SERIAL = "VEHICULE_SERIAL";
    public static final String DELIVERY_MODE_ON_OFF = "DELIVERY_MODE_ON_OFF";


    public static final String DELIVERMAN_SHIPPING_MODE_ENABLED = "DELIVERMAN_SHIPPING_MODE_ENABLED";

    public static final String SYSTOKEN = "SYSTOKEN";

    public static final String LINK_DELIVERYMAN_GET_MY_COMMANDS = Constant.SERVER_ADDRESS + "/livreur/api/command/v2/get";

    public static final String LINK_GET_COMMAND_DETAILS = Constant.SERVER_ADDRESS + "/livreur/api/command/details/get";

    public static final String LINK_DELIVER_MAN_START_SHIPPING = Constant.SERVER_ADDRESS+"/livreur/api/start-shipping";

    public static final String LINK_DELIVER_MAN_STOP_SHIPPING = Constant.SERVER_ADDRESS+"/livreur/api/stop-shipping";

    public static final String LINK_DELIVERMAN_LOGIN = Constant.SERVER_HTTPS_ADDRESS+ "/livreur/api/login_check";

    public static final String LINK_PHONE_UPDATE_SERVER_PUSH_TOKEN = Constant.SERVER_ADDRESS + "/livreur/api/device/add";

    public static final String LINK_SET_COMMAND_TO_SHIPPING = Constant.SERVER_ADDRESS + "/livreur/api/command/endShipping";

    public static final String LINK_START_SHIPPING =  Constant.SERVER_ADDRESS + "/livreur/api/command/auto-assign";

    public static final String LINK_RESTAURANT_LIST =  Constant.SERVER_ADDRESS + "/livreur/api/restaurants/get";

    public static final String LINK_REFUND = Constant.SERVER_ADDRESS + "/livreur/api/credit/customer";

    public static final String LINK_DEBIT = Constant.SERVER_ADDRESS + "/livreur/api/debit/customer";

    public static final String LINK_END_HSN = Constant.SERVER_ADDRESS + "/livreur/api/hsn-command/endShipping";

    public static final String LINK_START_SERVICE = Constant.SERVER_ADDRESS + "/livreur/api/start-service";

    public static final String LINK_STOP_SERVICE = Constant.SERVER_ADDRESS + "/livreur/api/stop-service";

    public static final String LINK_SEARCH_STATS = Constant.SERVER_ADDRESS + "/livreur/api/command/history/get";

}
