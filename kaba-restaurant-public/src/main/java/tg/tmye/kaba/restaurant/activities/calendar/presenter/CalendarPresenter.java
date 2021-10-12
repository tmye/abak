package tg.tmye.kaba.restaurant.activities.calendar.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tg.tmye.kaba.restaurant.ILog;
import tg.tmye.kaba.restaurant._commons.MultiThreading.NetworkRequestThreadBase;
import tg.tmye.kaba.restaurant.activities.calendar.contract.CalendarContract;
import tg.tmye.kaba.restaurant.activities.stats.contract.RestaurantStatsContract;
import tg.tmye.kaba.restaurant.data._OtherEntities.StatsEntity;
import tg.tmye.kaba.restaurant.data._OtherEntities.source.StatsRepository;
import tg.tmye.kaba.restaurant.data.calendar.CalendarModel;
import tg.tmye.kaba.restaurant.data.calendar.source.CalendarRepository;
import tg.tmye.kaba.restaurant.data.hsn.HSN;

/**
 * By abiguime on 2021/9/28.
 * email: 2597434002@qq.com
 */
public class CalendarPresenter implements CalendarContract.Presenter {


    private final CalendarRepository repository;
    private final CalendarContract.View view;
    private Gson gson = new Gson();

    private boolean isLoading = false;

    public CalendarPresenter(CalendarContract.View view, CalendarRepository repository) {

        this.view = view;
        this.repository = repository;
    }


