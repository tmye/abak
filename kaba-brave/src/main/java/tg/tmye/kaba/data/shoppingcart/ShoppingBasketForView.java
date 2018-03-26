package tg.tmye.kaba.data.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 24/02/2018.
 * email: 2597434002@qq.com
 */

public class ShoppingBasketForView {

    public Long id;
    public int restaurant_id;
    public RestaurantEntity restaurantEntity;
    public List<BasketFoodForDb> shopping_basket_item;

    public static List<ShoppingBasketForView> fakeList(int count) {

        List<ShoppingBasketForView> shoppingBasketForViews = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ShoppingBasketForView shoppingBasketForView = new ShoppingBasketForView();
            shoppingBasketForView.id = Long.valueOf(i);
            shoppingBasketForView.restaurantEntity = RestaurantEntity.fakeList(1).get(0);
            shoppingBasketForView.shopping_basket_item = BasketFoodForDb.fakeBasketFoodFromRestaurantId(shoppingBasketForView.restaurantEntity.id, count);
            shoppingBasketForViews.add(shoppingBasketForView);
        }

        return shoppingBasketForViews;
    }


}
