package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

import tg.tmye.kaba._commons.utils.UtilFunctions;

/**
 * By abiguime on 17/06/2018.
 * email: 2597434002@qq.com
 */
public class CustomAppBarLayout extends AppBarLayout {
    public CustomAppBarLayout(Context context) {
        super(context);
    }

    public CustomAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /* set up the size of the view here */
        int width = UtilFunctions.getScreenSize(getContext())[0];
        int height = 9 * width / 16;
        setMeasuredDimension(width, height);
    }
}
