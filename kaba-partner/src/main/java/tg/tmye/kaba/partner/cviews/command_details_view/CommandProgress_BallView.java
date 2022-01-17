package tg.tmye.kaba.partner.cviews.command_details_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import tg.tmye.kaba.partner.R;


/**
 * By abiguime on 14/05/2018.
 * email: 2597434002@qq.com
 */

public class CommandProgress_BallView extends View {

    private int width;
    private int height;


    int standardColor = Color.parseColor("#33666666");

    private int color = 0;
    private boolean enabled = false;
    private boolean hasFocus = false;


    public CommandProgress_BallView(Context context) {
        super(context);
        initParams(null);
        initSize();
        initDrawing();
    }

    public CommandProgress_BallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams(attrs);
        initSize();
        initDrawing();
    }

    public CommandProgress_BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(attrs);
        initSize();
        initDrawing();
    }

    private void initParams(AttributeSet attrs) {
        if (attrs == null)
            return;

        int[] colorAttr = new int[] { R.attr.command_style_color };
        TypedArray a = getContext().obtainStyledAttributes(attrs, colorAttr);

        color = a.getColor(0, -1);
        a.recycle();
    }

    private void initSize() {

        width = getResources().getDimensionPixelSize(hasFocus ?  R.dimen.command_progress_ball_size_has_focus : R.dimen.command_progress_ball_size);
        height = getResources().getDimensionPixelSize(hasFocus ?  R.dimen.command_progress_ball_size_has_focus : R.dimen.command_progress_ball_size);

//        cx = width/2;
//        cy = height/2;
//        radius = width/2;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void initDrawing() {

        cx = width/2;
        cy = height/2;

        radius = width/2;

        standard_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        enabled_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        standard_paint.setColor(standardColor);
        enabled_paint.setColor(color);
    }

    int cx,cy, radius;
    Paint standard_paint;
    Paint enabled_paint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(cx, cy, radius, enabled ? enabled_paint : standard_paint);
    }

    public void iEnable(boolean isEnabled, boolean hasFocus) {

        this.enabled = isEnabled;
        this.hasFocus = hasFocus;

//        initSize();
        invalidate();
    }
}