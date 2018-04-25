package tg.tmye.kaba.data.favorite;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 27/02/2018.
 * email: 2597434002@qq.com
 */

public class Favorite implements Parcelable {

    public int restaurant_id;
    public RestaurantEntity restaurant_entity;
    public List<Restaurant_Menu_FoodEntity> food_list;


    protected Favorite(Parcel in) {
        restaurant_id = in.readInt();
        restaurant_entity = in.readParcelable(RestaurantEntity.class.getClassLoader());
        food_list = in.createTypedArrayList(Restaurant_Menu_FoodEntity.CREATOR);
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(restaurant_id);
        parcel.writeParcelable(restaurant_entity, i);
        parcel.writeTypedList(food_list);
    }
}
