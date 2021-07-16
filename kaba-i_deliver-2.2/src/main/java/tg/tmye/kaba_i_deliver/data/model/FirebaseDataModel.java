package tg.tmye.kaba_i_deliver.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 2021/7/15.
 * email: 2597434002@qq.com
 */
public class FirebaseDataModel implements Parcelable {

    public String userToken;
    public String username;
    public float latitude;
    public float longitude;
    public String readGo; // on or off
    public long lastUpdate;
    public String dateTimeExplicit;


    public FirebaseDataModel(String userToken, String username, float latitude, float longitude, String readGo, long lastUpdate, String dateTimeExplicit) {
        this.userToken = userToken;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.readGo = readGo;
        this.lastUpdate = lastUpdate;
        this.dateTimeExplicit = dateTimeExplicit;
    }

    protected FirebaseDataModel(Parcel in) {
        userToken = in.readString();
        username = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        readGo = in.readString();
        lastUpdate = in.readLong();
        dateTimeExplicit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userToken);
        dest.writeString(username);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(readGo);
        dest.writeLong(lastUpdate);
        dest.writeString(dateTimeExplicit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FirebaseDataModel> CREATOR = new Creator<FirebaseDataModel>() {
        @Override
        public FirebaseDataModel createFromParcel(Parcel in) {
            return new FirebaseDataModel(in);
        }

        @Override
        public FirebaseDataModel[] newArray(int size) {
            return new FirebaseDataModel[size];
        }
    };

    @Override
    public String toString() {
        return "FirebaseDataModel{" +
                "userToken='" + userToken.substring(0,7) + '\'' +
                ", username='" + username + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", readGo='" + readGo + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", dateTimeExplicit='" + dateTimeExplicit + '\'' +
                '}';
    }
}
