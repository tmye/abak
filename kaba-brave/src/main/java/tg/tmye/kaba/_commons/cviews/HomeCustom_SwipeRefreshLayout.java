package tg.tmye.kaba._commons.cviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * By abiguime on 10/01/2018.
 * email: 2597434002@qq.com
 */

public class HomeCustom_SwipeRefreshLayout extends SwipeRefreshLayout {

    public HomeCustom_SwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public HomeCustom_SwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * The refresh indicator starting and resting position is always positioned
     * near the top of the refreshing content. This position is a consistent
     * location, but can be adjusted in either direction based on whether or not
     * there is a toolbar or actionbar present.
     * <p>
     * <strong>Note:</strong> Calling this will reset the position of the refresh indicator to
     * <code>start</code>.
     * </p>
     *
     *  scale Set to true if there is no view at a higher z-order than where the progress
     *              spinner is set to appear. Setting it to true will cause indicator to be scaled
     *              up rather than clipped.
     *  start The offset in pixels from the top of this view at which the
     *              progress spinner should appear.
     *  end The offset in pixels from the top of this view at which the
     *            progress spinner should come to rest after a successful swipe
     *            gesture.
     */

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setProgressViewOffset(false, 100, 200);
    }
}
