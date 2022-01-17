package tg.tmye.kaba.partner.activities.infos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import droidninja.filepicker.FilePickerBuilder.Companion.instance
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils.getFilePath
import tg.tmye.kaba.partner.R
import tg.tmye.kaba.partner._commons.utils.UtilFunctions
import tg.tmye.kaba.partner.syscore.Constant
import tg.tmye.kaba.partner.syscore.MyRestaurantApp
import java.lang.Exception
import java.util.ArrayList

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    final val PROFILE_PICTURE_REQUEST: Int = 100;
    final val BACKGROUND_PICTURE_REQUEST: Int = 101;

    lateinit var iv_placeholder_1 : ImageView;
    lateinit var iv_placeholder : ImageView;
    lateinit var bt_pick_1 : Button;
    lateinit var bt_pick : Button;
    lateinit var ed_restaurant_name : EditText;
    lateinit var ed_restaurant_description : EditText;
    lateinit var tv_restaurant_geolocalisation: TextView
    lateinit var bt_edit : Button
    lateinit var bt_check : Button

    lateinit var profilePicBase64 : String;
    lateinit var backgroundPicBase64 : String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setTitle(R.string.restaurant_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_upward_navigation_24dp)

        initViews()

        bt_pick.setOnClickListener(this)
        bt_pick_1.setOnClickListener(this)
        // load from presenter or from previous page
    }

    private fun initViews() {
        iv_placeholder = findViewById(R.id.iv_placeholder)
        iv_placeholder_1 = findViewById(R.id.iv_placeholder_1)
        //
        bt_pick = findViewById(R.id.bt_pick)
        bt_pick_1 = findViewById(R.id.bt_pick_1)
        //
        ed_restaurant_name = findViewById(R.id.ed_restaurant_name)
        ed_restaurant_description = findViewById(R.id.ed_restaurant_description)
        tv_restaurant_geolocalisation = findViewById(R.id.tv_restaurant_geolocalisation);
        //
        bt_edit = findViewById(R.id.bt_edit)
        bt_check = findViewById(R.id.bt_check)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_pick -> pickPicture(PROFILE_PICTURE_REQUEST)
            R.id.bt_pick_1 ->  pickPicture(BACKGROUND_PICTURE_REQUEST)
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

}