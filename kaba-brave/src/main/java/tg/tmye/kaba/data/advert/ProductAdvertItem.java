package tg.tmye.kaba.data.advert;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tg.tmye.kaba.R;


/**
 * By abiguime on 2017/12/8.
 * email: 2597434002@qq.com
 */
@Entity(indexes = {
        @Index(value = "id", unique = true)
})
public class ProductAdvertItem {

    @Id
    public Long id;

    public String img_path;

    public int cloo;

    public String ad_code;

    public String ad_hash;

    public String expire_date;


    public ProductAdvertItem() {
    }

    public ProductAdvertItem(String img_path, String action) {
        this.img_path = img_path;
    }

    @Generated(hash = 1773170857)
    public ProductAdvertItem(Long id, String img_path, int cloo, String ad_code,
                             String ad_hash, String expire_date) {
        this.id = id;
        this.img_path = img_path;
        this.cloo = cloo;
        this.ad_code = ad_code;
        this.ad_hash = ad_hash;
        this.expire_date = expire_date;
    }

    public static List<ProductAdvertItem> fakeList(int i) {

        return Group10AdvertItem.fakeproductItemList(i);
    }

    public Long get_id() {
        return this.id;
    }

    public void set_id(Long _id) {
        this.id = _id;
    }

    public String getImg_path() {
        return this.img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getAd_code() {
        return this.ad_code;
    }

    public void setAd_code(String ad_code) {
        this.ad_code = ad_code;
    }

    public String getAd_hash() {
        return this.ad_hash;
    }

    public void setAd_hash(String ad_hash) {
        this.ad_hash = ad_hash;
    }

    public String getExpire_date() {
        return this.expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public int getCloo() {
        return this.cloo;
    }

    public void setCloo(int cloo) {
        this.cloo = cloo;
    }
}
