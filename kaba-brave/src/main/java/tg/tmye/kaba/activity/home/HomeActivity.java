package tg.tmye.kaba.activity.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import tg.tmye.kaba.R;
import tg.tmye.kaba._commons.intf.YesOrNo;
import tg.tmye.kaba.activity.UserAcc.SoldeActivity;
import tg.tmye.kaba.activity.UserAuth.LoginActivity;
import tg.tmye.kaba.activity.cart.ShoppingCartActivity;
import tg.tmye.kaba.activity.home.presenter.F_Commands_3_Presenter;
import tg.tmye.kaba.activity.home.presenter.F_Home_1_Presenter;
import tg.tmye.kaba.activity.home.presenter.F_Restaurant_2_Presenter;
import tg.tmye.kaba.activity.home.presenter.F_UserAccount_4_Presenter;
import tg.tmye.kaba.activity.home.views.fragment.F_Commands_3_Fragment;
import tg.tmye.kaba.activity.home.views.fragment.F_Home_1_Fragment;
import tg.tmye.kaba.activity.home.views.fragment.F_MyAccount_4_Fragment;
import tg.tmye.kaba.activity.home.views.fragment.F_Restaurant_2_Fragment;
import tg.tmye.kaba.activity.menu.RestaurantMenuActivity;
import tg.tmye.kaba.activity.scanner.ScannerActivity;
import tg.tmye.kaba.activity.search.SearchActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Restaurant.RestaurantEntity;
import tg.tmye.kaba.data.Restaurant.source.RestaurantDbRepository;
import tg.tmye.kaba.data._OtherEntities.LightRestaurant;
import tg.tmye.kaba.data._OtherEntities.SimplePicture;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.advert.source.AdvertRepository;
import tg.tmye.kaba.data.command.source.CommandRepository;
import tg.tmye.kaba.data.customer.source.CustomerDataRepository;
import tg.tmye.kaba.syscore.MyKabaApp;


