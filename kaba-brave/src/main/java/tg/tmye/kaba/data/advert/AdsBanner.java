package tg.tmye.kaba.data.advert;

import android.os.Parcel;
import android.os.Parcelable;

import tg.tmye.kaba.data._OtherEntities.SimplePicture;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class AdsBanner implements Parcelable {

    /*"id": 3,
             "restaurant_name": "slider/cropper.jpg",
             "link": "www.google.tg",
             "placeholder": "slider 1"*/

    public int id;
    public String name;
    public String link;
    public String image;
    public String placeholder;

    public String food_json = "";

// id ... ad ? food ?

    public AdsBanner() {

    }


    protected AdsBanner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        link = in.readString();
        image = in.readString();
        placeholder = in.readString();
    }

    public static final Creator<AdsBanner> CREATOR = new Creator<AdsBanner>() {
        @Override
        public AdsBanner createFromParcel(Parcel in) {
            return new AdsBanner(in);
        }

        @Override
        public AdsBanner[] newArray(int size) {
            return new AdsBanner[size];
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
        parcel.writeString(link);
        parcel.writeString(image);
        parcel.writeString(placeholder);
    }
}
