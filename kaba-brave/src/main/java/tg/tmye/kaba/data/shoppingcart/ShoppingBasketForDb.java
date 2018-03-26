package tg.tmye.kaba.data.shoppingcart;

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
public class ShoppingBasketForDb {

    @Id
    public long id;

    public Long food_id;

    public Long restaurant_id;

@Generated(hash = 1336807876)
public ShoppingBasketForDb(long id, Long food_id, Long restaurant_id) {
    this.id = id;
    this.food_id = food_id;
    this.restaurant_id = restaurant_id;
}

@Generated(hash = 1657770428)
public ShoppingBasketForDb() {
}

public long getId() {
    return this.id;
}

public void setId(long id) {
    this.id = id;
}

public Long getFood_id() {
    return this.food_id;
}

public void setFood_id(Long food_id) {
    this.food_id = food_id;
}

public Long getRestaurant_id() {
    return this.restaurant_id;
}

public void setRestaurant_id(Long restaurant_id) {
    this.restaurant_id = restaurant_id;
}



}
