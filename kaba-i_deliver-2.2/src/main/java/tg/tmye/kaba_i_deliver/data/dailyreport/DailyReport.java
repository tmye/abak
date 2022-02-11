package tg.tmye.kaba_i_deliver.data.dailyreport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 2021/12/2.
 * email: 2597434002@qq.com
 */
public class DailyReport implements Parcelable {

//    	"id": 11,
//                "beginAmount": 10000,
//                "endAmount": 6200,
//                "prePaySys": 0,
//                "prePayHsys": 0,
//                "nbHsn": 0,
//                "dayTurnover": 0,
//                "isVerify": false,
//                "remuneration": -3800,
//                "isPaid": false,
//                "createdAt": "2022-02-02",
//                "makeAt": "2022-02-02",
//                "type": 1,
//                "cnssAmount": null,
//                "engineOilPrime": null

    public int id = 0;
    public int dayTurnover, endAmount, beginAmount;
    public int fuelAmount;
    public int communicationCredit;
    public int reparationAmount;
    public int lossAmount;
    public int parkingAmount;
    public int various;
    public int lossOnOrder;
    public int complementAmount, refundOrderAmount;
//    public String date;"createdAt":"2022-02-02","makeAt":"2022-02-02"
    public String makeAt;
    public boolean isVerify;


    public DailyReport() {

    }

    protected DailyReport(Parcel in) {
        id = in.readInt();
        dayTurnover = in.readInt();
        endAmount = in.readInt();
        beginAmount = in.readInt();
        fuelAmount = in.readInt();
        communicationCredit = in.readInt();
        reparationAmount = in.readInt();
        lossAmount = in.readInt();
        parkingAmount = in.readInt();
        various = in.readInt();
        lossOnOrder = in.readInt();
        complementAmount = in.readInt();
        refundOrderAmount = in.readInt();
        makeAt = in.readString();
        isVerify = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(dayTurnover);
        dest.writeInt(endAmount);
        dest.writeInt(beginAmount);
        dest.writeInt(fuelAmount);
        dest.writeInt(communicationCredit);
        dest.writeInt(reparationAmount);
        dest.writeInt(lossAmount);
        dest.writeInt(parkingAmount);
        dest.writeInt(various);
        dest.writeInt(lossOnOrder);
        dest.writeInt(complementAmount);
        dest.writeInt(refundOrderAmount);
        dest.writeString(makeAt);
        dest.writeByte((byte) (isVerify ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DailyReport> CREATOR = new Creator<DailyReport>() {
        @Override
        public DailyReport createFromParcel(Parcel in) {
            return new DailyReport(in);
        }

        @Override
        public DailyReport[] newArray(int size) {
            return new DailyReport[size];
        }
    };
}
