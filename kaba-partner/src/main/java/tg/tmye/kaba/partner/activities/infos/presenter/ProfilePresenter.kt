package tg.tmye.kaba.partner.activities.infos.presenter

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import tg.tmye.kaba.partner.ILog
import tg.tmye.kaba.partner._commons.MultiThreading.NetworkRequestThreadBase
import tg.tmye.kaba.partner._commons.utils.SimpleObjectHolder
import tg.tmye.kaba.partner.activities.hsn.contract.HSNDetailsContract
import tg.tmye.kaba.partner.activities.infos.contract.ProfileContract
import tg.tmye.kaba.partner.data.Food.Restaurant_Menu_FoodEntity
import tg.tmye.kaba.partner.data.Menu.Restaurant_SubMenuEntity
import tg.tmye.kaba.partner.data.Restaurant.RestaurantEntity
import tg.tmye.kaba.partner.data.Restaurant.source.RestaurantOnlineRepository
import tg.tmye.kaba.partner.data.calendar.CalendarModel
import tg.tmye.kaba.partner.data.command.source.CommandRepository
import java.lang.Exception

/**
 * By abiguime on 2022/1/17.
 * email: 2597434002@qq.com
 */
 class ProfilePresenter (view: ProfileContract.View?, restaurantRepository: RestaurantOnlineRepository) :
    ProfileContract.Presenter {

    private var isLoading: Boolean = false
    var restaurantRepository: RestaurantOnlineRepository? = restaurantRepository
    private val view: ProfileContract.View? = view
    private val gson = Gson()


    override fun loadPartnerProfileInfo() {
        if (isLoading) return
        isLoading = true
        view!!.showIsLoading(true)
        restaurantRepository?.loadRestaurantProfileInfo(object :
            NetworkRequestThreadBase.NetRequestIntf<String?> {
            override fun onNetworkError() {
                isLoading = false
                view.showIsLoading(false)
                view.networkError()
            }

            override fun onSysError() {
                isLoading = false
                view.showIsLoading(false)
                view.sysError()
            }

            override fun onSuccess(response: String?) {
                isLoading = false
                view.showIsLoading(false)

                ILog.print(response)
                /* parse restaurant data object */
                val obj: JsonObject = JsonParser().parse(response).getAsJsonObject()
                val data = obj["data"].asJsonObject
                // get the error object and make sure it's == 0
//                val error = data["error"].asInt
                // return object

               val restaurant = gson.fromJson<RestaurantEntity>(
                    data,
                    object : TypeToken<RestaurantEntity>() {}.type
               )
                view.inflateRestaurantProfileInfo(restaurant)

            }})
    }

    override fun updatePartnerProfile(restaurant : RestaurantEntity) {
        if (isLoading) return
        isLoading = true
        view!!.showIsLoading(true)
        restaurantRepository?.updatePartnerProfile(restaurant,object :
            NetworkRequestThreadBase.NetRequestIntf<String?> {
            override fun onNetworkError() {
                view.showIsLoading(false)
                view.networkError()
            }

            override fun onSysError() {
                view.showIsLoading(false)
                view.sysError()
            }

            override fun onSuccess(response: String?) {
                view.showIsLoading(false)

                /* parse restaurant data object */
                val obj: JsonObject = JsonParser().parse(response).getAsJsonObject()
                val data = obj["data"].asJsonObject
                // get the error object and make sure it's == 0
                val error = data["error"].asInt
                // return object

                /*    val calendar = gson.fromJson<Array<CalendarModel>>(
                        data["content"],
                        object : TypeToken<Array<CalendarModel?>?>() {}.type
                    )*/

            }})
    }

    override fun start() {
        TODO("Not yet implemented")
    }

}
