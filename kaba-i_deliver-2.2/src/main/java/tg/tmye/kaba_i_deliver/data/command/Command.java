package tg.tmye.kaba_i_deliver.data.command;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryAddress;
import tg.tmye.kaba_i_deliver.data.delivery.DeliveryTimeFrame;
import tg.tmye.kaba_i_deliver.data.delivery.KabaShippingMan;
import tg.tmye.kaba_i_deliver.data.shoppingcart.BasketInItem;


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

    // normal one
    public String total_pricing;
    public String shipping_pricing;
    public String food_pricing;

    // preorder case
    public String preorder_total_pricing;
    public String preorder_shipping_pricing;
    public String preorder_food_pricing;

    // promotion case
    public String promotion_total_pricing;
    public String promotion_shipping_pricing;
    public String promotion_food_pricing;

    // differents cases
    public int is_preorder = 0;
    public int is_promotion = 0;


    public String last_update;
    public KabaShippingMan livreur;
    public boolean is_payed_at_arrival = false;
    /* clée de la commande */
    public String passphrase = "~";
    public int reason;
    public String infos;
    public String preorder_discount;
    public int preorder = 0;///////
    public DeliveryTimeFrame preorder_hour;
    public String remise;

    // adding
    public boolean is_email_account; //
    public  boolean have_billing_discount; //

    public  String email_shipping_pricing;
    public  String promotion_email_shipping_pricing;
    public  String phoneNumber_shipping_pricing;
    public  String promotion_phoneNumber_shipping_pricing;
    public  String extra_shipping_pricing;
    public String shipping_pricing_minus_extra;


    protected Command(Parcel in) {
        id = in.readInt();
        restaurant_id = in.readInt();
        state = in.readInt();
        shipping_address = in.readParcelable(DeliveryAddress.class.getClassLoader());
        restaurant_entity = in.readParcelable(RestaurantEntity.class.getClassLoader());
        food_list = in.createTypedArrayList(BasketInItem.CREATOR);
        total_pricing = in.readString();
        shipping_pricing = in.readString();
        food_pricing = in.readString();
        preorder_total_pricing = in.readString();
        preorder_shipping_pricing = in.readString();
        preorder_food_pricing = in.readString();
        promotion_total_pricing = in.readString();
        promotion_shipping_pricing = in.readString();
        promotion_food_pricing = in.readString();
        is_preorder = in.readInt();
        is_promotion = in.readInt();
        last_update = in.readString();
        is_payed_at_arrival = in.readByte() != 0;
        passphrase = in.readString();
        reason = in.readInt();
        infos = in.readString();
        preorder_discount = in.readString();
        preorder = in.readInt();
        preorder_hour = in.readParcelable(DeliveryTimeFrame.class.getClassLoader());
        remise = in.readString();
        is_email_account = in.readByte() != 0;
        have_billing_discount = in.readByte() != 0;
        email_shipping_pricing = in.readString();
        promotion_email_shipping_pricing = in.readString();
        phoneNumber_shipping_pricing = in.readString();
        promotion_phoneNumber_shipping_pricing = in.readString();
        extra_shipping_pricing = in.readString();
        shipping_pricing_minus_extra = in.readString();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(restaurant_id);
        dest.writeInt(state);
        dest.writeParcelable(shipping_address, flags);
        dest.writeParcelable(restaurant_entity, flags);
        dest.writeTypedList(food_list);
        dest.writeString(total_pricing);
        dest.writeString(shipping_pricing);
        dest.writeString(food_pricing);
        dest.writeString(preorder_total_pricing);
        dest.writeString(preorder_shipping_pricing);
        dest.writeString(preorder_food_pricing);
        dest.writeString(promotion_total_pricing);
        dest.writeString(promotion_shipping_pricing);
        dest.writeString(promotion_food_pricing);
        dest.writeInt(is_preorder);
        dest.writeInt(is_promotion);
        dest.writeString(last_update);
        dest.writeByte((byte) (is_payed_at_arrival ? 1 : 0));
        dest.writeString(passphrase);
        dest.writeInt(reason);
        dest.writeString(infos);
        dest.writeString(preorder_discount);
        dest.writeInt(preorder);
        dest.writeParcelable(preorder_hour, flags);
        dest.writeString(remise);
        dest.writeByte((byte) (is_email_account ? 1 : 0));
        dest.writeByte((byte) (have_billing_discount ? 1 : 0));
        dest.writeString(email_shipping_pricing);
        dest.writeString(promotion_email_shipping_pricing);
        dest.writeString(phoneNumber_shipping_pricing);
        dest.writeString(promotion_phoneNumber_shipping_pricing);
        dest.writeString(extra_shipping_pricing);
        dest.writeString(shipping_pricing_minus_extra);
    }
}