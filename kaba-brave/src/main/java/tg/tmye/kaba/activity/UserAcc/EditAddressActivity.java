package tg.tmye.kaba.activity.UserAcc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.decorator.GridRecyclerViewDecorator;
import tg.tmye.kaba.activity.home.HomeActivity;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.syscore.GlideApp;


public class EditAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSION_LOCATION_STORAGE = 9;
    private List<String> images;
    private EditAddressImagesAdapter adapter;

    public static final int PLACE_PICKER_REQUEST = 1;

    private RecyclerView rc_address_images;
    private View bt_add_gps_address;
    private SupportMapFragment mapFragment;
    private MapView mapView;

    private LatLng location;
    private GoogleMap gMap;

    CardView map_cardview;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_DENIED
                ) {
            requestLocationPermission();
            return;
        }

        doInitActivity();
    }

    private void requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
          ActivityCompat.requestPermissions(EditAddressActivity.this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_LOCATION_STORAGE);
        } else {
       ActivityCompat.requestPermissions(this, new String[] {
               Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_LOCATION_STORAGE);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_LOCATION_STORAGE) {
            return;
        }

        if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            doInitActivity();
        } else {
            Toast.makeText(this, getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void doInitActivity() {
        setContentView(R.layout.activity_edit_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_yellow_upward_navigation_24dp);

        initVars();
        initViews();
        initRecyclerView();
        bt_add_gps_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGPSLocation();
            }
        });
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        /*mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frg_map);*/
        mapView = findViewById(R.id.swagishmap);
        mapView.onCreate(mapViewBundle);
    }


    private void initVars() {
        images = new ArrayList<>();
        images.add("1");

        adapter = new EditAddressImagesAdapter(this, images);
    }

    private void initRecyclerView() {

        rc_address_images.setLayoutManager(new GridLayoutManager(this, 3));
        rc_address_images.addItemDecoration(new GridRecyclerViewDecorator(
                getResources().getDimensionPixelSize(R.dimen.edit_address_image_spacing)
        ));
        rc_address_images.setAdapter(adapter);
    }

    private void initViews() {

        rc_address_images = findViewById(R.id.rc_address_images);
        bt_add_gps_address = findViewById(R.id.bt_add_gps_address);
        map_cardview = findViewById(R.id.map_cardview);
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


    public void selectPhoto() {

        FilePickerBuilder.getInstance().setMaxCount(5)
                .setSelectedFiles(null)
                .setActivityTheme(R.style.AppTheme)
//                .setCameraPlaceholder(R.drawable.ic_round_white_full_24dp)
                .pickPhoto(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    images = new ArrayList<>();
                    images.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    adapter.setData(images);
                    adapter.notifyDataSetChanged();
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    String toastMsg = String.format("Address: %s ;; Place: %s", place.getAddress(), place.getName());
                    toastMsg += String.format(" ;; Long: %s ;; Lat: %s", place.getLatLng().longitude, place.getLatLng().latitude);
                    // add viewport
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    location = place.getLatLng();
                    mapView.getMapAsync(this);
                    map_cardview.setVisibility(View.VISIBLE);
                    // Get the SupportMapFragment and request notification
                    // when the map is ready to be used.
                }
                break;
        }
    }


    private void addGPSLocation() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * - upload it to the server
     * - send back an update action to the address page
     *
     **/
    public void uploadAdressDataToServer(String pathImage,
                                         String title,
                                         String description,
                                         String LatLng) {

        // send the upload on network thread and close once the upload is successful
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        gMap = googleMap;
        gMap.addMarker(new MarkerOptions().position(location)
                .title(getResources().getString(R.string.this_is_location)));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        gMap.animateCamera( CameraUpdateFactory.zoomTo( 18.0f ) );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null)
            mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null)
            mapView.onStop();
    }

    @Override
    protected void onPause() {
        if (mapView != null)
            mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mapView != null)
            mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    private class EditAddressImagesAdapter extends RecyclerView.Adapter<EditAddressImagesAdapter.ViewHolder> {

        private List<String> data;

        public EditAddressImagesAdapter (Context ctx, List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (data.get(viewType).equals("1")) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_address_plus_layout, parent, false));
            }
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_address_plus_remove_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            String item = data.get(position);

            if (item.equals("1")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectPhoto();
                    }
                });
                return;
            }
            if (holder.imageView != null)
                GlideApp.with(EditAddressActivity.this)
                        .load(item)
                        .placeholder(R.drawable.placeholder_kaba)
                        .centerCrop()
                        .into(holder.imageView);

            Log.d(Constant.APP_TAG, item);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<String> newData) {

            data = new ArrayList<>();
            data.add("1");
            for (int i = 0; i < newData.size(); i++) {
                data.add(newData.get(i));
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView = itemView.findViewById(R.id.iv);
                this.itemView = itemView;
            }
        }
    }
}