    @Override
    public void loadCalendar() {
        if (isLoading)
            return;

        isLoading = true;
        view.showLoading(true);
        repository.loadCalendar(new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                isLoading = false;
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                isLoading = false;
                view.showLoading(false);
                view.syserror();
            }

            @Override
            public void onSuccess(String jsonResponse) {

//                L"error":0,"message":"","data":{"pending":[{"id":21,"customer_username":"92109474","last_update":1549061761,"last_update_sample":"2019-02-01 22:56:01","start_time":"2019-02-01 22:56:01","start_date":"2019-02-01","state":0,"reason":null,"state_show":"En attente","restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"shipping_address":{"id":77,"user_id":77,"location":"6.211997:1.185470","name":"Chez moi","description":"Non loin de l'Eglise MIGEC","indication":null,"quartier":"Cacaveli","description_details":"Non loin de l'Eglise MIGEC","near":"","picture":[],"phone_number":"92109474"},"shipping_pricing":"500","food_list":[{"id":390,"name":"Cordon bleu","description":null,"priority":0,"promotion":1,"promotion_price":"2300","pic":"food_pic\/oTXoWvvyXjeNYCr.jpg","food_details_pictures":["food_pic\/KHABkSwLIIca4bN.jpg","food_pic\/cLYqDgfsgGuL3BA.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"29-01-2019 16:09:18pm","rating_quantity":0,"rating_percentage":0,"quantity":1,"food_description":null},{"id":391,"name":"Filet de b\u0153uf ","description":null,"priority":0,"promotion":0,"promotion_price":null,"pic":"food_pic\/ldvUjWgXHKX6pEs.jpg","food_details_pictures":["food_pic\/7mjjCwewcHE4jqk.jpg","food_pic\/eqWEyLuASAJPzoY.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"10-10-2018 15:41:47pm","rating_quantity":0,"rating_percentage":0,"quantity":1,"food_description":null}],"passphrase":"connecter","is_payed_at_arrival":false,"price_command":7000,"price_remise":5800,"total":6300}],"cooking":[],"shipping":[{"id":20,"customer_username":"92109474","last_update":1549062902,"last_update_sample":"2019-02-01 23:15:02","start_time":"2019-02-01 20:45:01","start_date":"2019-02-01","state":2,"reason":null,"state_show":"En livraison","livreur":{"id":1,"name":"Tani Yao","is_active":true,"workcontact":"0987654","employee_entity":{"id":1,"first_name":"Tani","last_name":"Yao","address":"23456789","job_title":"Livreur","contact":"23456789","num_cnss":"4567890","num_cni":"456789","pic":"employee_picture\/"},"restaurant":[{"id":7,"name":"Le phenicien","description":"Un autre restaurant upp\u00e9 de la capitale","email":"lephenicien@kaba.com","working_hour":"10:00-23:00","is_open":0,"self_open":0,"address":"Lom\u00e9","pic":"\/resto_pic\/re6.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null},{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null},{"id":10,"name":"Festival des Glaces","description":"Tu veux des glaces? Tu sais ou aller!","email":"festivaldesglaces@kaba.com","working_hour":"10:00-22:00","is_open":0,"self_open":0,"address":"Dekon","pic":"\/resto_pic\/restaurant_101541001630.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme101536898883.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null}],"vehicle":"CB TG 1744","is_available":true},"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"shipping_address":{"id":57,"user_id":57,"location":"6.217675:1.188539","name":"Chez Francis - Ul.","description":"Ago\u00e8-Assiy\u00e9y\u00e9, Agouenyive, R\u00e9gion Maritime, BP 02, Togo","indication":null,"quartier":"Ago\u00e8-Assiy\u00e9y\u00e9","description_details":"Ago\u00e8-Assiy\u00e9y\u00e9, Agouenyive, R\u00e9gion Maritime, BP 02, Togo","near":"Non Loin de Fil O Parc","picture":["wfWUX7JsbKEbQh2.jpg"],"phone_number":"99105978"},"shipping_pricing":"1000","food_list":[{"id":390,"name":"Cordon bleu","description":null,"priority":0,"promotion":1,"promotion_price":"2300","pic":"food_pic\/oTXoWvvyXjeNYCr.jpg","food_details_pictures":["food_pic\/KHABkSwLIIca4bN.jpg","food_pic\/cLYqDgfsgGuL3BA.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"29-01-2019 16:09:18pm","rating_quantity":0,"rating_percentage":0,"quantity":2,"food_description":null},{"id":391,"name":"Filet de b\u0153uf ","description":null,"priority":0,"promotion":0,"promotion_price":null,"pic":"food_pic\/ldvUjWgXHKX6pEs.jpg","food_details_pictures":["food_pic\/7mjjCwewcHE4jqk.jpg","food_pic\/eqWEyLuASAJPzoY.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"10-10-2018 15:41:47pm","rating_quantity":0,"rating_percentage":0,"quantity":2,"food_description":null}],"passphrase":"irritation","is_payed_at_arrival":false,"price_command":14000,"price_remise":11600,"total":12600}],"delivered":[],"cancelled":[],"rejected":[]}}og.d(Constant.APP_TAG, jsonResponse);
                view.showLoading(false);
                isLoading = false;
                try {

                    ILog.print(jsonResponse);

                    JsonObject obj = new JsonParser().parse(jsonResponse).getAsJsonObject();

                    JsonObject data = obj.get("data").getAsJsonObject();

                    CalendarModel[] calendar = gson.fromJson(data.get("content"), new TypeToken<CalendarModel[]>() {}.getType());

                    view.inflateCalendar(calendar);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.syserror();
                }
            }
        });
    }

    @Override
    public void updateCalendar(CalendarModel[] calendar) {

        if (isLoading)
            return;

        isLoading = true;
        view.showLoading(true);
        repository.updateCalendar(calendar, new NetworkRequestThreadBase.NetRequestIntf<String>() {
            @Override
            public void onNetworkError() {
                isLoading = false;
                view.showLoading(false);
                view.networkError();
            }

            @Override
            public void onSysError() {
                isLoading = false;
                view.showLoading(false);
                view.syserror();
            }

            @Override
            public void onSuccess(String response) {

//                L"error":0,"message":"","data":{"pending":[{"id":21,"customer_username":"92109474","last_update":1549061761,"last_update_sample":"2019-02-01 22:56:01","start_time":"2019-02-01 22:56:01","start_date":"2019-02-01","state":0,"reason":null,"state_show":"En attente","restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"shipping_address":{"id":77,"user_id":77,"location":"6.211997:1.185470","name":"Chez moi","description":"Non loin de l'Eglise MIGEC","indication":null,"quartier":"Cacaveli","description_details":"Non loin de l'Eglise MIGEC","near":"","picture":[],"phone_number":"92109474"},"shipping_pricing":"500","food_list":[{"id":390,"name":"Cordon bleu","description":null,"priority":0,"promotion":1,"promotion_price":"2300","pic":"food_pic\/oTXoWvvyXjeNYCr.jpg","food_details_pictures":["food_pic\/KHABkSwLIIca4bN.jpg","food_pic\/cLYqDgfsgGuL3BA.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"29-01-2019 16:09:18pm","rating_quantity":0,"rating_percentage":0,"quantity":1,"food_description":null},{"id":391,"name":"Filet de b\u0153uf ","description":null,"priority":0,"promotion":0,"promotion_price":null,"pic":"food_pic\/ldvUjWgXHKX6pEs.jpg","food_details_pictures":["food_pic\/7mjjCwewcHE4jqk.jpg","food_pic\/eqWEyLuASAJPzoY.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"10-10-2018 15:41:47pm","rating_quantity":0,"rating_percentage":0,"quantity":1,"food_description":null}],"passphrase":"connecter","is_payed_at_arrival":false,"price_command":7000,"price_remise":5800,"total":6300}],"cooking":[],"shipping":[{"id":20,"customer_username":"92109474","last_update":1549062902,"last_update_sample":"2019-02-01 23:15:02","start_time":"2019-02-01 20:45:01","start_date":"2019-02-01","state":2,"reason":null,"state_show":"En livraison","livreur":{"id":1,"name":"Tani Yao","is_active":true,"workcontact":"0987654","employee_entity":{"id":1,"first_name":"Tani","last_name":"Yao","address":"23456789","job_title":"Livreur","contact":"23456789","num_cnss":"4567890","num_cni":"456789","pic":"employee_picture\/"},"restaurant":[{"id":7,"name":"Le phenicien","description":"Un autre restaurant upp\u00e9 de la capitale","email":"lephenicien@kaba.com","working_hour":"10:00-23:00","is_open":0,"self_open":0,"address":"Lom\u00e9","pic":"\/resto_pic\/re6.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null},{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null},{"id":10,"name":"Festival des Glaces","description":"Tu veux des glaces? Tu sais ou aller!","email":"festivaldesglaces@kaba.com","working_hour":"10:00-22:00","is_open":0,"self_open":0,"address":"Dekon","pic":"\/resto_pic\/restaurant_101541001630.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme101536898883.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":true,"cooking_time":null}],"vehicle":"CB TG 1744","is_available":true},"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"shipping_address":{"id":57,"user_id":57,"location":"6.217675:1.188539","name":"Chez Francis - Ul.","description":"Ago\u00e8-Assiy\u00e9y\u00e9, Agouenyive, R\u00e9gion Maritime, BP 02, Togo","indication":null,"quartier":"Ago\u00e8-Assiy\u00e9y\u00e9","description_details":"Ago\u00e8-Assiy\u00e9y\u00e9, Agouenyive, R\u00e9gion Maritime, BP 02, Togo","near":"Non Loin de Fil O Parc","picture":["wfWUX7JsbKEbQh2.jpg"],"phone_number":"99105978"},"shipping_pricing":"1000","food_list":[{"id":390,"name":"Cordon bleu","description":null,"priority":0,"promotion":1,"promotion_price":"2300","pic":"food_pic\/oTXoWvvyXjeNYCr.jpg","food_details_pictures":["food_pic\/KHABkSwLIIca4bN.jpg","food_pic\/cLYqDgfsgGuL3BA.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"29-01-2019 16:09:18pm","rating_quantity":0,"rating_percentage":0,"quantity":2,"food_description":null},{"id":391,"name":"Filet de b\u0153uf ","description":null,"priority":0,"promotion":0,"promotion_price":null,"pic":"food_pic\/ldvUjWgXHKX6pEs.jpg","food_details_pictures":["food_pic\/7mjjCwewcHE4jqk.jpg","food_pic\/eqWEyLuASAJPzoY.jpg"],"price":"3500","menu_id":"71","is_deleted":0,"restaurant_entity":{"id":9,"name":"Opera","description":"Opera, le restaurant le plus quot\u00e9 du Togo.","email":"opera@kaba.com","working_hour":"09:00-23:00","is_open":1,"self_open":1,"address":"Boulevard du Mono","pic":"\/resto_pic\/restaurant_91541001591.jpg","promotion_pic":"\/promotion_pic\/","theme_pic":"theme_pic\/theme91532965695.jpg","main_contact":"9XXXXXXXX","max_pay":null,"coming_soon":0,"enabled":1,"cooking_time":null,"stars":3,"votes":2},"lastupdate":"10-10-2018 15:41:47pm","rating_quantity":0,"rating_percentage":0,"quantity":2,"food_description":null}],"passphrase":"irritation","is_payed_at_arrival":false,"price_command":14000,"price_remise":11600,"total":12600}],"delivered":[],"cancelled":[],"rejected":[]}}og.d(Constant.APP_TAG, jsonResponse);
                view.showLoading(false);
                isLoading = false;
                try {

                    JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
                    // get the error object and make sure it's == 0
                    int error = obj.get("error").getAsInt();
                    view.modificationSuccess(error == 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.syserror();
                    view.modificationSuccess(false);
                }
            }
        });

    }

    @Override
    public void start() {

    }
}