package tg.tmye.kaba_i_deliver.activity.restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver.activity.restaurant.contract.RestaurantListContract;
import tg.tmye.kaba_i_deliver.activity.restaurant.presenter.RestaurantListPresenter;
import tg.tmye.kaba_i_deliver.cviews.CustomProgressbar;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.Restaurant.source.RestaurantRepository;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListContract.View
{

    RestaurantListPresenter presenter;
    RestaurantRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        initViews();

        repository = new RestaurantRepository(this);
        presenter = new RestaurantListPresenter(this, repository);

        presenter.getRestaurants();
    }

    TextView tv_messages;
    CustomProgressbar progressBar;
    Button bt_tryagain;
    TextView tv_menu_count;
    RecyclerView recyclerview;


    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        tv_messages = findViewById(R.id.tv_messages);
        bt_tryagain = findViewById(R.id.bt_tryagain);
        lny_content = findViewById(R.id.lny_content);
        lny_error_box = findViewById(R.id.lny_error_box);
        lny_loading_frame = findViewById(R.id.lny_loading);
        recyclerview = findViewById(R.id.recyclerview);
    }

    @Override
    public void inflateRestaurantList(List<RestaurantEntity> restaurants) {


    }

    LinearLayout lny_error_box, lny_content, lny_loading_frame;

    @Override
    public void showLoading(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                lny_content.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                lny_error_box.setVisibility(View.GONE);
//                tv_no_food_message.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void sysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void networkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lny_loading_frame.setVisibility(View.GONE);
                lny_content.setVisibility(View.GONE);
                lny_error_box.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void setPresenter(RestaurantListContract.Presenter presenter) {

    }
}
