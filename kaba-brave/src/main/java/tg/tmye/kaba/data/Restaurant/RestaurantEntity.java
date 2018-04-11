package tg.tmye.kaba.data.Restaurant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data._OtherEntities.Contact;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;

/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */
public class RestaurantEntity implements Parcelable {

    /*
    * {
         "id": 1,
                "name": "Kastas",
                "description": "The best of fast food",
                "email": "kastas2017@gmail.com",
                "working_hour": "0700-0000",
                "address": "adidogome",
                "pic": "resto_pic/re4.png",
                "main_contact": "99105978"
      }
    * */

    public long id;

    public String pic;

    public String restaurant_theme_image;

    public String name;

    public String description;

    private long contactId;

    public String address;

    public String email;

    public String main_contact;

    public String working_hour;


    public List<Restaurant_SubMenuEntity> menu;

    public RestaurantEntity() {
    }


    protected RestaurantEntity(Parcel in) {
        id = in.readLong();
        pic = in.readString();
        restaurant_theme_image = in.readString();
        name = in.readString();
        description = in.readString();
        contactId = in.readLong();
        address = in.readString();
        email = in.readString();
        main_contact = in.readString();
        working_hour = in.readString();
        menu = in.createTypedArrayList(Restaurant_SubMenuEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(pic);
        dest.writeString(restaurant_theme_image);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(contactId);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(main_contact);
        dest.writeString(working_hour);
        dest.writeTypedList(menu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RestaurantEntity> CREATOR = new Creator<RestaurantEntity>() {
        @Override
        public RestaurantEntity createFromParcel(Parcel in) {
            return new RestaurantEntity(in);
        }

        @Override
        public RestaurantEntity[] newArray(int size) {
            return new RestaurantEntity[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRestaurant_theme_image() {
        return restaurant_theme_image;
    }

    public void setRestaurant_theme_image(String restaurant_theme_image) {
        this.restaurant_theme_image = restaurant_theme_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public List<Restaurant_SubMenuEntity> getMenu() {
        return menu;
    }

    public void setMenu(List<Restaurant_SubMenuEntity> menu) {
        this.menu = menu;
    }

    public static List<RestaurantEntity> fakeList(int i) {

        List<RestaurantEntity> res = new ArrayList<>();

        for (int i1 = 0; i1 < i; i1++) {
            res.add(new RestaurantEntity());
        }
        return res;
    }

    public static RestaurantEntity fromLightRestaurant(LightRestaurant tmp_restaurant) {

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.id = tmp_restaurant.id;
        restaurantEntity.pic = tmp_restaurant.pic;
        restaurantEntity.name = tmp_restaurant.name;
        return restaurantEntity;
    }
}
