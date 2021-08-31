package tg.tmye.kaba_i_deliver.data.stats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 2021/7/26.
 * email: 2597434002@qq.com
 */
public class StatisticResults implements Parcelable {

    /*

     "error": 0,
    "message": "",
    "data": {
        "history": [
            {
                "date": "2021-05-10",

                "ca_hsn": 0, // chiffre d'affaire hsn
                "nb_hsn": 0, // nombre de hsn
                "nb_app_command": 0, // nombre applications in-system
                "ca_app": 0, // ca application


                "nb_command": 0, // nombre de commandes livrees
                "extra_shipping_pricing": 0, // frais de service
                "ca": 0 // ca total
            }
        ],
        "start": "2021-05-10",
        "end": "2021-06-16"
    }
}
    */

    public String date;

    public String ca_hsn;
    public String nb_hsn;

    public String nb_app_command;
    public String ca_app;

    public String nb_command;
    public String ca;

    public String ca_app_pp;
    public String nb_app_command_pp;

    public String ca_app_pa;
    public String nb_app_command_pa;

    public String nb_app_command_preorder;
    public String ca_app_command_preorder;

    public String extra_shipping_pricing;

    public StatisticResults(String date, String ca_hsn, String nb_hsn, String nb_app_command, String ca_app, String nb_command, String ca, String ca_app_pp, String nb_app_command_pp, String ca_app_pa, String nb_app_command_pa, String nb_app_command_preorder, String ca_app_command_preorder, String extra_shipping_pricing) {
        this.date = date;
        this.ca_hsn = ca_hsn;
        this.nb_hsn = nb_hsn;
        this.nb_app_command = nb_app_command;
        this.ca_app = ca_app;
        this.nb_command = nb_command;
        this.ca = ca;
        this.ca_app_pp = ca_app_pp;
        this.nb_app_command_pp = nb_app_command_pp;
        this.ca_app_pa = ca_app_pa;
        this.nb_app_command_pa = nb_app_command_pa;
        this.nb_app_command_preorder = nb_app_command_preorder;
        this.ca_app_command_preorder = ca_app_command_preorder;
        this.extra_shipping_pricing = extra_shipping_pricing;
    }

    protected StatisticResults(Parcel in) {
        date = in.readString();
        ca_hsn = in.readString();
        nb_hsn = in.readString();
        nb_app_command = in.readString();
        ca_app = in.readString();
        nb_command = in.readString();
        ca = in.readString();
        ca_app_pp = in.readString();
        nb_app_command_pp = in.readString();
        ca_app_pa = in.readString();
        nb_app_command_pa = in.readString();
        nb_app_command_preorder = in.readString();
        ca_app_command_preorder = in.readString();
        extra_shipping_pricing = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(ca_hsn);
        dest.writeString(nb_hsn);
        dest.writeString(nb_app_command);
        dest.writeString(ca_app);
        dest.writeString(nb_command);
        dest.writeString(ca);
        dest.writeString(ca_app_pp);
        dest.writeString(nb_app_command_pp);
        dest.writeString(ca_app_pa);
        dest.writeString(nb_app_command_pa);
        dest.writeString(nb_app_command_preorder);
        dest.writeString(ca_app_command_preorder);
        dest.writeString(extra_shipping_pricing);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StatisticResults> CREATOR = new Creator<StatisticResults>() {
        @Override
        public StatisticResults createFromParcel(Parcel in) {
            return new StatisticResults(in);
        }

        @Override
        public StatisticResults[] newArray(int size) {
            return new StatisticResults[size];
        }
    };
}
