package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import tg.tmye.kaba.config.Constant;


/**
 * By abiguime on 2017/10/29.
 * email: 2597434002@qq.com
 */

public class ScanzoneView extends View implements Runnable {

    private static final float SCANNING_EFFECT_SPEED = 10f;


    Handler mHandler = new Handler();

    Paint effectPaint, mOverlayPaint;

    Rect overLayRect, transparentRect;

    float sW, sH;

    float widthRatio = 3f/5f;


    private float smRatio_H;
    private float bigRatio_W;
    private float smRatio_W;
    private float bigRatio_H;


    float scanningHeight;
    float scanProgress = 0;
        boolean direction = true;



    private boolean running = true;

    public ScanzoneView(Context context) {
        super(context);
        init();
    }


    public ScanzoneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ScanzoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        initPaints();
        initRect();
        initValues();

        mHandler.post(this);

        setLayerType(LAYER_TYPE_SOFTWARE, mOverlayPaint);
    }

    private void initPaints() {

        mOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayPaint.setColor(Color.BLACK);
        mOverlayPaint.setAlpha(80);
        mOverlayPaint.setStyle(Paint.Style.FILL);


        effectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        effectPaint.setColor(Color.parseColor("#00FC43"));
        effectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        effectPaint.setStrokeWidth(15f);
    }

    private void initRect() {

    }


    private void initValues() {
        sW = (float) ScreenUtils.getScreenSize(getContext(), ScreenUtils.SCREEN.WIDTH);
        sH = (float) ScreenUtils.getScreenSize(getContext(), ScreenUtils.SCREEN.LONG);

        smRatio_W = (1f - widthRatio) /2f;
        bigRatio_W = (1f - smRatio_W);
        //
        smRatio_H = (((sH - widthRatio*sW))/sH)/2f;
        bigRatio_H = (1f - smRatio_H);

        // translate the drawing on the top
        smRatio_H -= ((1f*sH)/10f)/sH;
        bigRatio_H -= ((1f*sH)/10f)/sH;



        // length positions
        scanningHeight = (int) (widthRatio*sW);

        Log.d(Constant.APP_TAG, "scanningheight "+scanningHeight);
        scanProgress = scanningHeight;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        drawTransparentOverlay(canvas);
        drawRectangleWithBorder(canvas);
        drawEffect(canvas);

        invalidate();

//        drawPhotoRectangle(canvas);
    }


    private void drawEffect(Canvas canvas) {


        if (direction) { // downward
            if (scanProgress < scanningHeight)
                scanProgress+=SCANNING_EFFECT_SPEED;
            else
                direction = false;
        } else {
            if (scanProgress > 0)
                scanProgress-=SCANNING_EFFECT_SPEED;
            else
                direction = true;
        }

        Log.d(Constant.APP_TAG, "scanprogress "+scanProgress);

        canvas.drawLine(smRatio_W * sW, smRatio_H * sH + scanProgress, bigRatio_W * sW, smRatio_H * sH + scanProgress, effectPaint);
    }

    private void drawRectangleWithBorder(Canvas canvas) {


    }

    private void drawTransparentOverlay(Canvas canvas) {

        Path mPath = new Path();

        Log.d("scanmycodexxx", "sm_W "+smRatio_W+" --- "+"bg_W --- "+bigRatio_W);
        Log.d("scanmycodexxx", "sm_H "+smRatio_H+" --- "+"bg_H --- "+bigRatio_H);

        mPath.lineTo(0,0);
        mPath.lineTo(sW, 0);
        mPath.lineTo(sW, smRatio_H*sH);
        mPath.lineTo(0, smRatio_H*sH);

        mPath.moveTo(0, bigRatio_H*sH);
        mPath.lineTo(sW,bigRatio_H*sH);
        mPath.lineTo(sW, sH);
        mPath.lineTo(0, sH);
        mPath.lineTo(0, bigRatio_H*sH);


        mPath.lineTo(0, smRatio_H*sH);
        mPath.lineTo(smRatio_W*sW, smRatio_H*sH);
        mPath.lineTo(smRatio_W*sW, bigRatio_H*sH);
        mPath.lineTo(0, bigRatio_H*sH);

        mPath.moveTo(bigRatio_W*sW, smRatio_H*sH);
        mPath.lineTo(sW, smRatio_H*sH);
        mPath.lineTo(sW, bigRatio_H*sH);
        mPath.lineTo(bigRatio_W*sW, bigRatio_H*sH);
        mPath.lineTo(bigRatio_W*sW, smRatio_H*sH);

        canvas.drawPath(mPath, mOverlayPaint);
    }

    @Override
    public void run() {

      /*  while (running) {
//            try {
//                Thread.sleep(500);

                if (direction == 0) {
                    scanProgress++;
                } else {
                    scanProgress--;
                }

                if (scanProgress == 0) {
                    direction = (direction == 0 ? -1 : 0);
                } else if (scanProgress == scanningHeight) {
                    direction = (direction == 0 ? -1 : 0);
                }

                Log.d("xxx", " *** scanningHeight " + scanningHeight);
                Log.d("xxx", " *** scanProgress " + scanProgress);
//            } catch (InterruptedException e) {
//                e.printStackTrace();

        }*/
    }
}
