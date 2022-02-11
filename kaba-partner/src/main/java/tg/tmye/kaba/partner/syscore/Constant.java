package tg.tmye.kaba.partner.syscore;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class Constant {

    public static final String APP_TAG = "XXX_RESTAURANT_KB";

//  public static final String ip_address = "app.kaba-delivery.com"; // prod server
//  public static final String stats_server_ip_address = "stats.kaba-delivery.com"; // prod server
//    public static final boolean DEBUG = false;

    public static final String ip_address = "dev.kaba-delivery.com"; // dev server
    public static final String stats_server_ip_address = "dev.stats.kaba-delivery.com"; // dev server
public static final boolean DEBUG = true;

    public static final String SERVER_ADDRESS = "https://"+ip_address;
    public static final String SERVER_HTTPS_ADDRESS = "https://"+ip_address;
    public static final String CHANNEL_ID = "kaba-restaurant-notification-channel-id";
    public static final String CHANNEL_NAME = "kaba-channel";

    public static final boolean IS_RESTAURANT_APP = true;


}
