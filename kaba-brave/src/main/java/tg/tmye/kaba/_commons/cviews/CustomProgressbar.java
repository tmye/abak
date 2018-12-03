package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;

import tg.tmye.kaba.R;

/**
 * By abiguime on 23/04/2018.
 * email: 2597434002@qq.com
 */

public class CustomProgressbar extends ProgressBar /*android.support.v7.widget.AppCompatImageView*/ {


    private AnimationDrawable foodLoadingAnimation;

    public CustomProgressbar(Context context) {
        super(context);
        setdrawable();
    }

    public CustomProgressbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setdrawable();
    }

    public CustomProgressbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setdrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* bottom stuff, need to draw the items on the bottom. */
    }

    private void setdrawable() {


        /*setBackgroundResource(R.drawable.custom_loading_drawable);
        foodLoadingAnimation = (AnimationDrawable) getBackground();
        foodLoadingAnimation.start();*/
    }

    public void stopAnim() {
        if (foodLoadingAnimation != null)
            foodLoadingAnimation.stop();
    }

    public void startAnim() {
        if (foodLoadingAnimation != null)
            foodLoadingAnimation.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
       /* if (visibility == VISIBLE) {
            startAnim();
        } else {
            stopAnim();
        }*/
    }
}
