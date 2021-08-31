package tg.tmye.kaba.restaurant.cviews.command_details_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import tg.tmye.kaba.restaurant.ILog;
import tg.tmye.kaba.restaurant.syscore.Constant;
import tg.tmye.kaba.restaurant.R;


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

        ILog.print("screenwidth "+getResources().getDisplayMetrics().widthPixels);
        ILog.print("timeWidth "+ width);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
