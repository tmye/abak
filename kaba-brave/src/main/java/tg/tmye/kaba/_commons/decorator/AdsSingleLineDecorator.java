package tg.tmye.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 11/01/2018.
 * email: 2597434002@qq.com
 */

public class AdsSingleLineDecorator  extends RecyclerView.ItemDecoration {

    private final boolean isTop;
    private int space;

    public AdsSingleLineDecorator(int space, boolean isTop) {
        this.space = space;
        this.isTop = isTop;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        // single line, if you are the first --- no left, last, no right

        if (parent.getChildAdapterPosition(view)  != 0 ){
            outRect.left = space/2;
        }

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()-1) {
            outRect.right = space/2;
        }

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) < 4) {
            outRect.top = 0;
        } else {
            outRect.top = space;
        }

    }

}
