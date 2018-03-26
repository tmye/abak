package tg.tmye.kaba.data.Menu.source;

import android.content.Context;

import java.util.List;

import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.tmye.kaba.data.Menu.Restaurant_SubMenuEntityDao;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class MenuRepository {

    private final Context context;

    public MenuRepository (Context context) {
        this.context = context;
    }

    public static List<Restaurant_SubMenuEntity> loadAllSubMenusOfRestaurant(Restaurant_SubMenuEntityDao menuEntityDao, RestaurantEntity restaurantEntity) {

        List<Restaurant_SubMenuEntity> menus = menuEntityDao
                .queryBuilder()
                .where(Restaurant_SubMenuEntityDao.Properties.Restaurant_id.eq(restaurantEntity.getId()))
                .list();
        return menus;
    }


}
