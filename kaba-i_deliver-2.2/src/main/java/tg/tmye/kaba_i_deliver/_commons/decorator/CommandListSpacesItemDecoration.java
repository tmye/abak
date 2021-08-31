package tg.tmye.kaba_i_deliver._commons.decorator;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */

public class CommandListSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int bottom;
    private final int top;

    public CommandListSpacesItemDecoration(int top, int bottom) {
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