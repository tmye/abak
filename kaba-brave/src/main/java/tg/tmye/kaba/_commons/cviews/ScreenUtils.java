package tg.tmye.kaba._commons.cviews;

import android.content.Context;

/**
 * By abiguime on 2017/10/29.
 * email: 2597434002@qq.com
 */

public class ScreenUtils {

    public enum SCREEN {
        WIDTH, LONG
    }

    public static int getScreenSize (Context ctx, SCREEN direction) {

        if (direction == SCREEN.WIDTH) {
            return ctx.getResources().getDisplayMetrics().widthPixels;
        } else if (direction == SCREEN.LONG)  {
            return ctx.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

}
