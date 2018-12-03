package tg.tmye.kaba.data.advert;

import android.os.Parcel;
import android.os.Parcelable;

import tg.tmye.kaba.data._OtherEntities.SimplePicture;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class AdsBanner implements Parcelable {


    /* tips */
    public static final int TYPE_REPAS = 1;
    public static final int TYPE_MENU = 2;
    public static final int TYPE_ARTICLE = 3;
    public static final int TYPE_RESTAURANT = 4;
    public static final int BEST_SELLER = 90;
    public static final int EVENEMENT = 91;

    public int id;
    public String name;
    public String link;
    public String description;
    public String pic;
    public String food_json = "";
    public int type;
    public int entity_id;

    public AdsBanner() {

    }

    //    public String image;
    //    public String placeholder;


    protected AdsBanner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        link = in.readString();
        description = in.readString();
        pic = in.readString();
        food_json = in.readString();
        type = in.readInt();
        entity_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeString(pic);
        dest.writeString(food_json);
        dest.writeInt(type);
        dest.writeInt(entity_id);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