public class HomeActivity extends AppCompatActivity implements
        F_Commands_3_Fragment.OnFragmentInteractionListener,
        F_Home_1_Fragment.OnFragmentInteractionListener,
        F_MyAccount_4_Fragment.OnFragmentInteractionListener,
        F_Restaurant_2_Fragment.OnFragmentInteractionListener, View.OnClickListener {

    /* constants*/
    private static final int HOME = 0;
    private static final int RESTAURANT = 1;
    private static final int COMMAND_LIST = 2;
    private static final int MY_ACCOUNT = 3; // ids of the bottomTabs
    private static final long DELAY = 1000; // delay of the loading
    private static final int MY_PERMISSION_REQUEST_STORAGE = 0;


    private boolean isLoggedIn = false;

    /* presenters */
    F_Home_1_Presenter home_1_presenter;
    F_Restaurant_2_Presenter restaurant_2_presenter;
    F_Commands_3_Presenter command_3_presenter;
    F_UserAccount_4_Presenter userAccount_4_presenter;


    /* models */
    RestaurantDbRepository restaurantDbRepository;
    CommandRepository commandRepository;
    AdvertRepository advertRepository;
    private CustomerDataRepository customerDataRepository;


    /* fragments */
    F_Home_1_Fragment frg_1_home;
    F_Restaurant_2_Fragment frg_2_restaurants;
    F_Commands_3_Fragment frg_3_command_list;
    F_MyAccount_4_Fragment frg_4_myaccount; // try to find a strong reference towards these fragmntss


    /* variables */
    private int previousFragmentCode;
    private String daily_query;


    /* views */
    private FloatingActionButton cartFab;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(HOME);
                    return true;
                case R.id.navigation_restaurant:
                    switchFragment(RESTAURANT);
                    return true;
                case R.id.navigation_command_list:
                    switchFragment(COMMAND_LIST);
                    return isLoggedIn;
                case R.id.navigation_my_account:
                    switchFragment(MY_ACCOUNT);
                    return isLoggedIn;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initViews();

        initRepos();

        initFragments();

        initPresenters();

        switchFragment(HOME);

          /* ask for different permissions
         * - accessing gps : little location
         * - accessing networkstate
         * - accessing external_storage
         * */
        cartFab.setOnClickListener(this);

    }

    private void initViews() {
        cartFab = findViewById(R.id.fab_chart);
    }

    private void initFragments() {
        frg_1_home = F_Home_1_Fragment.newInstance();
        frg_2_restaurants = F_Restaurant_2_Fragment.newInstance();
        frg_3_command_list = F_Commands_3_Fragment.newInstance();
        frg_4_myaccount = F_MyAccount_4_Fragment.newInstance();
    }

    private void initPresenters() {
        home_1_presenter = new F_Home_1_Presenter(restaurantDbRepository, advertRepository, frg_1_home);
        restaurant_2_presenter = new F_Restaurant_2_Presenter(restaurantDbRepository, frg_2_restaurants);
        command_3_presenter = new F_Commands_3_Presenter(commandRepository, frg_3_command_list);
        userAccount_4_presenter = new F_UserAccount_4_Presenter(customerDataRepository, frg_4_myaccount);
    }

    private void initRepos() {

        restaurantDbRepository = new RestaurantDbRepository(this);
        commandRepository = new CommandRepository(this);
        advertRepository = new AdvertRepository(this);
        customerDataRepository = new CustomerDataRepository(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRestaurantInteraction(RestaurantEntity resto) {
        Intent intent = new Intent(this, RestaurantMenuActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT, resto);
        startActivity(intent);
    }

    @Override
    public void onRestaurantInteraction(LightRestaurant restaurantEntity) {
        Intent intent = new Intent(this, RestaurantMenuActivity.class);
        intent.putExtra(RestaurantMenuActivity.RESTAURANT, restaurantEntity);
        startActivity(intent);
    }

    @Override
    public void onAdsInteraction(AdsBanner ad) {
        Log.d(Constant.APP_TAG, ad.toString());
    }

    @Override
    public void onShowPic(SimplePicture.KabaShowPic pic) {
        Log.d(Constant.APP_TAG, pic.toString());
    }

    private void mSnack(String s) {
//        Snackbar.make(getSupportActionBar().getCustomView(), s, Snackbar.LENGTH_SHORT).show();
    }

    private void switchFragment(final int frgId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            requestStoragePermission();
            return;
        }

        switch (frgId) {
            case HOME:
                if (frg_1_home == null) {
                    frg_1_home = F_Home_1_Fragment.newInstance();
                    frg_1_home.setPresenter(home_1_presenter);
                }
                performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_home),  frg_1_home, HOME);
                previousFragmentCode = frgId;
                break;
            case RESTAURANT:
                if (frg_2_restaurants == null) {
                    frg_2_restaurants = F_Restaurant_2_Fragment.newInstance();
                    frg_2_restaurants.setPresenter(restaurant_2_presenter);
                }
                performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_restaurant),  frg_2_restaurants, RESTAURANT);
                previousFragmentCode = frgId;
                break;
            case COMMAND_LIST:
                checkLogin(previousFragmentCode, COMMAND_LIST, new YesOrNo(){
                    @Override
                    public void yes() {
                        if (frg_3_command_list == null){
                            frg_3_command_list = F_Commands_3_Fragment.newInstance();
                            frg_3_command_list.setPresenter(command_3_presenter);
                        }
                        performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_command_list),  frg_3_command_list, COMMAND_LIST);
                        previousFragmentCode = frgId;
                    }
                    @Override
                    public void no() {
                        // go back to the previous
                        switchFragment(getSwitchedOne(previousFragmentCode));
                    }
                });
                break;
            case MY_ACCOUNT:
                checkLogin(previousFragmentCode, MY_ACCOUNT, new YesOrNo() {
                    @Override
                    public void yes() {
                        if (frg_4_myaccount == null) {
                            frg_4_myaccount = F_MyAccount_4_Fragment.newInstance();
                            frg_4_myaccount.setPresenter(userAccount_4_presenter);
                        }
                        performNoBackStackTransaction(getSupportFragmentManager(), getString(R.string.title_myaccount),  frg_4_myaccount, MY_ACCOUNT);
                        previousFragmentCode = frgId;
                        // switch bottom button to the fourth
                    }

                    @Override
                    public void no() {
                        // go back to the previous
                        switchFragment(getSwitchedOne(previousFragmentCode));
                    }
                });
                break;
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION_REQUEST_STORAGE);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION_REQUEST_STORAGE);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_STORAGE) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switchFragment(HOME);
        } else {
            Toast.makeText(this, getResources().getString(R.string.storage_permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    private int getSwitchedOne (int frgId) {
        switch (frgId) {
            case HOME:
                return R.id.navigation_home;
            case RESTAURANT:
                return R.id.navigation_restaurant;
            case COMMAND_LIST:
                return R.id.navigation_command_list;
            case MY_ACCOUNT:
                return R.id.navigation_my_account;
        }
        return R.id.navigation_home;
    }


    private void checkLogin(int previousFragCode, int fragmentDestination, YesOrNo yesOrNo) {

        String token = ((MyKabaApp)getApplication()).getAuthToken().trim();
        // if try to do a transaction and it fails, then you know there is someth wrong
        if (token == null || token.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            in.putExtra(Config.HOME_SWITCH_FRAG_DESTINATION, fragmentDestination);
            in.putExtra(Config.HOME_SWITCH_FRAG_PREVIOUS, previousFragCode);
            startActivityForResult(in, Config.LOGIN_SUCCESS);
        } else {
            isLoggedIn = true;
            yesOrNo.yes();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Config.LOGIN_SUCCESS:
                // if login success, confirm the switch
                if (data != null) {
                    int dest = data.getIntExtra(Config.HOME_SWITCH_FRAG_DESTINATION, -1);
                    if (dest != -1){
                        isLoggedIn = true;
                        switchFragment(dest);
                    }
                }
                break;
            case Config.LOGIN_FAILURE:
                if (data != null) {
                    int prev = data.getIntExtra(Config.HOME_SWITCH_FRAG_PREVIOUS, -1);
                    if (prev != -1)
                        switchFragment(prev);
                } else
                    switchFragment(HOME);
                mToast("LOGIN FAILURE");
                break;
        }
    }

    public void performNoBackStackTransaction(final FragmentManager fragmentManager,
                                              String tag,
                                              Fragment fragment,
                                              int fragmentIndex) {

        final int newBackStackLength = fragmentManager.getBackStackEntryCount() +1;

        int anim_In = 0, anim_Out = 0;

        if (previousFragmentCode == -1) {
            anim_In = android.R.anim.fade_in;
            anim_Out = android.R.anim.fade_out;
        } else {
            if (previousFragmentCode < fragmentIndex) {
                anim_In = R.anim.enter_from_right;
                anim_Out = R.anim.exit_to_left;
            } else {
                anim_In = R.anim.enter_from_left;
                anim_Out = R.anim.exit_to_right;
            }
        }

        anim_Out = R.anim.exit_to_void;

        fragmentManager.beginTransaction()
                .setCustomAnimations(anim_In, anim_Out)
                .replace(R.id.frame_main_layout_content, fragment, tag)
                .addToBackStack(tag)
                .commit();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int nowCount = fragmentManager.getBackStackEntryCount();
                if (newBackStackLength != nowCount) {
                    // we don't really care if going back or forward. we already performed the logic here.
                    fragmentManager.removeOnBackStackChangedListener(this);

                    if ( newBackStackLength > nowCount ) { // user pressed back
                        fragmentManager.popBackStackImmediate();
                    }
                }
            }
        });

        // update theme
        if (previousFragmentCode == 3 && fragmentIndex < 3) {
            revealFabCart(true);
            return;
        }
        if (previousFragmentCode < 3 && fragmentIndex == 3) {
            revealFabCart(false);
        }
    }

    private void revealFabCart(boolean isFabCartVisible) {

        if (isFabCartVisible) {
            if (cartFab != null)
                cartFab.setVisibility(View.VISIBLE);
        } else {
            if (cartFab != null)
                cartFab.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            mToast("action_settings home");
            return true;
        } else if (id == R.id.action_mymessages) {
            mToast("action_mymessages home");
            return true;
        } else if (id == android.R.id.home) {
            mToast("home home");
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        }
        return false;
    }

    private void mToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    FrameLayout frm;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_chart:
                openShopCart();
                break;
            case R.id.ib_search_bt:
                openSearch_hasDailyQuery(false);
                break;
            case R.id.tv_search_query:
                openSearch_hasDailyQuery(true);
                break;
            case R.id.lny_account_balance:
                showHideBalance();
                break;
            case R.id.lny_account_more:
                openSoldeActivity();
                break;
            case R.id.lny_account_topup:
                openSoldeTopUpActivity();
                break;
        }
    }

    private void showHideBalance() {
    }

    private void openSoldeActivity() {

        Intent intent = new Intent(this, SoldeActivity.class);
        startActivity(intent);
    }

    private void openSoldeTopUpActivity() {

    }

    private void openSearch_hasDailyQuery(boolean hasDailyQuery) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }


    private void openShopCart() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}
