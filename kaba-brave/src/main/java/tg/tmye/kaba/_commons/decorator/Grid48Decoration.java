package tg.tmye.kaba._commons.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */

public class Grid48Decoration extends RecyclerView.ItemDecoration {


    private int space;

    public Grid48Decoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {



        if (parent.getChildAdapterPosition(view)  == 0) {
//            outRect.bottom = space/2;
            outRect.right = space/2;
        }

        if (parent.getChildAdapterPosition(view)  == 1) {
//            outRect.bottom = space/2;
        }

//        if (parent.getChildAdapterPosition(view)  == 2) {
//            outRect.right = space/2;
//        }
//
//        if (parent.getChildAdapterPosition(view)  == 3) {
//
//        }
    }

}
