package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import tg.tmye.kaba.R;


/**
 * By abiguime on 31/01/2018.
 * email: 2597434002@qq.com
 */

public class SlidingBannerBottomHalfCircle extends View {

    int left, right, height;

    Paint mPaint;

    private int superRight;

    public SlidingBannerBottomHalfCircle(Context context) {
        super(context);
        init();
    }

    public SlidingBannerBottomHalfCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidingBannerBottomHalfCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingBannerBottomHalfCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        // init vars
        left = 0;
        height = getContext().getResources().getDimensionPixelSize(R.dimen.home_slidingbanner_bottom_semicirlce_height);
        right = getContext().getResources().getDisplayMetrics().widthPixels;

        superRight = 4*right;

        // init paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);

        // init angles and vals
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(right/2, superRight+height/2/*+superRight*/, superRight, mPaint);
    }
}
