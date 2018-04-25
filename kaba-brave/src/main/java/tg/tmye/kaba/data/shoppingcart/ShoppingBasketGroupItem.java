package tg.tmye.kaba.data.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 24/02/2018.
 * email: 2597434002@qq.com
 */

public class ShoppingBasketGroupItem {

    public Long id;
    public int restaurant_id;
    public RestaurantEntity restaurant_entity;
    public List<BasketInItem> food_list;
    public int total;

    public static List<ShoppingBasketGroupItem> fakeList(int count) {

        List<ShoppingBasketGroupItem> shoppingBasketGroupItems = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ShoppingBasketGroupItem shoppingBasketGroupItem = new ShoppingBasketGroupItem();
            shoppingBasketGroupItem.id = Long.valueOf(i);
            shoppingBasketGroupItem.restaurant_entity = RestaurantEntity.fakeList(1).get(0);
//            shoppingBasketGroupItem.shopping_basket_item = BasketInItem.fakeBasketFoodFromRestaurantId(shoppingBasketGroupItem.restaurantEntity.id, count);
            shoppingBasketGroupItems.add(shoppingBasketGroupItem);
        }

        return shoppingBasketGroupItems;
    }


}
