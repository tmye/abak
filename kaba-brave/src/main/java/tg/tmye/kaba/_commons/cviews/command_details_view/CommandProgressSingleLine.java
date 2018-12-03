package tg.tmye.kaba._commons.cviews.command_details_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import tg.tmye.kaba.R;
import tg.tmye.kaba.config.Constant;

/**
 * By abiguime on 14/05/2018.
 * email: 2597434002@qq.com
 */

public class CommandProgressSingleLine extends LinearLayout {


    private int width;
    private int height;


    public CommandProgressSingleLine(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        initWidth();
    }

    public CommandProgressSingleLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        initWidth();
    }

    public CommandProgressSingleLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        initWidth();
    }




    private void initWidth() {

        width = getResources().getDisplayMetrics().widthPixels;
        width = width/2 + (getResources().getDimensionPixelSize(R.dimen.command_progress_ball_size)/2);
        height =(getResources().getDimensionPixelSize(R.dimen.command_progress_ball_size));

        Log.d(Constant.APP_TAG, "screenwidth "+getResources().getDisplayMetrics().widthPixels);
        Log.d(Constant.APP_TAG, "timeWidth "+ width);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
