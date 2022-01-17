package tg.tmye.kaba.partner.data.Restaurant;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * By abiguime on 2017/11/25.
 * email: 2597434002@qq.com
 */
public class RestaurantEntity implements Parcelable {

    public int id;

    public String pic;

    public String theme_pic = "";

    public String name;

    public String description = "";

    private long contactId;

    public String address = "";

    public String email;

    public String main_contact = "";

    public String working_hour = "";

    public int is_open;

    public int open_type;


    public RestaurantEntity() {
    }


    protected RestaurantEntity(Parcel in) {
        id = in.readInt();
        pic = in.readString();
        theme_pic = in.readString();
        name = in.readString();
        description = in.readString();
        contactId = in.readLong();
        address = in.readString();
        email = in.readString();
        main_contact = in.readString();
        working_hour = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(pic);
        dest.writeString(theme_pic);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(contactId);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(main_contact);
        dest.writeString(working_hour);
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

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTheme_pic() {
        return theme_pic;
    }

    public void setTheme_pic(String theme_pic) {
        this.theme_pic = theme_pic;
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




}
