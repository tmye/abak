package tg.experta.kaba._commons.cviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.adapters.Ad_4_8_Adapter;
import tg.experta.kaba._commons.decorator.SpacesItem_4_8_Decoration;
import tg.experta.kaba.activities.Home.Fragmentz.Home_1_Fragment;
import tg.experta.kaba.data.advert.ProductAdvertItem;

/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class Ad_4_8_View extends RecyclerView {

    private static final int AD_SPACE_4_COUNT = 4;

    public Ad_4_8_View(Context context) {
        super(context);
    }

    public Ad_4_8_View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Ad_4_8_View(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void inflateAds (List<ProductAdvertItem> ads) {

        GridLayoutManager grdLm = new GridLayoutManager(getContext(), AD_SPACE_4_COUNT);
//        recyclerview_home_4_adspace

        Ad_4_8_Adapter adp = new Ad_4_8_Adapter(getContext(), ProductAdvertItem.fakeList(8));
        setLayoutManager(grdLm);
        setAdapter(adp);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.ad_4_spacing);
        addItemDecoration(new SpacesItem_4_8_Decoration(spacingInPixels));
    }

}
