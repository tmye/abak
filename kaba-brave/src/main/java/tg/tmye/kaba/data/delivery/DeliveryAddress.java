package tg.tmye.kaba.data.delivery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 23/01/2018.
 * email: 2597434002@qq.com
 */

public class DeliveryAddress implements Parcelable {

    public int id = 0;
    public String name;
    public String location;
    public String phone_number;
    public String user_id;
    public String description;
    public String[] picture;
    public String quartier;
    public String near;
    public String updated_at; /* last update date - time stamp */

    public DeliveryAddress () {
        super();
    }

    protected DeliveryAddress(Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
        phone_number = in.readString();
        user_id = in.readString();
        description = in.readString();
        picture = in.createStringArray();
        quartier = in.readString();
        near = in.readString();
        updated_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(phone_number);
        dest.writeString(user_id);
        dest.writeString(description);
        dest.writeStringArray(picture);
        dest.writeString(quartier);
        dest.writeString(near);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
        @Override
        public DeliveryAddress createFromParcel(Parcel in) {
            return new DeliveryAddress(in);
        }

        @Override
        public DeliveryAddress[] newArray(int size) {
            return new DeliveryAddress[size];
        }
    };
}
