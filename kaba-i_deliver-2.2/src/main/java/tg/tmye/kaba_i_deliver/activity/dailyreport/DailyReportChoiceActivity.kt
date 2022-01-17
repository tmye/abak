package tg.tmye.kaba_i_deliver.activity.dailyreport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.cardview.widget.CardView
import tg.tmye.kaba_i_deliver.R

class DailyReportChoiceActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var card_edit : CardView;
    lateinit var card_create : CardView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_report_choice)
        setTitle(R.string.point_action_choice)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp)
        initViews();

        card_create.setOnClickListener(this)
        card_edit.setOnClickListener(this)
    }

    private fun initViews() {
        card_create = findViewById(R.id.cv_create);
        card_edit = findViewById(R.id.cv_edit);
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.cv_create -> {
                val intent = Intent(this@DailyReportChoiceActivity, DailyReportActivity::class.java)
                startActivity(intent)
            }
            R.id.cv_edit -> {
                val intent = Intent(this@DailyReportChoiceActivity, DailyReportHistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}