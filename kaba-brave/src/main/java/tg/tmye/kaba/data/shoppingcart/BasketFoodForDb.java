package tg.tmye.kaba.data.shoppingcart;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

import tg.tmye.kaba._commons.greendao.converter.StringConverter;
import tg.tmye.kaba.data.Food.Food_Tag;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;

/**
 * By abiguime on 22/02/2018.
 * email: 2597434002@qq.com
 */

public class BasketFoodForDb implements Parcelable {

    /**
     *  @Id
        public long id;
        public Long food_id;
        public Long restaurant_id;
    **/

    @Id
    public long id;

    /* food id */
    public long food_id;

    /* quantity */
    public long quantity;

    public long restaurant_id;

    @Transient
    public Restaurant_Menu_FoodEntity foodEntity;

    @Convert(columnType = String.class, converter = StringConverter.class)
    public long[] etags_id;

    @Transient
    public List<Food_Tag> etags;


    public BasketFoodForDb() {

    }


    protected BasketFoodForDb(Parcel in) {
        id = in.readLong();
        food_id = in.readInt();
        quantity = in.readInt();
        foodEntity = in.readParcelable(Restaurant_Menu_FoodEntity.class.getClassLoader());
        etags_id = in.createLongArray();
    }

    public static final Creator<BasketFoodForDb> CREATOR = new Creator<BasketFoodForDb>() {
        @Override
        public BasketFoodForDb createFromParcel(Parcel in) {
            return new BasketFoodForDb(in);
        }

        @Override
        public BasketFoodForDb[] newArray(int size) {
            return new BasketFoodForDb[size];
        }
    };


    public static List<BasketFoodForDb> fakeBasketFoodFromRestaurantId(long restaurant_id, int count) {

        List<BasketFoodForDb> basketFoodForDbItems = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            BasketFoodForDb bf = new BasketFoodForDb();
            bf.quantity = Integer.valueOf(i%3);
            bf.food_id = 2+i;
            bf.id = i+i;
            basketFoodForDbItems.add(bf);
        }

        return basketFoodForDbItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(food_id);
        parcel.writeLong(quantity);
        parcel.writeParcelable(foodEntity, i);
        parcel.writeLongArray(etags_id);
    }
}
