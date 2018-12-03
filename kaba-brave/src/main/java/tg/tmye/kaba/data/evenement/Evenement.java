package tg.tmye.kaba.data.evenement;

import android.os.Parcel;
import android.os.Parcelable;

import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.advert.AdsBanner;

/**
 * By abiguime on 06/07/2018.
 * email: 2597434002@qq.com
 */
public class Evenement extends AdsBanner implements Parcelable {

    /*  "id": 1,
            "name": "Promo Coca",
            "description": "Promo",
            "category": "Food",
            "pic": "event_picture/5b44916f9c94a547295576.jpeg"*/

    public String category, created_at;

    protected Evenement(Parcel in) {
        super(in);
        category = in.readString();
        created_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(category);
        dest.writeString(created_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Evenement> CREATOR = new Creator<Evenement>() {
        @Override
        public Evenement createFromParcel(Parcel in) {
            return new Evenement(in);
        }

        @Override
        public Evenement[] newArray(int size) {
            return new Evenement[size];
        }
    };
}
