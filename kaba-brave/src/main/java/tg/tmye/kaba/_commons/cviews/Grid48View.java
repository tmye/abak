package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.Grid48ViewAdapter;
import tg.tmye.kaba._commons.decorator.Grid48Decoration;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.data.advert.AdsBanner;


/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class Grid48View extends OffRecyclerview {

    private static final int AD_SPACE_4_COUNT = 4;
    private static final int AD_SPACE_2_COUNT = 2;
    private Grid48Decoration myItemDecoration;

    public Grid48View(Context context) {
        super(context);
    }

    public Grid48View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Grid48View(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void inflateAds(final List<AdsBanner> ads, final F_Home_1_Fragment.OnFragmentInteractionListener listener) {

        ((AppCompatActivity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /* get size of the view */
                GridLayoutManager grdLm = new GridLayoutManager(getContext(), AD_SPACE_2_COUNT);
                Grid48ViewAdapter adp = new Grid48ViewAdapter(getContext(), ads, listener);
                setLayoutManager(grdLm);
                setAdapter(adp);
                int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.ad_4_spacing);

                if (myItemDecoration == null) {
                    myItemDecoration = new Grid48Decoration(spacingInPixels);

//           bcs Cannot add item decoration during a scroll  or layout
                    getItemAnimator().isRunning(new ItemAnimator.ItemAnimatorFinishedListener() {
                        @Override
                        public void onAnimationsFinished() {
                            addItemDecoration(myItemDecoration);
                        }
                    });
                }

            }
        });

    }
}