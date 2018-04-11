package tg.tmye.kaba.data.Menu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;

/**
 * By abiguime on 2017/12/3.
 * email: 2597434002@qq.com
 */

public class Restaurant_SubMenuEntity implements Parcelable {

    /*
    *
    *  "_id": 1,
        "name": "RICE"
    * */

    public long id;

    public String name;

    public Long restaurant_id;

    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Restaurant_Menu_FoodEntity> foods;


    private Restaurant_SubMenuEntity() {
        foods = new ArrayList<>();
    }

    protected Restaurant_SubMenuEntity(Parcel in) {
        id = in.readLong();
        name = in.readString();
        if (in.readByte() == 0) {
            restaurant_id = null;
        } else {
            restaurant_id = in.readLong();
        }
        foods = in.createTypedArrayList(Restaurant_Menu_FoodEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        if (restaurant_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(restaurant_id);
        }
        dest.writeTypedList(foods);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant_SubMenuEntity> CREATOR = new Creator<Restaurant_SubMenuEntity>() {
        @Override
        public Restaurant_SubMenuEntity createFromParcel(Parcel in) {
            return new Restaurant_SubMenuEntity(in);
        }

        @Override
        public Restaurant_SubMenuEntity[] newArray(int size) {
            return new Restaurant_SubMenuEntity[size];
        }
    };


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }


    public void setFoods(List<Restaurant_Menu_FoodEntity> foods) {
        this.foods = foods;
    }
}