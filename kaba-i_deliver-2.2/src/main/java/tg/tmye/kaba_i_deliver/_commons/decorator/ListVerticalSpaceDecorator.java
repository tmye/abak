package tg.tmye.kaba_i_deliver._commons.decorator;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * By abiguime on 09/01/2018.
 * email: 2597434002@qq.com
 */

public class ListVerticalSpaceDecorator extends RecyclerView.ItemDecoration {

    private int space;

    public ListVerticalSpaceDecorator (int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;
    }
}
