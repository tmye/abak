package tg.experta.kaba.data.Menu.source;

import java.util.ArrayList;
import java.util.List;

import tg.experta.kaba.data.Menu.Restaurant_SubMenuEntity;
import tg.experta.kaba.data.Menu.Restaurant_SubMenuEntityDao;
import tg.experta.kaba.data.Restaurant.RestaurantEntity;
import tg.experta.kaba.data._OtherEntities.ContactDao;
import tg.experta.kaba.data._OtherEntities.DaoSession;

/**
 * By abiguime on 21/12/2017.
 * email: 2597434002@qq.com
 */

public class MenuDbLocalSource {


    public static List<Restaurant_SubMenuEntity> loadAllSubMenusOfRestaurant(Restaurant_SubMenuEntityDao menuEntityDao, RestaurantEntity restaurantEntity) {

        List<Restaurant_SubMenuEntity> menus = menuEntityDao
                .queryBuilder()
                .where(Restaurant_SubMenuEntityDao.Properties.Restaurant_id.eq(restaurantEntity.getId()))
                .list();
        return menus;
    }


}
