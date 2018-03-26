package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import tg.tmye.kaba.data.advert.Group10AdvertItem;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class OffRecyclerview extends RecyclerView {
    public OffRecyclerview(Context context) {
        super(context);
    }

    public OffRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OffRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return false;
    }

}
