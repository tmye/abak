package tg.tmye.kaba.restaurant._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 13/08/2018.
 * email: 2597434002@qq.com
 */
public class StatsListSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int bottom;
    private final int top;

    public StatsListSpacesItemDecoration(int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.top = top;


        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
            outRect.bottom = bottom;
        }
    }
}
