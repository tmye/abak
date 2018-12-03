package tg.tmye.kaba.restaurant.data.command;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba.restaurant.data.Restaurant.RestaurantEntity;
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


    protected Command(Parcel in) {
        id = in.readInt();
        customer_username = in.readString();
        state = in.readInt();
        shipping_address = in.readParcelable(DeliveryAddress.class.getClassLoader());
        food_list = in.createTypedArrayList(BasketInItem.CREATOR);
        total = in.readString();
        last_update = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(customer_username);
        parcel.writeInt(state);
        parcel.writeParcelable(shipping_address, i);
        parcel.writeTypedList(food_list);
        parcel.writeString(total);
        parcel.writeString(last_update);
    }
}