package tg.tmye.kaba.data._OtherEntities;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class Contact implements Parcelable{

    @Id
    public Long id = Long.valueOf(0);


    public Long restaurant_id = Long.valueOf(0);


    public String phone;


    public String address;


    protected Contact(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            restaurant_id = null;
        } else {
            restaurant_id = in.readLong();
        }
        phone = in.readString();
        address = in.readString();
    }

    @Generated(hash = 11908214)
    public Contact(Long id, Long restaurant_id, String phone, String address) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.phone = phone;
        this.address = address;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Long get_id() {
        return this.id;
    }

    public void set_id(Long id) {
        this.id = id;
    }

    public Long getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(Long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        if (restaurant_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(restaurant_id);
        }
        parcel.writeString(phone);
        parcel.writeString(address);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
