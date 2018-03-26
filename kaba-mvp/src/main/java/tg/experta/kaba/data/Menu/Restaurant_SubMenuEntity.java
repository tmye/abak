package tg.experta.kaba.data.Menu;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntityDao;
import tg.experta.kaba.data._OtherEntities.DaoSession;

/**
 * By abiguime on 2017/12/3.
 * email: 2597434002@qq.com
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class Restaurant_SubMenuEntity implements Parcelable {

    /*
    *
    *  "_id": 1,
        "title": "RICE"
    * */

    @Id
    public long id;

    public String title;

    public Long restaurant_id;


    @ToMany(referencedJoinProperty = "sub_menu_id")
    @OrderBy("id ASC")
    public List<Restaurant_Menu_FoodEntity> foods;


    private Restaurant_SubMenuEntity() {
        foods = new ArrayList<>();
    }

    protected Restaurant_SubMenuEntity(Parcel in) {
        id = in.readLong();
        title = in.readString();
        if (in.readByte() == 0) {
            restaurant_id = null;
        } else {
            restaurant_id = in.readLong();
        }
        foods = in.createTypedArrayList(Restaurant_Menu_FoodEntity.CREATOR);
    }

    @Generated(hash = 23311271)
    public Restaurant_SubMenuEntity(long id, String title, Long restaurant_id) {
        this.id = id;
        this.title = title;
        this.restaurant_id = restaurant_id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
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

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 26390584)
    private transient Restaurant_SubMenuEntityDao myDao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1909235786)
    public List<Restaurant_Menu_FoodEntity> getFoods() {
        if (foods == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            Restaurant_Menu_FoodEntityDao targetDao = daoSession.getRestaurant_Menu_FoodEntityDao();
            List<Restaurant_Menu_FoodEntity> foodsNew = targetDao._queryRestaurant_SubMenuEntity_Foods(id);
            synchronized (this) {
                if (foods == null) {
                    foods = foodsNew;
                }
            }
        }
        return foods;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1950966)
    public synchronized void resetFoods() {
        foods = null;
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
    @Generated(hash = 1074572441)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRestaurant_SubMenuEntityDao() : null;
    }
}
