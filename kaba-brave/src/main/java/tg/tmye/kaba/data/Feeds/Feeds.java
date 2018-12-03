package tg.tmye.kaba.data.Feeds;

import android.os.Parcel;
import android.os.Parcelable;

import tg.tmye.kaba._commons.notification.NotificationItem;

/**
 * By abiguime on 02/02/2018.
 * email: 2597434002@qq.com
 */

public class Feeds implements Parcelable{

               /*     {
                "id": 101,
                "title": "foo",
                "content": "bar",
                "destination": {
                    "type": 758,
                    "product_id": 1
                },
                "created_at": "2018-06-26 14:17:49",
                "pic": "food_notification/5b322eed60d77023242763.jpg"
            }*/

    /* feed is an entity */
    public int id;
    public String title;
    public String pic;
    public String content;
    public String created_at;
    public NotificationItem.NotificationFDestination destination;

    protected Feeds(Parcel in) {
        id = in.readInt();
        title = in.readString();
        pic = in.readString();
        content = in.readString();
        created_at = in.readString();
        destination = in.readParcelable(NotificationItem.NotificationFDestination.class.getClassLoader());
    }

    public static final Creator<Feeds> CREATOR = new Creator<Feeds>() {
        @Override
        public Feeds createFromParcel(Parcel in) {
            return new Feeds(in);
        }

        @Override
        public Feeds[] newArray(int size) {
            return new Feeds[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(pic);
        parcel.writeString(content);
        parcel.writeString(created_at);
        parcel.writeParcelable(destination, i);
    }
}
