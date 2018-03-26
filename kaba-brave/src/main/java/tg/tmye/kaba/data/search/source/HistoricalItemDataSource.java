package tg.tmye.kaba.data.search.source;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.search.HistoricalItem;

/**
 * Created by abiguime on 2016/12/7.
 */

public class HistoricalItemDataSource /*implements HistoricalItemData*/ {



    public HistoricalItemDataSource(Context ctx) {
    }

    public List<HistoricalItem> getLastItems() {

        int count = 20;
        List<HistoricalItem> smp = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            smp.add(new HistoricalItem(i, "Historical "+i));
        }
        return smp;
    }

    public void saveItemToDb(HistoricalItem item) {

    }


}
