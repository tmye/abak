package tg.tmye.kaba_i_deliver.data.delivery;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * By abiguime on 2020/4/15.
 * email: 2597434002@qq.com
 */

public class DeliveryTimeFrame implements Parcelable {

    public int id; // id of it
    public String start; // what time start delivery
    public String end; // what time ending delivery

    protected DeliveryTimeFrame(Parcel in) {
        id = in.readInt();
        start = in.readString();
        end = in.readString();
    }

    public DeliveryTimeFrame() {

    }

    public static List<DeliveryTimeFrame> random(int count) {

        List<DeliveryTimeFrame>  res = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            res.add(DeliveryTimeFrame.randomSingle());
        }
        return res;
    }

    private static DeliveryTimeFrame randomSingle() {

        DeliveryTimeFrame rd = new DeliveryTimeFrame();

        rd.start = "1586170800";
        rd.end = "1586178000";

        return rd;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(start);
        dest.writeString(end);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryTimeFrame> CREATOR = new Creator<DeliveryTimeFrame>() {
        @Override
        public DeliveryTimeFrame createFromParcel(Parcel in) {
            return new DeliveryTimeFrame(in);
        }

        @Override
        public DeliveryTimeFrame[] newArray(int size) {
            return new DeliveryTimeFrame[size];
        }
    };
}
