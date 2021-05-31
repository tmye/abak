package tg.tmye.kaba_i_deliver.activity.restaurant;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tg.tmye.kaba_i_deliver.R;
import tg.tmye.kaba_i_deliver._commons.adapter.RestaurantListAdapter;
import tg.tmye.kaba_i_deliver._commons.decorator.LastItemListSpaceDecoration;
import tg.tmye.kaba_i_deliver.activity.restaurant.contract.RestaurantListContract;
import tg.tmye.kaba_i_deliver.activity.restaurant.presenter.RestaurantListPresenter;
import tg.tmye.kaba_i_deliver.cviews.CustomProgressbar;
import tg.tmye.kaba_i_deliver.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba_i_deliver.data.Restaurant.source.RestaurantRepository;
import tg.tmye.kaba_i_deliver.syscore.MyKabaDeliverApp;

public class RestaurantListActivity extends AppCompatActivity implements RestaurantListContract.View
{

    RestaurantListPresenter presenter;
    RestaurantRepository repository;
    private RestaurantListAdapter resListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        initViews();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Acquire a reference to the system Location Manager
            final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            // Or, use GPS location data:
            // String locationProvider = LocationManager.GPS_PROVIDER;
            LocationListener locationListener = new LocationListener() {

                public void onLocationChanged(Location location) {
                    /* update the 2fragment */
                    if (((MyKabaDeliverApp) getApplicationContext()).getLastLocation() == null) {
                        ((MyKabaDeliverApp) getApplicationContext()).setLastLocation(location);
                        presenter.getRestaurants();
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }

            };
            try {
                if (locationManager != null)
                    locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        repository = new RestaurantRepository(this);
        presenter = new RestaurantListPresenter(this, repository);

        presenter.getRestaurants();
    }

    TextView tv_messages;
    CustomProgressbar progressBar;
    Button bt_tryagain;
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
    public void inflateRestaurantList(final List<RestaurantEntity> restaurantEntities) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /* compute the deliveries of the day on the app */


                lny_error_box.setVisibility(View.GONE);

                if (restaurantEntities == null || restaurantEntities.size() == 0) {
                    recyclerview.setVisibility(View.GONE);
                    lny_error_box.setVisibility(View.VISIBLE);
//                    lny_error_box.setText(getResources().getString(R.string.no_restaurant_available));
                } else {
                    lny_error_box.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                    recyclerview.setLayoutManager(new LinearLayoutManager(RestaurantListActivity.this));
                    if (recyclerview.getItemDecorationCount() == 0)
                        recyclerview.addItemDecoration(new LastItemListSpaceDecoration(getResources().getDimensionPixelSize(R.dimen.menu_food_item_height)));
                    resListAdapter = new RestaurantListAdapter(RestaurantListActivity.this, restaurantEntities);
                    recyclerview.setAdapter(resListAdapter);
                }
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurant_page_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               _filterRestaurantList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               _filterRestaurantList(s);
                return false;
            }
        });

        return true;
    }

    private void _filterRestaurantList(String hint) {
        if (resListAdapter!=null)
            resListAdapter.getFilter().filter(hint.toString());
    }

    @Override
    public void setPresenter(RestaurantListContract.Presenter presenter) {

    }
}
