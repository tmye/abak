package tg.tmye.kaba_i_deliver.data.command;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryAddress;
import tg.tmye.kaba_i_deliver.data.shoppingcart.BasketInItem;


/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

public class Command implements Parcelable {

    public int id;
    public RestaurantEntity restaurant_entity;
    public String customer_username;
    public int state;
    /* shipping address */
    public DeliveryAddress shipping_address;
    public List<BasketInItem> food_list;
    public String total;
    public String last_update;

    /* clée de la commande */
    public String passphrase;

    protected Command(Parcel in) {
        id = in.readInt();
        restaurant_entity = in.readParcelable(RestaurantEntity.class.getClassLoader());
        customer_username = in.readString();
        state = in.readInt();
        shipping_address = in.readParcelable(DeliveryAddress.class.getClassLoader());
        food_list = in.createTypedArrayList(BasketInItem.CREATOR);
        total = in.readString();
        last_update = in.readString();
        passphrase = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(restaurant_entity, flags);
        dest.writeString(customer_username);
        dest.writeInt(state);
        dest.writeParcelable(shipping_address, flags);
        dest.writeTypedList(food_list);
        dest.writeString(total);
        dest.writeString(last_update);
        dest.writeString(passphrase);
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