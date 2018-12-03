package tg.tmye.kaba.data.delivery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 23/01/2018.
 * email: 2597434002@qq.com
 */
public class KabaShippingMan {

    public int id;
    public String name;
    public String current_location;
    public String vehicle_serial_code;
    public String last_update;
    public String workcontact;
    public String pic;

    public boolean is_available = false; // 0 unchecked, 1 checked
}
