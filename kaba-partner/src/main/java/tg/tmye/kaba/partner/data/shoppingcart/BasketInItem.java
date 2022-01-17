package tg.tmye.kaba.partner.data.shoppingcart;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public class BasketInItem implements Parcelable {

    public long id;

    public String name;

    public String price;

    public String pic;

    public String details;

    public int quantity;

    public long menu_id;

    public long restaurant_id;

    public String description;

    public List<String> food_details_pictures;

    public double stars;

    public int promotion;

    public String promotion_price;

    public BasketInItem() {

    }


    protected BasketInItem(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readString();
        pic = in.readString();
        details = in.readString();
        quantity = in.readInt();
        menu_id = in.readLong();
        restaurant_id = in.readLong();
        description = in.readString();
        food_details_pictures = in.createStringArrayList();
        stars = in.readDouble();
        promotion = in.readInt();
        promotion_price = in.readString();
    }

    public static final Creator<BasketInItem> CREATOR = new Creator<BasketInItem>() {
        @Override
        public BasketInItem createFromParcel(Parcel in) {
            return new BasketInItem(in);
        }

        @Override
        public BasketInItem[] newArray(int size) {
            return new BasketInItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(pic);
        parcel.writeString(details);
        parcel.writeInt(quantity);
        parcel.writeLong(menu_id);
        parcel.writeLong(restaurant_id);
        parcel.writeString(description);
        parcel.writeStringList(food_details_pictures);
        parcel.writeDouble(stars);
        parcel.writeInt(promotion);
        parcel.writeString(promotion_price);
    }
}
