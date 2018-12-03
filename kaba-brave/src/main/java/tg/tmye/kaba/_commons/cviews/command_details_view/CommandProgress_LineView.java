package tg.tmye.kaba._commons.cviews.command_details_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import tg.tmye.kaba.R;

/**
 * By abiguime on 14/05/2018.
 * email: 2597434002@qq.com
 */

public class CommandProgress_LineView extends View {

    int standardColor = Color.parseColor("#33666666");

    private int color = 0;

    private boolean enabled;

    public CommandProgress_LineView(Context context) {
        super(context);
        initParams(null);
    }

    public CommandProgress_LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams(attrs);
    }

    public CommandProgress_LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(attrs);
    }

    private void initParams(AttributeSet attrs) {

        if (attrs == null)
            return;

        int[] colorAttr = new int[] { R.attr.command_style_color };
        TypedArray a = getContext().obtainStyledAttributes(attrs, colorAttr);

        color = a.getColor(0, -1);
        a.recycle();
    }



    public void iEnable(boolean isEnabled) {

        /* according to the type, set the background color */
        enabled = isEnabled;

        if (enabled) {
            setBackgroundColor(color);
        } else
            setBackgroundColor(standardColor);
    }
}