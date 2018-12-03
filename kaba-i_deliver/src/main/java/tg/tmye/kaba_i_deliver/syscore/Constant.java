package tg.tmye.kaba_i_deliver.syscore;

/**
 * By abiguime on 24/05/2018.
 * email: 2597434002@qq.com
 */

public class Constant {
    public static final String APP_TAG = "XXX_DELIVERY_KB";
    //    public static final String SERVER_ADDRESS = "http://192.168.1.94:8000";
//    public static final String SERVER_ADDRESS = "http://192.168.1.94:8001";
//    public static final String SERVER_ADDRESS = "http://10.0.2.2:8001";
//public static final String SERVER_ADDRESS = "http://192.168.1.201:8000";

        public static final String ip_address = "app1.kaba-delivery.com"; // prod server
//    public static final String ip_address = "dev.kaba-delivery.com"; // dev server

    public static final String SERVER_ADDRESS = "http://"+ip_address;
    public static final String SERVER_HTTPS_ADDRESS = "https://"+ip_address;

    public static final String CHANNEL_ID = "kaba-restaurant-notification-channel-id";
    public static final String CHANNEL_NAME = "kaba-channel";
}
