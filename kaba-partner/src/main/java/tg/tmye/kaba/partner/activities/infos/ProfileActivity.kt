package tg.tmye.kaba.partner.activities.infos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import droidninja.filepicker.FilePickerBuilder.Companion.instance
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils.getFilePath
import tg.tmye.kaba.partner.R
import tg.tmye.kaba.partner._commons.utils.UtilFunctions
import tg.tmye.kaba.partner.activities.infos.contract.ProfileContract
import tg.tmye.kaba.partner.activities.infos.presenter.ProfilePresenter
import tg.tmye.kaba.partner.cviews.dialog.LoadingDialogFragment
import tg.tmye.kaba.partner.data.Restaurant.RestaurantEntity
import tg.tmye.kaba.partner.data.Restaurant.source.RestaurantOnlineRepository
import tg.tmye.kaba.partner.syscore.Constant
import tg.tmye.kaba.partner.syscore.MyRestaurantApp

class ProfileActivity : AppCompatActivity(), View.OnClickListener, ProfileContract.View {


      var restaurantLocation: String = "6.1:1.6"
    final val PROFILE_PICTURE_REQUEST: Int = 100;
    final val BACKGROUND_PICTURE_REQUEST: Int = 101;

    lateinit var iv_placeholder_1 : ImageView;
    lateinit var iv_placeholder : ImageView;
    lateinit var bt_pick_1 : Button;
    lateinit var bt_pick : Button;
    lateinit var ed_restaurant_name : EditText;
    lateinit var ed_restaurant_description : EditText;
    lateinit var ed_restaurant_address : EditText;
    lateinit var tv_restaurant_geolocalisation: TextView
    lateinit var bt_edit : Button
    lateinit var bt_check : Button
    lateinit var bt_cancel : Button
    lateinit var bt_confirm : Button
    lateinit var bt_tryagain : Button

    lateinit var restaurant: RestaurantEntity

    lateinit var profilePicBase64 : String;
    lateinit var backgroundPicBase64 : String;

    lateinit var presenter : ProfilePresenter;
    lateinit var restaurantRepository : RestaurantOnlineRepository;

    lateinit var nested_form : NestedScrollView;
    lateinit var lny_error_box : LinearLayoutCompat;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setTitle(R.string.restaurant_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp)

        initViews()

        //
        bt_pick.setOnClickListener(this)
        bt_pick_1.setOnClickListener(this)
        //
        bt_edit.setOnClickListener(this)
        bt_check.setOnClickListener(this)
        //
        bt_cancel.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
        bt_tryagain.setOnClickListener(this)

        // load from presenter or from previous page

        restaurantRepository = RestaurantOnlineRepository(this)
        presenter = ProfilePresenter(this, restaurantRepository)

        presenter.loadPartnerProfileInfo()
    }

    private fun initViews() {

        lny_error_box = findViewById(R.id.lny_error_box)
        nested_form = findViewById(R.id.nested_form)

        iv_placeholder = findViewById(R.id.iv_placeholder)
        iv_placeholder_1 = findViewById(R.id.iv_placeholder_1)
        //
        bt_pick = findViewById(R.id.bt_pick)
        bt_pick_1 = findViewById(R.id.bt_pick_1)
        //
        ed_restaurant_name = findViewById(R.id.ed_restaurant_name)
        ed_restaurant_description = findViewById(R.id.ed_restaurant_description)
        ed_restaurant_address  = findViewById(R.id.ed_partner_address)
        tv_restaurant_geolocalisation = findViewById(R.id.tv_restaurant_geolocalisation);
        //
        bt_edit = findViewById(R.id.bt_edit)
        bt_check = findViewById(R.id.bt_check)

        bt_cancel = findViewById(R.id.bt_cancel)
        bt_confirm = findViewById(R.id.bt_confirm)

        bt_tryagain = findViewById(R.id.bt_tryagain)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_pick -> pickPicture(PROFILE_PICTURE_REQUEST)
            R.id.bt_pick_1 ->  pickPicture(BACKGROUND_PICTURE_REQUEST)
            R.id.bt_check -> openGpsIfLocationExists()
            R.id.bt_edit -> pickAndUpdateGpsLocation()
            R.id.bt_cancel -> finish()
            R.id.bt_confirm -> pushAndUpdate()
            R.id.bt_tryagain -> tryAgain()
        }
    }

    private fun tryAgain() {
        presenter.loadPartnerProfileInfo()
    }

    private fun pushAndUpdate() {

        restaurant.name = ed_restaurant_name.text.toString()
        restaurant.description = ed_restaurant_description.text.toString()
        restaurant.address = ed_restaurant_address.text.toString()
        restaurant.theme_pic = backgroundPicBase64
        restaurant.pic = profilePicBase64
        // new gps
//        restaurant.gps_location = restaurantLocation

        // all the data goes in  here
        presenter.updatePartnerProfile(restaurant)
    }

    private fun pickAndUpdateGpsLocation() {


    }

    private fun openGpsIfLocationExists() {

        /* if (restaurant.location == null || restaurant.location.length == 0) {
             mToast("Erreur")
             return
         }*/

        val splitted_location: Array<String> = restaurantLocation.split(":").toTypedArray()
        val latitude = splitted_location[0]
        val longitude = splitted_location[1]

        // Create a Uri from an intent string. Use the result to create an Intent.

        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri =
            Uri.parse("geo:0,0?q=" + latitude + "," + longitude/* + "(" + Uri.encode(restaurant.name) + ")"*/)
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        } else {
            mToast(getString(R.string.google_maps_not_installed))
        }
    }


    private fun pickPicture(requestCode: Int) {
        /* check if camera and storage rights */

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), requestCode
            )
            return
        }

        instance.setMaxCount(1) //                .setSelectedFiles(null)
            .setActivityTheme(R.style.AppTheme_NoActionBar)
            .setCameraPlaceholder(R.drawable.ic_photo_camera_white_24dp)
            .pickPhoto(this, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            when(requestCode){
                PROFILE_PICTURE_REQUEST ->    bt_pick.performClick()
                BACKGROUND_PICTURE_REQUEST -> bt_pick_1.performClick()
            }
        } else {
            mToast(resources.getString(R.string.failed_in_allowing_permission))
        }
    }
