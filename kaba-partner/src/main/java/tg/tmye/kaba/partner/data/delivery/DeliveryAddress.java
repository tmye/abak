package tg.tmye.kaba.partner.data.delivery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 23/01/2018.
 * email: 2597434002@qq.com
 */

public class DeliveryAddress implements Parcelable {

    public int id = 0;
    public String name = "";
    public String location;
    public String phone_number = "";
    public String user_id;
    public String description = "";
    public String[] picture;
    public String quartier = "";
    public String near;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeString(phone_number);
        parcel.writeString(user_id);
        parcel.writeString(description);
        parcel.writeStringArray(picture);
        parcel.writeString(quartier);
        parcel.writeString(near);
    }
}
