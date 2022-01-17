package tg.tmye.kaba.partner.cviews.command_details_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import tg.tmye.kaba.partner.R;


/**
 * By abiguime on 14/05/2018.
 * email: 2597434002@qq.com
 */

public class CommandProgressView extends LinearLayout {


    private static int height;
    private static int width;


    /* views */
    CommandProgressSingleLine lny_1_waiting;
    CommandProgressSingleLine lny_2_preparing;
    CommandProgressSingleLine lny_3_shipping;
    CommandProgressSingleLine lny_4_end;


    CommandProgress_BallView ball_1_waiting;
    CommandProgress_BallView ball_2_preparing;
    CommandProgress_BallView ball_3_shipping;
    CommandProgress_BallView ball_4_end;

    CommandProgress_LineView line_1_waiting_preparing;
    CommandProgress_LineView line_2_preparing_shipping;
    CommandProgress_LineView line_3_shipping_end;

    TextView tv_1_waiting;
    TextView tv_2_preparing;
    TextView tv_3_shipping;
    TextView tv_4_end;


    private String standardColor = "#33333333";


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // tv
        tv_1_waiting = findViewById(R.id.tv_1_waiting);
        tv_2_preparing = findViewById(R.id.tv_2_preparing);
        tv_3_shipping = findViewById(R.id.tv_3_shipping);
        tv_4_end = findViewById(R.id.tv_4_end);
        // balls
        ball_1_waiting = findViewById(R.id.ball_1_waiting);
        ball_2_preparing = findViewById(R.id.ball_2_preparing);
        ball_3_shipping = findViewById(R.id.ball_3_shipping);
        ball_4_end = findViewById(R.id.ball_4_end);
        // single line
        lny_1_waiting = findViewById(R.id.lny_1_waiting);
        lny_2_preparing = findViewById(R.id.lny_2_preparing);
        lny_3_shipping = findViewById(R.id.lny_3_shipping);
        lny_4_end = findViewById(R.id.lny_4_end);
        // line view
        line_1_waiting_preparing = findViewById(R.id.line_1_waiting_preparing);
        line_2_preparing_shipping = findViewById(R.id.line_2_preparing_shipping);
        line_3_shipping_end = findViewById(R.id.line_3_shipping_end);
    }

    public CommandProgressView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public CommandProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public CommandProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    public void setCommandState (int state) {

        disableAll();
        switch (state) {
            case 0:
                // waiting
                tv_1_waiting.setVisibility(VISIBLE);
//                tv_1_waiting.setTextColor(Color.parseColor(standardColor));
                tv_1_waiting.setTextColor(Color.parseColor("#000000"));
                ball_1_waiting.iEnable(true, true);
                break;
            case 1:
                // cooking
                tv_1_waiting.setTextColor(Color.parseColor(standardColor));
                tv_1_waiting.setVisibility(VISIBLE);
                ball_1_waiting.iEnable(false, false);
                /* line 1 */line_1_waiting_preparing.iEnable(true);;
                tv_2_preparing.setVisibility(VISIBLE);
                tv_2_preparing.setTextColor(Color.parseColor("#000000"));
                ball_2_preparing.iEnable(true, true);
                break;
            case 2:
                // shipping
                tv_1_waiting.setVisibility(VISIBLE);
                tv_1_waiting.setTextColor(Color.parseColor(standardColor));
                ball_1_waiting.iEnable(false, false);
                /* line 1 */line_1_waiting_preparing.iEnable(true);;
                tv_2_preparing.setVisibility(VISIBLE);
                tv_2_preparing.setTextColor(Color.parseColor(standardColor));
                ball_2_preparing.iEnable(false, false);
                /* line 2 */line_2_preparing_shipping.iEnable(true);;
                tv_3_shipping.setVisibility(VISIBLE);
                tv_3_shipping.setTextColor(Color.parseColor("#000000"));
                ball_3_shipping.iEnable(true, true);
                break;
            case 3:
                // end shipping
                // shipping
                tv_1_waiting.setVisibility(VISIBLE);
                tv_1_waiting.setTextColor(Color.parseColor(standardColor));
                ball_1_waiting.iEnable(false, false);
                /* line 1 */line_1_waiting_preparing.iEnable(true);;
                tv_2_preparing.setVisibility(VISIBLE);
                tv_2_preparing.setTextColor(Color.parseColor(standardColor));
                ball_2_preparing.iEnable(false, false);
                /* line 2 */line_2_preparing_shipping.iEnable(true);;
                tv_3_shipping.setVisibility(VISIBLE);
                tv_3_shipping.setTextColor(Color.parseColor(standardColor));
                ball_3_shipping.iEnable(false, false);
                /* line 3 */line_3_shipping_end.iEnable(true);;
                tv_4_end.setVisibility(VISIBLE);
                tv_4_end.setTextColor(Color.parseColor("#000000"));
                ball_4_end.iEnable(true, true);
                break;
        }
    }

    private void disableAll() {

        /* all tv invisible */
        tv_4_end.setVisibility(INVISIBLE);
        tv_3_shipping.setVisibility(INVISIBLE);
        tv_2_preparing.setVisibility(INVISIBLE);
        tv_1_waiting.setVisibility(INVISIBLE);

        ball_1_waiting.iEnable(false, false);
        ball_2_preparing.iEnable(false, false);
        ball_3_shipping.iEnable(false, false);
        ball_4_end.iEnable(false, false);
        line_1_waiting_preparing.iEnable(false);
        line_2_preparing_shipping.iEnable(false);
        line_3_shipping_end.iEnable(false);

    }

}
