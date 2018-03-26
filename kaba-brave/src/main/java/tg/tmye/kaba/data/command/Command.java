package tg.tmye.kaba.data.command;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.Food.Food_TagDao;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntityDao;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.RestaurantEntityDao;
import tg.tmye.kaba.data.shoppingcart.BasketFoodForDb;

/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

public class Command {

       /*{
        "id" : 89,
        "restaurant_id": 3,
        "command_list" : [
          {
            "food_id" : 8,
            "quantity" : 1,
            "etags_id": [1,2]
          },
          {
            "food_id" : 6,
            "quantity" : 2,
            "etags_id": []
          }
        ]
      }*/

    public Long id;
    public int restaurant_id;
    public int state;
    public RestaurantEntity restaurantEntity;
    public List<BasketFoodForDb> command_list;


    public static List<Command> fakeList(int count) {

        List<Command> commands = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Command command = new Command();
            command.id = Long.valueOf(i);
            command.restaurantEntity = RestaurantEntity.fakeList(1).get(0);
            command.command_list = BasketFoodForDb.fakeBasketFoodFromRestaurantId(command.restaurantEntity.id, count);
            commands.add(command);
        }

        return commands;
    }

    public Command fetchAll(RestaurantEntityDao restaurantDao, Restaurant_Menu_FoodEntityDao foodDao, Food_TagDao foodTagDao) {

        restaurantEntity = restaurantDao.loadByRowId(restaurant_id);
        for (int i = 0; i < command_list.size(); i++) {
            command_list.get(i).foodEntity = foodDao.loadByRowId(command_list.get(i).food_id);

            List<Food_Tag> food_tags = new ArrayList<>();
            for (int j = 0; j < command_list.get(i).etags_id.length; j++) {
                food_tags.add(foodTagDao.loadByRowId(command_list.get(i).etags_id[j]));
            }
            command_list.get(i).etags = food_tags;
        }
        return this;
    }
}
