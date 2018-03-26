package tg.tmye.kaba.data.favorite;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * By abiguime on 27/02/2018.
 * email: 2597434002@qq.com
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class Favorite {

    @Id
    public long id;

    public long food_id;

    public long restaurant_id;

    public long bought_times; /* how many times did i buy these */

@Generated(hash = 880053116)
public Favorite(long id, long food_id, long restaurant_id, long bought_times) {
    this.id = id;
    this.food_id = food_id;
    this.restaurant_id = restaurant_id;
    this.bought_times = bought_times;
}

@Generated(hash = 459811785)
public Favorite() {
}

public long getId() {
    return this.id;
}

public void setId(long id) {
    this.id = id;
}

public long getFood_id() {
    return this.food_id;
}

public void setFood_id(long food_id) {
    this.food_id = food_id;
}

public long getRestaurant_id() {
    return this.restaurant_id;
}

public void setRestaurant_id(long restaurant_id) {
    this.restaurant_id = restaurant_id;
}

public long getBought_times() {
    return this.bought_times;
}

public void setBought_times(long bought_times) {
    this.bought_times = bought_times;
}

}
