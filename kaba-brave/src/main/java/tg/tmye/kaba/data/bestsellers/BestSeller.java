package tg.tmye.kaba.data.bestsellers;

import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 06/07/2018.
 * email: 2597434002@qq.com
 */
public class BestSeller {

    /* best seller is a product */
    RestaurantEntity restaurant_entity;

    public int ranking;

    public Restaurant_Menu_FoodEntity food_entity;

    public int rating_quantity;

    public int rating_percentage;

    public int[] history; /* yest, the day before, the day before before */

}
