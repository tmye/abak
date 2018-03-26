package tg.tmye.kaba.data.Restaurant;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data._OtherEntities.Contact;
import tg.tmye.kaba.data._OtherEntities.DaoSession;
import tg.tmye.kaba.data._OtherEntities.ContactDao;

/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class RestaurantEntity implements Parcelable {

    /*
    * {
        "_id": 1,
        "restaurant_unique_id": "mami2017",
        "restaurant_logo": "",
        "restaurant_theme_image": "",
        "restaurant_name": "Mami",
        "restaurant_description": "Le Paradis du Watchi",
        "restaurant_contact_id": 1,
      }
    *
    * */

    @Id
    public long id;

    @NotNull
    public String restaurant_logo;

    @NotNull
    public String restaurant_theme_image;

    @NotNull
    public String restaurant_name;

    @NotNull
    public String restaurant_description;

    private long contactId;

    public long restaurant_contact_id;

    @ToOne(joinProperty = "restaurant_contact_id")
    private Contact contact;

    @Transient
    public List<Restaurant_SubMenuEntity> menu;

    protected RestaurantEntity(Parcel in) {
        id = in.readLong();
        restaurant_logo = in.readString();
        restaurant_theme_image = in.readString();
        restaurant_name = in.readString();
        restaurant_description = in.readString();
        contactId = in.readLong();
        restaurant_contact_id = in.readLong();
        contact = in.readParcelable(Contact.class.getClassLoader());
        menu = in.createTypedArrayList(Restaurant_SubMenuEntity.CREATOR);
    }

    public RestaurantEntity() {
    }

    @Generated(hash = 1059767000)
    public RestaurantEntity(long id, @NotNull String restaurant_logo,
            @NotNull String restaurant_theme_image, @NotNull String restaurant_name,
            @NotNull String restaurant_description, long contactId,
            long restaurant_contact_id) {
        this.id = id;
        this.restaurant_logo = restaurant_logo;
        this.restaurant_theme_image = restaurant_theme_image;
        this.restaurant_name = restaurant_name;
        this.restaurant_description = restaurant_description;
        this.contactId = contactId;
        this.restaurant_contact_id = restaurant_contact_id;
    }

     
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(restaurant_logo);
        dest.writeString(restaurant_theme_image);
        dest.writeString(restaurant_name);
        dest.writeString(restaurant_description);
        dest.writeLong(contactId);
        dest.writeLong(restaurant_contact_id);
        dest.writeParcelable(contact, flags);
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

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 613689067)
    private transient RestaurantEntityDao myDao;

    @Generated(hash = 321829790)
    private transient Long contact__resolvedKey;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRestaurant_logo() {
        return restaurant_logo;
    }

    public void setRestaurant_logo(String restaurant_logo) {
        this.restaurant_logo = restaurant_logo;
    }

    public String getRestaurant_theme_image() {
        return restaurant_theme_image;
    }

    public void setRestaurant_theme_image(String restaurant_theme_image) {
        this.restaurant_theme_image = restaurant_theme_image;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_description() {
        return restaurant_description;
    }

    public void setRestaurant_description(String restaurant_description) {
        this.restaurant_description = restaurant_description;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getRestaurant_contact_id() {
        return restaurant_contact_id;
    }

    public void setRestaurant_contact_id(long restaurant_contact_id) {
        this.restaurant_contact_id = restaurant_contact_id;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 523241853)
    public Contact getContact() {
        long __key = this.restaurant_contact_id;
        if (contact__resolvedKey == null || !contact__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactDao targetDao = daoSession.getContactDao();
            Contact contactNew = targetDao.load(__key);
            synchronized (this) {
                contact = contactNew;
                contact__resolvedKey = __key;
            }
        }
        return contact;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 702311960)
    public void setContact(@NotNull Contact contact) {
        if (contact == null) {
            throw new DaoException(
                    "To-one property 'restaurant_contact_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.contact = contact;
            restaurant_contact_id = contact.getId();
            contact__resolvedKey = restaurant_contact_id;
        }
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
    @Generated(hash = 1278456171)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRestaurantEntityDao() : null;
    }

}
