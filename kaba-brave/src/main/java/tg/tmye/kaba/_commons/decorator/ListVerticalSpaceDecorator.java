package tg.tmye.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class ListVerticalSpaceDecorator extends RecyclerView.ItemDecoration {

    private int bottom_space = -1;
    private int space;

    public ListVerticalSpaceDecorator (int space) {
        this.space = space;
    }

    public ListVerticalSpaceDecorator (int space, int bottom_space) {
        this.space = space;
        this.bottom_space = bottom_space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        /* last item bottom*/
        if (bottom_space!=-1 && parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
            outRect.bottom = bottom_space;
        } else {
            outRect.bottom = space;
        }
    }
}
