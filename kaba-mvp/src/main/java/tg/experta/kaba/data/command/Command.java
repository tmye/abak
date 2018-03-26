package tg.experta.kaba.data.command;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

import tg.experta.kaba.data.Restaurant.RestaurantEntity;

/**
 * By abiguime on 2017/12/15.
 * email: 2597434002@qq.com
 */

@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class Command {

    @Id
    public Long id;

    /* list of food and count that has been commanded */


    /* launch time */
//    public Long launch_time;


    /* expire date (paying + 3hours) */
//    public Long expire_date;


    /*
     * - check how maps look in json
      * */


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Command() {
    }

    @Generated(hash = 414653553)
    public Command(Long id) {
        this.id = id;
    }

    public static List<Command> fakeList(int count) {

        List<Command> tmp = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tmp.add(new Command());
        }
        return tmp;
    }
}
