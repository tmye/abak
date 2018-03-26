package tg.tmye.kaba.data.Food;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import tg.tmye.kaba._commons.greendao.converter.StringConverter;
import tg.tmye.kaba.data._OtherEntities.DaoSession;

/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class Restaurant_Menu_FoodEntity implements Parcelable {


    /* {
        "_id": "1",
        "title": "ayimolou",
        "price": "300",
        "food_pic": "/sample/food_icon/burger.jpg",
        "details": "",
        "sub_menu_id" : 1,
        "restaurant_id": 1,
        "food_description": "Riz haricot",
        "food_details_pictures": [
          "/sample/food_description/burger_0.jpg",
          "/sample/food_description/burger_1.jpg",
          "/sample/food_description/burger_2.jpg"
        ],
        "stars": "3.5"
      },*/

    @Id
    public long id;

    public String title;

    public String price;

    public String food_pic;

    public String details;

    public long sub_menu_id;

    public long restaurant_id;

    public String food_description;

    @Convert(columnType = String.class, converter = StringConverter.class)
    public List<String> food_details_pictures;

    @ToMany(referencedJoinProperty = "food_id")
    @OrderBy("id ASC")
    public List<Food_Tag> food_tags;

    public double stars;

    public Restaurant_Menu_FoodEntity () {

    }

    protected Restaurant_Menu_FoodEntity(Parcel in) {
        id = in.readLong();
        title = in.readString();
        price = in.readString();
        food_pic = in.readString();
        details = in.readString();
        sub_menu_id = in.readLong();
        restaurant_id = in.readLong();
        food_description = in.readString();
        food_details_pictures = in.createStringArrayList();
        stars = in.readDouble();
    }

    @Generated(hash = 676283681)
    public Restaurant_Menu_FoodEntity(long id, String title, String price, String food_pic, String details,
            long sub_menu_id, long restaurant_id, String food_description, List<String> food_details_pictures,
            double stars) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.food_pic = food_pic;
        this.details = details;
        this.sub_menu_id = sub_menu_id;
        this.restaurant_id = restaurant_id;
        this.food_description = food_description;
        this.food_details_pictures = food_details_pictures;
        this.stars = stars;
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

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 997027882)
    private transient Restaurant_Menu_FoodEntityDao myDao;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(food_pic);
        dest.writeString(details);
        dest.writeLong(sub_menu_id);
        dest.writeLong(restaurant_id);
        dest.writeString(food_description);
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFood_pic() {
        return this.food_pic;
    }

    public void setFood_pic(String food_pic) {
        this.food_pic = food_pic;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getSub_menu_id() {
        return this.sub_menu_id;
    }

    public void setSub_menu_id(long sub_menu_id) {
        this.sub_menu_id = sub_menu_id;
    }

    public long getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getFood_description() {
        return this.food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 928031622)
    public List<Food_Tag> getFood_tags() {
        if (food_tags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            Food_TagDao targetDao = daoSession.getFood_TagDao();
            List<Food_Tag> food_tagsNew = targetDao._queryRestaurant_Menu_FoodEntity_Food_tags(id);
            synchronized (this) {
                if (food_tags == null) {
                    food_tags = food_tagsNew;
                }
            }
        }
        return food_tags;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1544578361)
    public synchronized void resetFood_tags() {
        food_tags = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1654198421)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRestaurant_Menu_FoodEntityDao() : null;
    }


}
