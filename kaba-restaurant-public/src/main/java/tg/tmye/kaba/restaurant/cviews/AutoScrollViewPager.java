package tg.tmye.kaba.restaurant.cviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant._commons.utils.UtilFunctions;

/**
 * By abiguime on 05/04/2018.
 * email: 2597434002@qq.com
 */

public class AutoScrollViewPager extends ViewPager {

    private boolean autoCompute = true;
    public boolean canScroll = false;

    private boolean stopScrollWhenTouch = true;
    private boolean isAutoScroll = true;
    private boolean isStopByTouch = true;

    public AutoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customSlider);
            autoCompute = a.getBoolean(R.styleable.customSlider_autocompute, true);
            a.recycle();
        }
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
            super.setCurrentItem(item, smoothScroll); // gros bug
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /* set up the size of the view here */
        if (autoCompute) {
            int width = UtilFunctions.getScreenSize(getContext())[0];
            int height = 9 * width / 16;
            setMeasuredDimension(width, height);
        }
    }

}
