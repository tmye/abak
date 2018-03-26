package tg.tmye.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */

public class GridRecyclerViewDecorator extends RecyclerView.ItemDecoration {
    private int space;

    public GridRecyclerViewDecorator(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.right = space/2;
        outRect.left = space/2;
        outRect.top = space/2;
        outRect.bottom = space/2;
    }

}
