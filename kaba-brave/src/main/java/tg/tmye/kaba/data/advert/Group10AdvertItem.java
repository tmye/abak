package tg.tmye.kaba.data.advert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * By abiguime on 21/02/2018.
 * email: 2597434002@qq.com
 */

public class Group10AdvertItem {

    public String title;
    public String title_code_color;
    public ProductAdvertItem adOne;
    public ProductAdvertItem adOnePrime;

    /* content of first recyclerview */
    public List<ProductAdvertItem> level1_ads;
    public List<ProductAdvertItem> level2_ads;

    public static List<Group10AdvertItem> fakeList(int indice) {

        List<Group10AdvertItem> list = new ArrayList<>();

        for (int i = 0; i < indice; i++) {
            Group10AdvertItem ad = new Group10AdvertItem();
            ad.adOne = new ProductAdvertItem(long_image_array[new Random().nextInt(long_image_array.length)], null);
            ad.adOnePrime = new ProductAdvertItem(square_image_array[new Random().nextInt(square_image_array.length)], null);
            ad.level1_ads = fakeproductItemList(2);
            ad.level2_ads = fakeproductItemList(4);
            ad.title_code_color = groupAdTitleThemeColor[new Random().nextInt(groupAdTitleThemeColor.length)];
            ad.title = groupAdTitle[new Random().nextInt(groupAdTitle.length)];
            list.add(ad);
        }
        return list;
    }


    public static List<ProductAdvertItem> fakeproductItemList (int count) {
        List<ProductAdvertItem> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ProductAdvertItem item = new ProductAdvertItem(image_no_background_array[new Random().nextInt(image_no_background_array.length)], null);
            list.add(item);
        }
        return list;
    }


    /* fake database */
    public static  String[] long_image_array = {
            "/sample/rectangle/cover_pub_01.jpg"
    };

    public static  String[] square_image_array = {
            "/sample/square/cover_pub_02.jpg"
    };

    public static  String[] image_no_background_array = {
            "/sample/wbg/shirt_spl_3.png",
            "/sample/wbg/shirt_spl_4.png",
            "/sample/wbg/shirt_spl_5.png",
            "/sample/wbg/shirt_spl_6.png",
            "/sample/wbg/shirt_spl_7.png"
    };

    public static  String[] groupAdTitle = {
            "BEEGRICS", "JIMSCOM", "TMYE", "SPACEX", "TESLA", "GOUV", "SUPERAMCO"
    };

    public static  String[] groupAdTitleThemeColor = {
            "#911001",  "#017021",  "#110031", "#F10F0F"
    };

    public static  String[] adThemeText = {
            "NOEL EN FAMILLE !", "NOUVEL ARRIVAGE!", "RENDEZ-NOUS VISITE!", "MANGEZ PLUS SAINT!", "PRENEZ SOIN DE LA PLANETE!"
    };



}
