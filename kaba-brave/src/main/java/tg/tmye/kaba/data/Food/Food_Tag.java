package tg.tmye.kaba.data.Food;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * By abiguime on 2017/12/4.
 * email: 2597434002@qq.com
 */

public class Food_Tag implements Parcelable{


    /*
    *  {
        "_id": 0,
        "restaurant_name": "onion",
        "state": 1,
        "food_id": 1
      }*/


    public Long id;

    public Long food_id;

    public String name;

    public int state = 1; // 1 selected // 0 unselected

    public Food_Tag (String name) {
        this.name = name.toUpperCase();
    }

    protected Food_Tag(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            food_id = null;
        } else {
            food_id = in.readLong();
        }
        name = in.readString();
        state = in.readInt();
    }
 

    public Food_Tag() {
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (food_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(food_id);
        }
        dest.writeString(name);
        dest.writeInt(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Food_Tag> CREATOR = new Creator<Food_Tag>() {
        @Override
        public Food_Tag createFromParcel(Parcel in) {
            return new Food_Tag(in);
        }

        @Override
        public Food_Tag[] newArray(int size) {
            return new Food_Tag[size];
        }
    };

    public void switchState() {
        if (state == 0) {
            state = 1;
            return;
        } else if (state == 1) {
            state = 0;
            return;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static List<Food_Tag> generate(int food_tag_count) {

        List<Food_Tag> tags = new ArrayList<>();
        for (int i = 0; i < food_tag_count; i++) {

            tags.add(new Food_Tag("TAG_"+i));
        }
        return tags;
    }
}
