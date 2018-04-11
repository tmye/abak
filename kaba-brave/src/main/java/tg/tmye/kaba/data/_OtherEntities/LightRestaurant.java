package tg.tmye.kaba.data._OtherEntities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 27/03/2018.
 * email: 2597434002@qq.com
 */

public class LightRestaurant implements Parcelable {

/*   "id": 1,
                "restaurant_name": "Kastas",
                "restaurant_logo": "restaurant_pic/restaurant_11522062266.jpg"*/

    public int id;
    public String name;
    public String pic;

    protected LightRestaurant(Parcel in) {
        id = in.readInt();
        name = in.readString();
        pic = in.readString();
    }

    public static final Creator<LightRestaurant> CREATOR = new Creator<LightRestaurant>() {
        @Override
        public LightRestaurant createFromParcel(Parcel in) {
            return new LightRestaurant(in);
        }

        @Override
        public LightRestaurant[] newArray(int size) {
            return new LightRestaurant[size];
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
        parcel.writeString(pic);
    }
}
