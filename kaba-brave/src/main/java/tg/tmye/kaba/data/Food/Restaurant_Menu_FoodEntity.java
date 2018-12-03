package tg.tmye.kaba.data.Food;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */

public class Restaurant_Menu_FoodEntity implements Parcelable {

    public long id;

    public String name;

    public String price;

    public String pic;

    public String details;

    public long menu_id;

    public long restaurant_id;

    public String description;

    public List<String> food_details_pictures;

    public List<Food_Tag> food_tags;

    public int is_favorite = 0;

    public double stars;

    /* restaurant entity */
    public RestaurantEntity restaurant_entity;

    public Restaurant_Menu_FoodEntity () {

    }

    protected Restaurant_Menu_FoodEntity(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readString();
        pic = in.readString();
        details = in.readString();
        menu_id = in.readLong();
        restaurant_id = in.readLong();
        description = in.readString();
        food_details_pictures = in.createStringArrayList();
        stars = in.readDouble();
    }

    public static final Creator<Restaurant_Menu_FoodEntity> CREATOR = new Creator<Restaurant_Menu_FoodEntity>() {
        @Override
        public Restaurant_Menu_FoodEntity createFromParcel(Parcel in) {
            return new Restaurant_Menu_FoodEntity(in);
        }

        @Override
        public Restaurant_Menu_FoodEntity[] newArray(int size) {
            return new Restaurant_Menu_FoodEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(pic);
        dest.writeString(details);
        dest.writeLong(menu_id);
        dest.writeLong(restaurant_id);
        dest.writeString(description);
        dest.writeStringList(food_details_pictures);
        dest.writeDouble(stars);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long get_id() {
        return this.id;
    }

    public void set_id(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getMenu_id() {
        return this.menu_id;
    }

    public void setMenu_id(long menu_id) {
        this.menu_id = menu_id;
    }

    public long getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFood_details_pictures() {
        return this.food_details_pictures;
    }

    public void setFood_details_pictures(List<String> food_details_pictures) {
        this.food_details_pictures = food_details_pictures;
    }

    public double getStars() {
        return this.stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
