package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import tg.tmye.kaba.data.advert.Group10AdvertItem;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class Group10AdsView extends RelativeLayout {
    public Group10AdsView(Context context) {
        super(context);
    }

    public Group10AdsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Group10AdsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    public void inflateAds(Group10AdvertItem group10AdvertItems) {
        /* inflate the recyclerview */

    }
}
