package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * By abiguime on 05/04/2018.
 * email: 2597434002@qq.com
 */

public class AutoScrollViewPager extends ViewPager {


    public boolean canScroll = false;

    private boolean stopScrollWhenTouch = true;
    private boolean isAutoScroll = true;
    private boolean isStopByTouch = true;

    public AutoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        Log.d("scroll", "dispatchTouchEvent");
        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                isStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                startAutoScroll();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void startAutoScroll() {
        canScroll = true;
        Log.d("scroll", "can scroll true");
    }

    private void stopAutoScroll() {
        canScroll = false;
        Log.d("scroll", "can scroll false");
    }

    @Override
    public void setCurrentItem(int item) {
        if (!canScroll)
            super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!canScroll)
            super.setCurrentItem(item, smoothScroll);
    }
}
