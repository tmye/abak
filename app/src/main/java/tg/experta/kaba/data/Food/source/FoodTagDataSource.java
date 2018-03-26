package tg.experta.kaba.data.Food.source;

import java.util.List;

import tg.experta.kaba.data.Food.Food_Tag;
import tg.experta.kaba.data.Food.Food_TagDao;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.experta.kaba.data.Food.Restaurant_Menu_FoodEntityDao;

/**
 * By abiguime on 20/12/2017.
 * email: 2597434002@qq.com
 */

public class FoodTagDataSource {

    public static List<Food_Tag> findByFoodId (Food_TagDao dao, long _id) {

        List<Food_Tag> foods = dao.queryBuilder()
                .where(Food_TagDao.Properties.Food_id.eq(_id))
                .orderAsc(Food_TagDao.Properties.Id)
                .list();

        return foods;
    }


}
