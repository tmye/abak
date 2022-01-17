package tg.tmye.kaba.partner.activities.infos.contract

import tg.tmye.kaba.partner.BasePresenter
import tg.tmye.kaba.partner.BaseView
import tg.tmye.kaba.partner.data.hsn.HSN

/**
 * By abiguime on 2022/1/17.
 * email: 2597434002@qq.com
 */

interface ProfileContract {
    interface View : BaseView<Presenter?> {
        fun showIsLoading(isLoading: Boolean)
        /* different list of hsn */
        fun inflateRestaurantProfileInfo(hsns: List<HSN?>?)
        fun sysError()
        fun networkError()
    }

    interface Presenter : BasePresenter {
        /* update commands data basically */
        fun loadRestaurantProfileInfo()
    }
}