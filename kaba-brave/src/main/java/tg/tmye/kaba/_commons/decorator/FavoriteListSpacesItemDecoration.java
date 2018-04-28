package tg.tmye.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */

public class FavoriteListSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int bottom;
    private final int top;

    public FavoriteListSpacesItemDecoration(int top, int bottom) {
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