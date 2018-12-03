package tg.tmye.kaba._commons.intf;

import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;

/**
 * By abiguime on 2018/11/21.
 * email: 2597434002@qq.com
 */
public interface OnMenuAddedToBasket extends View.OnClickListener {
    public void addMenu (CircleImageView origin_view, Restaurant_Menu_FoodEntity food_item);
}