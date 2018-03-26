package tg.experta.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */

public class SpacesItem_4_8_Decoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItem_4_8_Decoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        /*
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right = space/2;
        } else {
            outRect.left = space/2;
        }*/

        if (parent.getChildAdapterPosition(view)  == 1 ||
                parent.getChildAdapterPosition(view)  == 5) {
            outRect.right = space/2;
        }

        if (parent.getChildAdapterPosition(view)  == 2 ||
                parent.getChildAdapterPosition(view)  == 6) {
            outRect.left = space/2;
        }

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) < 4) {
            outRect.top = 0;
        } else {
            outRect.top = space;
        }

        // if u are the first of the list, nothing left, something right

    }

}
