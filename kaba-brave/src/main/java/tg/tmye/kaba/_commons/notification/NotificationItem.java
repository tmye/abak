package tg.tmye.kaba._commons.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 24/04/2018.
 * email: 2597434002@qq.com
 */

public class NotificationItem implements Parcelable {

    /* if you have a to open an activity, you need the meta - food_item / link /  - */

    /* jsonObject - data */

    public String title;
    public String body;
    public String image_link;
    public NotificationFDestination destination;
    public int priority = 0;


    /* according to the type
    *
    * 1- food_item {"food_object, drinks list, restaurant entity "}
    * 2- restaurant_menu_item {"restaurant whole object "}
    * 3- article {"link"} .. got special articles ... not loaded from internet
    * 4- activity {"Activity name"}
    * 5- command - state {"command_activity", "command_id"}
    * */


    /* destination activity */
    public static class NotificationFDestination implements Parcelable{


        /* get food / menu details */
        public static final int FOOD_DETAILS = 373;
        public static final int RESTAURANT_PAGE = 758;
        public static final int RESTAURANT_MENU = 434;
        public static final int MONEY_MOVMENT = 888;

        /* get comand details */
        public static final int COMMAND_PAGE = 90;
        public static final int COMMAND_DETAILS = 154;

        public static final int COMMAND_PREPARING = 300;
        public static final int COMMAND_SHIPPING = 301;
        public static final int COMMAND_END_SHIPPING = 302;
        public static final int COMMAND_CANCELLED = 303;
        public static final int COMMAND_REJECTED = 304;

        /* get article details */
        public static final int ARTICLE_DETAILS = 128;


        /* help to know which activity we're going in */
        public int type;

        /* meta data attached with it */
        public int product_id; /* cn be product / restaurant */

        protected NotificationFDestination(Parcel in) {
            type = in.readInt();
            product_id = in.readInt();
        }

        /* can be article */


        /* for all type of activity, i need a dfifrent set data */
        /*
        * 1- food_details / restaurant_page / restaurant_menu
        * - food id :  * restaurant entity
        *                   * food_entity
        *                   * restaurant_drinks_list
        *
        * - menu entity: * restaurant entity
        *                * menu_id
        *
        * 2- command_page / command_details
        * - command (page): - command _ id
        *
        * 3- article_details
        * - article id
        *
        * */

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeInt(product_id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<NotificationFDestination> CREATOR = new Creator<NotificationFDestination>() {
            @Override
            public NotificationFDestination createFromParcel(Parcel in) {
                return new NotificationFDestination(in);
            }

            @Override
            public NotificationFDestination[] newArray(int size) {
                return new NotificationFDestination[size];
            }
        };
    }

    protected NotificationItem(Parcel in) {
        title = in.readString();
        body = in.readString();
        image_link = in.readString();
        destination = in.readParcelable(NotificationFDestination.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(image_link);
        dest.writeParcelable(destination, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationItem> CREATOR = new Creator<NotificationItem>() {
        @Override
        public NotificationItem createFromParcel(Parcel in) {
            return new NotificationItem(in);
        }

        @Override
        public NotificationItem[] newArray(int size) {
            return new NotificationItem[size];
        }
    };
}
