package tg.tmye.kaba.restaurant.data.command;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba.restaurant.data.delivery.DeliveryAddress;
import tg.tmye.kaba.restaurant.data.delivery.KabaShippingMan;
import tg.tmye.kaba.restaurant.data.shoppingcart.BasketInItem;


/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

public class Command implements Parcelable {

    public int id;
    public String customer_username;
    public int state;
    /* shipping address */
    public DeliveryAddress shipping_address;
    public List<BasketInItem> food_list;
    public String total;
    public String last_update;
    public KabaShippingMan livreur;
    public boolean is_payed_at_arrival = false;



    protected Command(Parcel in) {
        id = in.readInt();
        customer_username = in.readString();
        state = in.readInt();
        shipping_address = in.readParcelable(DeliveryAddress.class.getClassLoader());
        food_list = in.createTypedArrayList(BasketInItem.CREATOR);
        total = in.readString();
        last_update = in.readString();
        is_payed_at_arrival = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(customer_username);
        dest.writeInt(state);
        dest.writeParcelable(shipping_address, flags);
        dest.writeTypedList(food_list);
        dest.writeString(total);
        dest.writeString(last_update);
        dest.writeByte((byte) (is_payed_at_arrival ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Command> CREATOR = new Creator<Command>() {
        @Override
        public Command createFromParcel(Parcel in) {
            return new Command(in);
        }

        @Override
        public Command[] newArray(int size) {
            return new Command[size];
        }
    };
}