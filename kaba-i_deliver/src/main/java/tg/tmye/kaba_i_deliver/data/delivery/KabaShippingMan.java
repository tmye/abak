package tg.tmye.kaba_i_deliver.data.delivery;

/**
 * By abiguime on 29/05/2018.
 * email: 2597434002@qq.com
 */

public class KabaShippingMan {

    public int id;
    public String name;
    public String current_location;
    public String vehicle_serial_code;
    public String last_update;
    public String workcontact;

    public boolean is_available = false; // 0 unchecked, 1 checked
}
