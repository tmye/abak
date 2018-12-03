package tg.tmye.kaba.activity.UserAcc.adresses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.cviews.dialog.ForceLogoutDialogFragment;
import tg.tmye.kaba._commons.cviews.dialog.LoadingDialogFragment;
import tg.tmye.kaba._commons.utils.UtilFunctions;
import tg.tmye.kaba.activity.UserAcc.adresses.contract.AdressesContract;
import tg.tmye.kaba.activity.UserAcc.adresses.presenter.AdressesPresenter;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.delivery.DeliveryAddress;
import tg.tmye.kaba.data.delivery.source.DeliveryAdresseRepo;
import tg.tmye.kaba.syscore.GlideApp;
import tg.tmye.kaba.syscore.MyKabaApp;


public class EditAddressActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, AdressesContract.View {

    private static final int MY_PERMISSION_LOCATION_PERMISSION = 9;
    private List<String> images;
    private EditAddressImagesAdapter adapter;

    public static final int PLACE_PICKER_REQUEST = 1;

    private RecyclerView rc_address_images;
    private TextView tv_select_address;
    private LinearLayoutCompat lny_pick_address;
    private SupportMapFragment mapFragment;

    private LatLng location = new LatLng(6.217675, 1.188539);
    private GoogleMap gMap;

    //    CardView map_cardview;
    Button bt_confirm;

    /* repository */
    DeliveryAdresseRepo repo;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Bundle savedInstanceState;
    private AdressesContract.Presenter presenter;

