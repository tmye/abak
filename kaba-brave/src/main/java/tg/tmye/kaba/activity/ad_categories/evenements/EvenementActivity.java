package tg.tmye.kaba.activity.ad_categories.evenements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tg.tmye.kaba.activity.ImagePreviewActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.syscore.baseobj.BasePresenter;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.adapters.EvenementAdapter;
import tg.tmye.kaba._commons.decorator.CommandListSpacesItemDecoration;
import tg.tmye.kaba.activity.ad_categories.evenements.contract.EvenementContract;
import tg.tmye.kaba.activity.ad_categories.evenements.presenter.EvenementPresenter;
import tg.tmye.kaba.data.evenement.Evenement;
import tg.tmye.kaba.data.evenement.EvenementRepository;

public class EvenementActivity extends AppCompatActivity implements EvenementContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    /* meilleures ventes sur kaba. */

    EvenementPresenter presenter;

    EvenementRepository repository;

    LinearLayoutCompat lny_error;

    TextView tv_message;
    Button bt_tryagain;
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.ItemDecoration decoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initViews();

        repository = new EvenementRepository(this);
        presenter = new EvenementPresenter(this, repository);
        presenter.loadEvenements();

        swipeRefreshLayout.setOnRefreshListener(this);
        bt_tryagain.setOnClickListener(this);
    }


    private void initViews() {
        lny_error = findViewById(R.id.lny_error);
        tv_message =findViewById(R.id.tv_message);
        bt_tryagain =findViewById(R.id.bt_tryagain);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
    }


    @Override
    public void inflateEvenements(final List<Evenement> evenements) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                lny_error.setVisibility(View.GONE);
                recyclerView.removeAllViews();

                /* depending if the array is empty */
                if (evenements == null || evenements.size() ==0 ){
                    setUpEmptyList();
                    return;
                }

                /* inflate best sellers list */
                EvenementAdapter adapter = new EvenementAdapter(EvenementActivity.this, evenements);
                recyclerView.setLayoutManager(new LinearLayoutManager(EvenementActivity.this));

                /* decoration */
                if (decoration == null) {
                    decoration = new CommandListSpacesItemDecoration(
                            getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_half),
                            getResources().getDimensionPixelSize(R.dimen.food_details_fab_margin_bottom)
                    );
                }

                if (recyclerView.getItemDecorationCount() == 0)
                    recyclerView.addItemDecoration(decoration);

                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpEmptyList() {
        recyclerView.setVisibility(View.GONE);
        bt_tryagain.setVisibility(View.GONE);
        lny_error.setVisibility(View.VISIBLE);
        tv_message.setText(getResources().getString(R.string.no_data));
    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_message.setText(getResources().getString(R.string.network_error));
                lny_error.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_message.setText(getResources().getString(R.string.sys_error));
                lny_error.setVisibility(View.VISIBLE);
                bt_tryagain.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_error.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(isLoading);
            }
        });
    }

    @Override
    public void onEventClicked(Evenement evenement) {

        Evenement[] evenements = new Evenement[]{evenement};

        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.EVENT_TAG, evenements);
        startActivity(intent);
    }

    /*  @Override
    public void onImageClicked(String pic) {
        mToast(pic);
        pic = Constant.SERVER_ADDRESS+"/"+pic;
        String[] imgs = new String[]{pic};
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.IMG_URL_TAG, imgs);
        startActivity(intent);
    }
    */

    private void mToast(String pic) {
        Toast.makeText(this, pic, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        presenter.loadEvenements();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tryagain:
                presenter.loadEvenements();
                break;
        }
    }
}
