package tg.tmye.kaba_i_deliver.data.dailyreport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 2021/12/2.
 * email: 2597434002@qq.com
 */
public class DailyReport implements Parcelable {

//    {
//        "id": 2,
//            "beginAmount": 5000,
//            "endAmount": -46300,
//            "prePaySys": 63900,
//            "prePayHsys": 0,
//            "nbHsn": 0,
//            "dayTurnover": 16400,
//            "communicationCredit": 500,
//            "fuelAmount": 2000,
//            "reparationAmount": 0,
//            "complementAmount": 0,
//            "refundOrderAmount": 0,
//            "lossAmount": 1000,
//            "parkingAmount": 200,
//            "various": 100,
//            "lossOnOrder": 0,
//            "isVerify": false,
//            "remuneration": 6040,
//            "isPaid": false,
//            "createdAt": [],
//        "livreur": {
//        "id": 33,
//                "name": "LIVREUR KABA",
//                "workcontact": "92109474",
//                "isActive": true,
//                "username": "kaba1",
//                "email": "livreurkaba@kaba-delivery.com",
//                "enabled": 1,
//                "deviceId": 27644,
//                "isAvailable": true
//    },
//        "type": 1,
//            "cnssAmount": null,
//            "engineOilPrime": null
//    }

    public int id;
    public int fuel_amount;
    public int communication_credit;
    public int reparation_amount;
    public int balance_loss;
    public int parking_amount;
    public int various;
    public String date;
    public boolean isVerify;

    public DailyReport() {

    }


    protected DailyReport(Parcel in) {
        id = in.readInt();
        fuel_amount = in.readInt();
        communication_credit = in.readInt();
        reparation_amount = in.readInt();
        balance_loss = in.readInt();
        parking_amount = in.readInt();
        various = in.readInt();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(fuel_amount);
        dest.writeInt(communication_credit);
        dest.writeInt(reparation_amount);
        dest.writeInt(balance_loss);
        dest.writeInt(parking_amount);
        dest.writeInt(various);
        dest.writeString(date);
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
