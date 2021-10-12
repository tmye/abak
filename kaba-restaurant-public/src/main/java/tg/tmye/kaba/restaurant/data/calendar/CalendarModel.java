package tg.tmye.kaba.restaurant.data.calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 2021/9/28.
 * email: 2597434002@qq.com
 */
public class CalendarModel implements Parcelable {

//      "day": 1,
//              "open": 0,
//              "pause": 0,
//              "start": "11:00",
//              "end": "20:45",
//              "pause_start": "",
//              "pause_end": ""
//},

    public int day;
    public String day_name;
    public int open;
    public int pause;
    public String start;
    public String end;
    public String pause_start;
    public String pause_end;

    public CalendarModel(String day_name, int open, int pause, String start, String end, String pause_start, String pause_end) {
        this.day_name = day_name;
        this.open = open;
        this.pause = pause;
        this.start = start;
        this.end = end;
        this.pause_start = pause_start;
        this.pause_end = pause_end;
    }

    protected CalendarModel(Parcel in) {
        day = in.readInt();
        open = in.readInt();
        pause = in.readInt();
        start = in.readString();
        end = in.readString();
        pause_start = in.readString();
        pause_end = in.readString();
    }

    public static final Creator<CalendarModel> CREATOR = new Creator<CalendarModel>() {
        @Override
        public CalendarModel createFromParcel(Parcel in) {
            return new CalendarModel(in);
        }

        @Override
        public CalendarModel[] newArray(int size) {
            return new CalendarModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(open);
        dest.writeInt(pause);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(pause_start);
        dest.writeString(pause_end);
    }
}
