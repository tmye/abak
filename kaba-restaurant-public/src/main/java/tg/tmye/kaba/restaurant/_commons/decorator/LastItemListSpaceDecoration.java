package tg.tmye.kaba.restaurant._commons.decorator;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

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
