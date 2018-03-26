package tg.tmye.kaba.data.search;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * By abiguime on 24/02/2018.
 * email: 2597434002@qq.com
 */

public class SearchResult implements Parcelable {


    public String title;
    public String channel;

    protected SearchResult(Parcel in) {
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
