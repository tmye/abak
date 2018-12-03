package tg.tmye.kaba.activity.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.SettingsAdapter;

public class SettingsActivity extends AppCompatActivity {

    RecyclerView rec_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        // R.string.contact_us
//        R.string.terms_and_conditions
//        R.string.app_info
//        R.string.faq

        /* activities to jump at */

        /* normal side icons */

//        R.drawable.ic_contact_us
//        R.drawable.ic_terms_and_condition
//        R.drawable.ic_faq
//        R.drawable.ic_app_info

        /**
         *
         * / Show a list of options:
         *
         * - help;
         * - about;
         * - contact us;
         * - terms and conditions;
         * - App info;
         * - FAQ;
         *
         **/

        initViews();

        SettingsAdapter settingsAdapter = new SettingsAdapter(this);

        rec_settings.setLayoutManager(new LinearLayoutManager(this));
        rec_settings.setAdapter(settingsAdapter);
    }

    private void initViews() {

        rec_settings = findViewById(R.id.rec_settings);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
