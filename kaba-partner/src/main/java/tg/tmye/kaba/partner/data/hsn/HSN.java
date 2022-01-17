package tg.tmye.kaba.partner.data.hsn;


import android.os.Parcel;
import android.os.Parcelable;

import tg.tmye.kaba.partner.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.partner.data.delivery.KabaShippingMan;

/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

public class HSN implements Parcelable {

    public int id;
    public String resto_name;
    public RestaurantEntity restaurant_entity;
    public String food_pricing;
    public String shipping_pricing;
    public String total;
    public String infos;
    public String shipping_address;
    public String delivery_date;
    public KabaShippingMan livreur_entity;
    public int pay_at_arrival;
    public String phone_number;
    public String shipping_location_link;
    public int state; // -2 annulé, -1 en attente, 0 en livraison, 1 livreé

    protected HSN(Parcel in) {
        id = in.readInt();
        resto_name = in.readString();
        restaurant_entity = in.readParcelable(RestaurantEntity.class.getClassLoader());
        food_pricing = in.readString();
        shipping_pricing = in.readString();
        total = in.readString();
        infos = in.readString();
        shipping_address = in.readString();
        delivery_date = in.readString();
        pay_at_arrival = in.readInt();
        phone_number = in.readString();
        shipping_location_link = in.readString();
        state = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(resto_name);
        dest.writeParcelable(restaurant_entity, flags);
        dest.writeString(food_pricing);
        dest.writeString(shipping_pricing);
        dest.writeString(total);
        dest.writeString(infos);
        dest.writeString(shipping_address);
        dest.writeString(delivery_date);
        dest.writeInt(pay_at_arrival);
        dest.writeString(phone_number);
        dest.writeString(shipping_location_link);
        dest.writeInt(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HSN> CREATOR = new Creator<HSN>() {
        @Override
        public HSN createFromParcel(Parcel in) {
            return new HSN(in);
        }

        @Override
        public HSN[] newArray(int size) {
            return new HSN[size];
        }
    };
}