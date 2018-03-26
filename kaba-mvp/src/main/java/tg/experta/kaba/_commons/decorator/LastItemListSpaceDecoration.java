package tg.experta.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import tg.experta.kaba.R;
import tg.experta.kaba._commons.utils.UtilFunctions;

/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */

public class LastItemListSpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public LastItemListSpaceDecoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1) {
            outRect.bottom = space;
        }
    }

}
