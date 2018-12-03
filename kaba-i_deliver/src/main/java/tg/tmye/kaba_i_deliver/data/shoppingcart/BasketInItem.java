package tg.tmye.kaba_i_deliver.data.shoppingcart;

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

    public String food_description;

    public List<String> food_details_pictures;

    public double stars;

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
        food_description = in.readString();
        food_details_pictures = in.createStringArrayList();
        stars = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(pic);
        dest.writeString(details);
        dest.writeInt(quantity);
        dest.writeLong(menu_id);
        dest.writeLong(restaurant_id);
        dest.writeString(food_description);
        dest.writeStringList(food_details_pictures);
        dest.writeDouble(stars);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
