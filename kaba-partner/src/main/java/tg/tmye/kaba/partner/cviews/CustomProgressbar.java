package tg.tmye.kaba.partner.cviews;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import tg.tmye.kaba.partner.R;


/**
 * By abiguime on 23/04/2018.
 * email: 2597434002@qq.com
 */

public class CustomProgressbar extends AppCompatImageView {


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


    private void setdrawable() {

        setBackgroundResource(R.drawable.custom_loading_drawable);
        foodLoadingAnimation = (AnimationDrawable) getBackground();
        foodLoadingAnimation.start();
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
        if (visibility == VISIBLE) {
            startAnim();
        } else {
            stopAnim();
        }
    }
}
