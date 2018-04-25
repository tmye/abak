package tg.tmye.kaba.data.command;

import java.util.List;

import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;

/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

public class Command {

    public Long id;
    public int restaurant_id;
    public int state;
    public RestaurantEntity restaurantEntity;
    public List<BasketInItem> command_list;

/*
    public static List<Command> fakeList(int count) {

        List<Command> commands = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Command command = new Command();
            command.id = Long.valueOf(i);
            command.restaurantEntity = RestaurantEntity.fakeList(1).get(0);
            command.command_list = BasketInItem.fakeBasketFoodFromRestaurantId(command.restaurantEntity.id, count);
            commands.add(command);
        }

        return commands;
    }*/

}
