package tg.tmye.kaba_i_deliver.activity.dailyreport

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import tg.tmye.kaba_i_deliver.R
import tg.tmye.kaba_i_deliver.activity.dailyreport.contract.DailyReportContract
import tg.tmye.kaba_i_deliver.activity.dailyreport.presenter.DailyReportPresenter
import tg.tmye.kaba_i_deliver.data.delivery.source.DeliveryManRepository
import tg.tmye.kaba_i_deliver.data.dailyreport.DailyReport
import tg.tmye.kaba_i_deliver.cviews.dialog.DailyReportConfirmationDialogFragment
import tg.tmye.kaba_i_deliver.activity.dailyreport.DailyReportHistoryActivity
import tg.tmye.kaba_i_deliver.activity.delivery.contract.DeliveryModeContract
import tg.tmye.kaba_i_deliver.cviews.dialog.LoadingDialogFragment

class DailyReportActivity : AppCompatActivity(), View.OnClickListener, DailyReportContract.View {
    var ed_fuel: EditText? = null
    var ed_credit: EditText? = null
    var ed_reparation: EditText? = null
    var ed_losses: EditText? = null
    var ed_parking: EditText? = null
    var ed_various: EditText? = null
    var tv_point_date: TextView? = null
    var bt_confirm_daily_report: Button? = null
    var bt_cancel: Button? = null
    var presenter: DailyReportPresenter? = null
    var repository: DeliveryManRepository? = null
    var loadingDialogFragment: LoadingDialogFragment? = null
    private var data: DailyReport? = null
    private var today = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_report)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(R.string.daily_report)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp)
        initViews()
        data = DailyReport()
        data = intent.getParcelableExtra("report")
        if (data != null) {
            today = false
            inflateData()
        } else {
            today = true
        }
        if (!today) {
            tv_point_date!!.text = data!!.date.toString()
        } else {
            tv_point_date!!.setText(R.string.report_today)
        }

        /*
        {
           "various" : 100,
           "fuel_amount":2000,
           "communication_credit": 500,
           "reparation_amount":0,
           "balance_loss":1000,
           "parking_amount":200
        }
        * */bt_confirm_daily_report!!.setOnClickListener(this)
        bt_cancel!!.setOnClickListener(this)
        repository = DeliveryManRepository(this)
        presenter = DailyReportPresenter(this, repository)
    }

    private fun inflateData() {
        ed_fuel!!.setText(data!!.fuel_amount.toString())
        ed_credit!!.setText(data!!.communication_credit.toString())
        ed_reparation!!.setText(data!!.reparation_amount.toString())
        ed_losses!!.setText(data!!.balance_loss.toString())
        ed_parking!!.setText(data!!.parking_amount.toString())
        ed_various!!.setText(data!!.various.toString())
    }

    private fun initViews() {
        ed_fuel = findViewById(R.id.ed_fuel_amount)
        ed_credit = findViewById(R.id.ed_communication_credit)
        ed_reparation = findViewById(R.id.ed_reparation_amount)
        ed_losses = findViewById(R.id.ed_losses_amount)
        ed_parking = findViewById(R.id.ed_parking_amount)
        ed_various = findViewById(R.id.ed_various_fees)
        tv_point_date = findViewById(R.id.tv_point_date)
        bt_confirm_daily_report = findViewById(R.id.bt_confirm_daily_report)
        bt_cancel = findViewById(R.id.bt_cancel)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_confirm_daily_report -> checkAndUploadDailyReport()
            R.id.bt_cancel -> finish()
        }
    }

    private fun checkAndUploadDailyReport() {
        val fuel: Int
        val credit: Int
        val reparation: Int
        val losses: Int
        val parking: Int
        val various: Int
        fuel =
            Integer.valueOf(if ("" == ed_fuel!!.text.toString()) "0" else ed_fuel!!.text.toString())
        credit =
            Integer.valueOf(if ("" == ed_credit!!.text.toString()) "0" else ed_credit!!.text.toString())
        reparation =
            Integer.valueOf(if ("" == ed_reparation!!.text.toString()) "0" else ed_reparation!!.text.toString())
        losses =
            Integer.valueOf(if ("" == ed_losses!!.text.toString()) "0" else ed_losses!!.text.toString())
        parking =
            Integer.valueOf(if ("" == ed_parking!!.text.toString()) "0" else ed_parking!!.text.toString())
        various =
            Integer.valueOf(if ("" == ed_various!!.text.toString()) "0" else ed_various!!.text.toString())

        /* show a dialog to confirm the data that is going to be uploaded*/
        val dialogFragment = DailyReportConfirmationDialogFragment.newInstance(
            fuel,
            credit,
            reparation,
            losses,
            parking,
            various
        )
        showFragment(dialogFragment, "DailyReportConfirmationDialogFragment", true)
    }

    override fun networkError() {}
    override fun sysError() {}
    override fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (loadingDialogFragment == null) {
                if (isLoading) {
                    loadingDialogFragment =
                        LoadingDialogFragment.newInstance(getString(R.string.content_on_loading))
                    showFragment(loadingDialogFragment, "loadingbox", true)
                }
            } else {
                if (isLoading) {
                    showFragment(loadingDialogFragment, "loadingbox", false)
                } else {
                    hideFragment()
                }
            }
        }
    }

    private fun hideFragment() {
        if (loadingDialogFragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.remove(loadingDialogFragment!!)
            loadingDialogFragment = null
            ft.commitAllowingStateLoss()
        }
    }

    private fun showFragment(dialogFragment: DialogFragment?, tag: String, justCreated: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        if (justCreated == true) ft.add(dialogFragment!!, tag) else ft.show(dialogFragment!!)
        ft.commitAllowingStateLoss()
    }

    override fun dailyReportSuccess(isSuccessful: Boolean) {
        // jump to history view
        if (isSuccessful) {
            startActivity(Intent(this, DailyReportHistoryActivity::class.java))
            finish()
        } else {
            mToast(getString(R.string.report_error))
        }
    }

    private fun mToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setPresenter(presenter: DeliveryModeContract.Presenter) {}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun confirmReport(
        fuel: Int,
        credit: Int,
        reparation: Int,
        losses: Int,
        parking: Int,
        various: Int
    ) {
        /* do a push on the online server */
        presenter!!.uploadDailyReport(
            data!!.id,
            fuel,
            credit,
            reparation,
            losses,
            parking,
            various
        )
    }
}