    private DeliveryAddress waitingForInflatingAddress = null;
    private int current_address_id = 0;
    private int WIDTH = 0;


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
            }, MY_PERMISSION_LOCATION_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_LOCATION_PERMISSION);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_LOCATION_PERMISSION) {
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_redprimary_upward_navigation_24dp);

        initVars();
        initViews();
        initRecyclerView();
        bt_confirm.setOnClickListener(this);
        tv_select_address.setOnClickListener(this);
        lny_pick_address.setOnClickListener(this);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        repo = new DeliveryAdresseRepo(this);
        presenter = new AdressesPresenter((AdressesContract.View) this, repo);

        /* check if there is some data sent from the precedent view.
         * - in the case, there is something,
         * */
        DeliveryAddress address = getIntent().getParcelableExtra("address");
        if (address != null) {
            presenter.presentAddress(address);
            getIntent().putExtra("address", 0);
        }
    }


    private void initVars() {
        images = new ArrayList<>();
        images.add("1");

        adapter = new EditAddressImagesAdapter(this, images);
    }

    private void initRecyclerView() {

        /* according to screen size, size is different */
//        int width = UtilFunctions.getScreenSize(this)[0];
//        int item_size = getResources().getDimensionPixelSize(R.dimen.edit_address_image_size);
//        int count = (width/item_size); count = count > 3 ? count : 3;

        rc_address_images.setLayoutManager(new GridLayoutManager(this, 3));
       /* rc_address_images.addItemDecoration(new GridRecyclerViewDecorator(
                getResources().getDimensionPixelSize(R.dimen.edit_address_image_spacing)
        ));*/
        rc_address_images.setAdapter(adapter);
    }

    private EditText ed_title_of_location, ed_phone_number;
    private EditText ed_address_details, ed_address_non_loin;
    private TextView tv_address_quartier;

    private void initViews() {

        ed_title_of_location = findViewById(R.id.ed_title_of_location);
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_address_details = findViewById(R.id.ed_address_details);
        ed_address_non_loin = findViewById(R.id.ed_address_non_loin);
        tv_address_quartier = findViewById(R.id.tv_address_quartier);

        rc_address_images = findViewById(R.id.rc_address_images);
        tv_select_address = findViewById(R.id.tv_select_address);
        bt_confirm = findViewById(R.id.bt_confirm);
        lny_pick_address = findViewById(R.id.lny_pick_address);
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

        int count = 5 - (adapter.getItemCount()-1);
        if (count > 0)
            FilePickerBuilder.getInstance().setMaxCount(count)
                    .setSelectedFiles(null)
                    .setActivityTheme(R.style.AppTheme)
//                .setCameraPlaceholder(R.drawable.ic_round_white_full_24dp)
                    .pickPhoto(this);
        else {
            mToast(getResources().getString(R.string.delete_before_adding_more));
        }
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
//                    String toastMsg = String.format("Address: %s ;; Place: %s", place.getAddress(), place.getName());
//                    toastMsg += String.format(" ;; Long: %s ;; Lat: %s", place.getLatLng().longitude, place.getLatLng().latitude);
                    // add viewport
//                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    location = place.getLatLng();
                    presenter.inflateLocation(location);
                }
                break;
        }
    }


    private void addGPSLocation() {

        /* empty everything we have here */
        location = null;
//        ed_address_details.setText("");
        tv_address_quartier.setText("");

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(new LatLngBounds(new LatLng(6.1135, 1.1121), new LatLng(6.2510,1.3812)));
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_void);
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

        if (waitingForInflatingAddress != null) {

            /* inflate images into the recyclerview */
            adapter.setData(waitingForInflatingAddress.picture);

            /* inflate content into the other views */
            ed_address_details.setText(waitingForInflatingAddress.description);
            ed_title_of_location.setText(waitingForInflatingAddress.name);
            ed_phone_number.setText(waitingForInflatingAddress.phone_number);

            /* inflate the map */
            String[] lat_long = waitingForInflatingAddress.location.split(":");
            if (lat_long != null && lat_long.length == 2) {
                LatLng coordinate = new LatLng(Double.valueOf(lat_long[0]), Double.valueOf(lat_long[1]));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18.0f);
                gMap.animateCamera(yourLocation);
            } else {
                mSnack(getResources().getString(R.string.map_coordonates_invalid));
            }
            waitingForInflatingAddress = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        showLoading(false);
        Glide.with(this).pauseRequestsRecursive();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequestsRecursive();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_confirm:
                upCreateNewAdress();
                break;
            case R.id.tv_select_address:
                addGPSLocation();
                break;
            case R.id.lny_pick_address:
                addGPSLocation();
                break;
        }
    }

    LoadingDialogFragment loadingDialogFragment;

    private void upCreateNewAdress() {

        /* check the entries */
        String[] pictures = {};

        if (!checkEmptyness(ed_title_of_location)) {
            mSnack(getResources().getString(R.string.title_this_field_shouldnt_empty));
            return;
        }
        if (!checkEmptyness(ed_phone_number)) {
            mSnack(getResources().getString(R.string.phone_number_this_field_shouldnt_empty));
            return;
        }

        /* check whether phone number is valid */
        if (!UtilFunctions.isPhoneNumber_TGO(ed_phone_number.getText().toString())) {
            mSnack(getString(R.string.enter_togolese_no));
            ed_phone_number.requestFocus();
            return;
        }

        if (!checkEmptyness(ed_address_non_loin)) {
            mSnack(getResources().getString(R.string.non_loin_this_field_shouldnt_empty));
            return;
        }

        if (!checkEmptyness(ed_address_details)) {
            mSnack(getResources().getString(R.string.address_this_field_shouldnt_empty));
            return;
        }
        if (!checkEmptyness(tv_address_quartier)) {
            mSnack(getResources().getString(R.string.quartier_this_field_shouldnt_empty));
            return;
        }

        if (location == null) {
            mSnack(getResources().getString(R.string.please_choose_location));
            return;
        }


        String[] tmp = null;
        List<String> data = adapter.getData();
        if (data != null && data.size() > 1) {
            /* change images list to image array */
            tmp = new String[data.size()-1];
            for (int i = 1; i < data.size(); i++) {
                tmp[i-1] = data.get(i);
            }
        }

        DeliveryAddress adress = new DeliveryAddress();
        adress.id = current_address_id;
        adress.picture = tmp;
        adress.quartier = tv_address_quartier.getText().toString();
        adress.near = ed_address_non_loin.getText().toString();
        adress.description = ed_address_details.getText().toString();
        adress.name = ed_title_of_location.getText().toString();
        adress.phone_number = ed_phone_number.getText().toString();
        adress.location = location.latitude+":"+location.longitude;

        presenter.uploadNewAdressToServer (adress);
    }

    private boolean checkEmptyness(EditText ed) {

        if (ed != null) {
            String content = ed.getText().toString();
            if ("".equals(content)) {
                /* we should not */
                ed.requestFocus();
                return false;
            }
        }
        return true;
    }
    private boolean checkEmptyness(TextView tv) {

        if (tv != null) {
            String content = tv.getText().toString();
            if ("".equals(content)) {
                /* we should not */
                mSnack(getResources().getString(R.string.this_field_shouldnt_empty));
                tv.requestFocus();
                return false;
            }
        }
        return true;
    }

    public void mSnack (String message) {
        Snackbar.make(rc_address_images, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void inflateAdresses(DeliveryAddress address) {

        this.waitingForInflatingAddress = address;
        this.current_address_id = address.id;

        ed_phone_number.setText(address.phone_number);
        ed_title_of_location.setText(address.name);
        tv_address_quartier.setText(address.quartier);
        ed_address_details.setText(address.description);
        ed_address_non_loin.setText(address.near);

        /* for the pictures ?? */
        adapter.setData(address.picture);
        adapter.notifyDataSetChanged();
        showLoading(false);
    }

    @Override
    public void inflateAdresses(List<DeliveryAddress> deliveryAddressList) {
    }


    @Override
    public void showLoading(final boolean isLoading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialogFragment == null) {
                    if (isLoading) {
                        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.content_on_loading));
                        showFragment(loadingDialogFragment, "loadingbox", true);
                    }
                } else {
                    if (isLoading) {
                        showFragment(loadingDialogFragment, "loadingbox",false);
                    } else {
                        hideFragment();
                    }
                }
            }
        });
    }

    private void hideFragment() {
        if (loadingDialogFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(loadingDialogFragment);
            loadingDialogFragment = null;
            ft.commitAllowingStateLoss();
        }
    }

    private void showFragment(LoadingDialogFragment loadingDialogFragment, String tag, boolean justCreated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (justCreated == true)
            ft.add(loadingDialogFragment, tag);
        else
            ft.show(loadingDialogFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void addressCreationSuccess() {
        mToast(getResources().getString(R.string.adress_creation_success));
        finish();
    }

    private void mToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addressCreationFailure() {
        mToast(getResources().getString(R.string.adress_creation_failed));
    }

    @Override
    public void addressDeletedFailure() {}

    @Override
    public void addressDeletedSuccess() {}

    @Override
    public void onAddressInteraction(DeliveryAddress address) {

    }

    @Override
    public void showCurrentAddressDetails(final String quartier, final String description_details) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv_address_quartier.setText(quartier);
                ed_address_details.setText(description_details);
            }
        });
    }

    @Override
    public void onNetworkError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast(getResources().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void onSysError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast(getResources().getString(R.string.sys_error));
            }
        });
    }

    @Override
    public void setPresenter(AdressesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void exitActivity(View view) {
        finish();
    }

    @Override
    public void onLoggingTimeout() {
        ForceLogoutDialogFragment forceLogoutDialogFragment = ForceLogoutDialogFragment.newInstance();
        forceLogoutDialogFragment.show(getSupportFragmentManager(), ForceLogoutDialogFragment.TAG);
    }


    private class EditAddressImagesAdapter extends RecyclerView.Adapter<EditAddressImagesAdapter.ViewHolder> {

        private List<String> data;

        public EditAddressImagesAdapter (Context ctx, List<String> data) {
            this.data = data;
            WIDTH = UtilFunctions.getScreenSize(ctx)[0]/3;
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

//            int width = UtilFunctions.getScreenSize(this)[0];
//            frame_container
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = WIDTH;
            holder.itemView.setLayoutParams(params);

            if (item.equals("1")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectPhoto();
                    }
                });
                return;
            }

            /* if local, show it,
             * if link, add server address */

            if (holder.imageView != null) {

                if (!UtilFunctions.checkFileExistsLocally(item)) {
                    item = Constant.SERVER_ADDRESS+"/"+item;
                }
                GlideApp.with(EditAddressActivity.this)
                        .load(item)
                        .placeholder(R.drawable.white_placeholder)
                        .transition(GenericTransitionOptions.with(  ((MyKabaApp)getApplicationContext()).getGlideAnimation()  ))
                        .centerCrop()
                        .into(holder.imageView);
            }

            /* delete or preview a picture according to the type */
            if (holder.iv_delete != null)
                holder.iv_delete.setOnClickListener(new DeletePictureOnClickListener(position));

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<String> newData) {

            if (data == null || data.size() == 0) {
                data = new ArrayList<>();
                data.add("1");
            }
            for (int i = 0; i < newData.size(); i++) {
                data.add(newData.get(i));
            }
        }

        public List<String> getData() {
            return data;
        }

        public void setData(String[] picture) {
            List<String> pict = new ArrayList<>();
            for (int i = 0; i < picture.length; i++) {
                pict.add(picture[i]);
            }
            setData(pict);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            View itemView;
            ImageView iv_delete;
            ImageView iv_preview;
            FrameLayout frame_container;

            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView = itemView.findViewById(R.id.iv);
                this.itemView = itemView;
                this.iv_preview = itemView.findViewById(R.id.iv_preview);
                this.iv_delete = itemView.findViewById(R.id.iv_remove);
                this.frame_container = itemView.findViewById(R.id.frame_container);
            }
        }

        private class DeletePictureOnClickListener implements View.OnClickListener {
            private final int position;

            public DeletePictureOnClickListener(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyDataSetChanged();
            }
        }
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

}
