package tg.tmye.kaba.data.command;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.delivery.KabaShippingMan;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;

/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

public class Command implements Parcelable {

    public int id;
    public int restaurant_id;
    public int state;
    /* shipping address */
    public DeliveryAddress shipping_address;
    public RestaurantEntity restaurant_entity;
    public List<BasketInItem> food_list;
    public String total_pricing;
    public String shipping_pricing;
    public String last_update;
    public KabaShippingMan livreur;

    /* clée de la commande */
    public String passphrase = "~";

    protected Command(Parcel in) {
        id = in.readInt();
        restaurant_id = in.readInt();
        state = in.readInt();
        shipping_address = in.readParcelable(DeliveryAddress.class.getClassLoader());
        restaurant_entity = in.readParcelable(RestaurantEntity.class.getClassLoader());
        food_list = in.createTypedArrayList(BasketInItem.CREATOR);
        total_pricing = in.readString();
        shipping_pricing = in.readString();
        last_update = in.readString();
        passphrase = in.readString();
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
        parcel.writeInt(restaurant_id);
        parcel.writeInt(state);
        parcel.writeParcelable(shipping_address, i);
        parcel.writeParcelable(restaurant_entity, i);
        parcel.writeTypedList(food_list);
        parcel.writeString(total_pricing);
        parcel.writeString(shipping_pricing);
        parcel.writeString(last_update);
        parcel.writeString(passphrase);
    }
}
