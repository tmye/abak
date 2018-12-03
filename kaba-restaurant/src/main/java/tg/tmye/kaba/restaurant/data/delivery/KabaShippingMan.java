package tg.tmye.kaba.restaurant.data.delivery;

/**
 * By abiguime on 29/05/2018.
 * email: 2597434002@qq.com
 */

public class KabaShippingMan {

    public static int SELECTED = 1, NO_SELECTED = 0;

    public int id;
    public String name;
    public String current_location;
    public String vehicle_serial_code;
    public String last_update;
    public String workcontact;
    public String pic;

    public boolean is_available = false; // 0 unchecked, 1 checked
    public int is_selected = NO_SELECTED;
}