/*

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        */
/* on activity result *//*
Log.d(Constant.APP_TAG, "onactivity result")
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == RESULT_OK && data != null) {
                val images: MutableList<Uri> = ArrayList()
                //                    images.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                images.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
                */
/* set image *//*
try {
                    if (images != null && images.size > 0) {
                        Glide.with(this)
                            .load(getFilePath(this, images[0]))
                            .transition(GenericTransitionOptions.with((applicationContext as MyRestaurantApp).glideAnimation))
                            .centerCrop()
                            .into(if (requestCode == PROFILE_PICTURE_REQUEST) iv_placeholder else iv_placeholder_1)

                        if (requestCode == PROFILE_PICTURE_REQUEST)
                            profilePicBase64 = UtilFunctions.changeAndRotatePathToBase64(
                                getFilePath(
                                    this,
                                    images[0]
                                )
                            )
                        else
                            backgroundPicBase64 = UtilFunctions.changeAndRotatePathToBase64(
                                getFilePath(
                                    this,
                                    images[0]
                                )
                            )
                        */
/*this is base64, check if we have to return it or not *//*

//                        activityResultSuccess = true;
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mToast(getString(R.string.image_picking_error))
                }
            }
        }
    }
*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* on activity result */Log.d(Constant.APP_TAG, "onactivity result")
        try {
            if (resultCode == RESULT_OK && data != null) {
                val images: MutableList<Uri> = ArrayList()
                images.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
                Glide.with(this)
                    .load(getFilePath(this, images[0]))
                    .transition(GenericTransitionOptions.with((applicationContext as MyRestaurantApp).glideAnimation))
                    .centerCrop()
                    .into(if (requestCode == PROFILE_PICTURE_REQUEST) iv_placeholder else iv_placeholder_1)
                if (requestCode == PROFILE_PICTURE_REQUEST)
                    profilePicBase64 = UtilFunctions.changeAndRotatePathToBase64(
                        getFilePath(
                            this,
                            images[0]
                        )
                    )
                else
                    backgroundPicBase64 = UtilFunctions.changeAndRotatePathToBase64(
                        getFilePath(
                            this,
                            images[0]
                        )
                    )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mToast(getString(R.string.image_picking_error))
        }
    }


    private fun mToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showIsLoading(isLoading: Boolean) {
        showLoading(isLoading)
    }

    override fun inflateRestaurantProfileInfo(restaurant: RestaurantEntity) {
        runOnUiThread {

            lny_error_box.visibility = View.GONE
            nested_form.visibility = View.VISIBLE

            this.restaurant = restaurant;
            ed_restaurant_name.setText(restaurant.name)
            ed_restaurant_description.setText(restaurant.description)
            ed_restaurant_address.setText(restaurant.address)
            tv_restaurant_geolocalisation.setText(restaurant.location)
            if (restaurant.location != null)
                restaurantLocation = restaurant.location

//        top picture restaurant background

            Glide.with(this)
                .load(Constant.SERVER_ADDRESS + "/" + restaurant.theme_pic)
                .transition(GenericTransitionOptions.with<Drawable>((getApplicationContext() as MyRestaurantApp).glideAnimation))
                .centerCrop()
                .into(iv_placeholder_1)

            Glide.with(this)
                .load(Constant.SERVER_ADDRESS + "/" + restaurant.pic)
                .transition(GenericTransitionOptions.with<Drawable>((getApplicationContext() as MyRestaurantApp).glideAnimation))
                .centerCrop()
                .into(iv_placeholder)

        }
    }

    override fun sysError() {
        mToast("sysError");
        runOnUiThread {
            lny_error_box.visibility = View.VISIBLE
            nested_form.visibility = View.GONE
        }
    }

    override fun networkError() {
        mToast("networkError");
        runOnUiThread {
            lny_error_box.visibility = View.VISIBLE
            nested_form.visibility = View.GONE
        }
    }

    override fun setPresenter(presenter: ProfileContract.Presenter?) {
        TODO("Not yet implemented")
    }

    private var loadingDialogFragment: LoadingDialogFragment? = null

    fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (loadingDialogFragment == null) {
                if (isLoading) {
                    loadingDialogFragment =
                        LoadingDialogFragment.newInstance(getString(R.string.content_on_loading))
                    showFragment(loadingDialogFragment!!, "loadingbox", true)
                }
            } else {
                if (isLoading) {
                    showFragment(loadingDialogFragment!!, "loadingbox", false)
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

    private fun showFragment(
        loadingDialogFragment: LoadingDialogFragment,
        tag: String,
        justCreated: Boolean
    ) {
        val ft = supportFragmentManager.beginTransaction()
        if (justCreated == true) ft.add(loadingDialogFragment, tag) else ft.show(
            loadingDialogFragment
        )
        ft.commitAllowingStateLoss()
    }

}