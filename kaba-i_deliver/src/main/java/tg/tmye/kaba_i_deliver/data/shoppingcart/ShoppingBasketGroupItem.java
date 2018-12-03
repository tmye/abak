package tg.tmye.kaba_i_deliver.data.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;


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

}
