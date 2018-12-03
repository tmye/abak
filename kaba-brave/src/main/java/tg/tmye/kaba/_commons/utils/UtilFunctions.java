package tg.tmye.kaba._commons.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tg.tmye.kaba.R;
import tg.tmye.kaba.activity.trans.ConfirmCommandDetailsActivity;
import tg.tmye.kaba.config.Config;
import tg.tmye.kaba.config.Constant;
import tg.tmye.kaba.data.Food.Restaurant_Menu_FoodEntity;
import tg.tmye.kaba.data.advert.AdsBanner;
import tg.tmye.kaba.data.customer.Customer;
import tg.tmye.kaba.data.shoppingcart.BasketInItem;
import tg.tmye.kaba.data.transaction.Transaction;


/**
 * By abiguime on 2017/12/6.
 * email: 2597434002@qq.com
 */

public class UtilFunctions {

    private static final String STYLE_ASSET_FILE = "file:///android_asset/style/style1_0.css";

    public static int[] getScreenSize(Context context) {
        int[] screenSize = new int[2];
        screenSize[0] = context.getResources().getDisplayMetrics().widthPixels;
        screenSize[1] = context.getResources().getDisplayMetrics().heightPixels;
        return screenSize;
    }

//    public static int dp2Pixels(Context context, int lil_round_size) {
//    }


    public static boolean checkLink(String link) {
        return true;
    }

    public static AdsBanner[] convertToArray(List<AdsBanner> ads_list) {

        AdsBanner[] adsBanners = new AdsBanner[ads_list.size()];

        for (int i = 0; i < ads_list.size(); i++) {
            adsBanners[i] = ads_list.get(i);
        }
        return adsBanners;
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation(Context ctx) {
     /*   if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }*/
    }

    public static String randomToken() {
        String token = "";
        for (int i = 0; i < 33; i++) {
            token+= (""+(i%10));
        }
        return token;
    }

    public static String toJsonData(Object dataObject) {

        String jsonData = "";
        // use reflexion to get all variables
        if (dataObject instanceof Customer) {

            JSONObject object = new JSONObject();
            try {
                object.put("id", ""+((Customer)dataObject).id);
                object.put("gender", ""+((Customer)dataObject).gender);
                object.put("birthday", ""+((Customer)dataObject).birthday);
                object.put("nickname", ""+((Customer)dataObject).username);
                object.put("phone_number", ""+((Customer)dataObject).phone_number);
                jsonData = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (dataObject instanceof String[]) {

            JSONArray array = new JSONArray();
            try {
                for (int i = 0; i < ((String[]) dataObject).length; i++) {
                    array.put( ((String[]) dataObject)[i]  );
                }
                jsonData = array.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonData;
    }

    public static String readFromFile(Context context, String filename) {

        String ret = "";

        try {
            FileInputStream inputStream = new FileInputStream(openInAppDir(context, filename));
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private static String openInAppDir(Context context, String filename) {
        String appPath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        // current dir
        File fileDir = new File(appPath + "/" + "local_data");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getAbsolutePath()+"/"+filename;
    }

    public static void writeToFile(Context context, String filename, String data) {

        try {
            FileWriter out = new FileWriter(new File(openInAppDir(context, filename)));
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String arrayToString(String[] subStrings) {

        String res = "";
        for (int i = 0; i < subStrings.length; i++) {
            res += subStrings[i];
        }
        return res;
    }

    public static String superTrim(String restaurant_name) {
        return arrayToString(restaurant_name.split(" "));
    }

    public static String changePathToBase64(String path) {


        double DESIREDWIDTH = 480;
        double DESIREDHEIGHT = 0;

        try {
            Bitmap unscaledBitmap = BitmapFactory.decodeFile(path);

            double width = unscaledBitmap.getWidth();

            if (width > 480) {
                double ratio = width/480;
                DESIREDHEIGHT = unscaledBitmap.getHeight()/ratio;
            } else {
                DESIREDHEIGHT = unscaledBitmap.getHeight();
            }


            /* turn image if there is to turn it */

            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, (int)DESIREDWIDTH, (int)DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);


            byte[] byteFormat = stream.toByteArray();
            // get the base 64 string
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            return imgString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String timeStampToDate (Context context, String timeStamp) {
        try {
            long unixSeconds = Long.parseLong(timeStamp);
            Date commandTime = new java.util.Date(unixSeconds * 1000L);

            Date currentTime = new Date();

            String pattern_not_today = "yyyy-MM-dd HH:mm:ss";
            String pattern_today = "HH:mm:ss";

            SimpleDateFormat sdf = new java.text.SimpleDateFormat();
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
            String formattedDate = "";

            if (commandTime.getMonth() == currentTime.getMonth() &&
                    commandTime.getYear() == currentTime.getYear()) {

                sdf = new java.text.SimpleDateFormat(pattern_today);
                formattedDate = sdf.format(commandTime);
                if (commandTime.getDate() == currentTime.getDate()) {
                    formattedDate = context.getResources().getString(R.string.today) + " " + formattedDate;
                } else if (commandTime.getDate() + 1 == currentTime.getDate()) {
                    formattedDate = context.getResources().getString(R.string.yesterday) + " " + formattedDate;
                } else {
                    sdf = new java.text.SimpleDateFormat(pattern_not_today);
                    formattedDate = sdf.format(commandTime);
                }
            }

            return formattedDate;
        } catch (Exception e){
            e.printStackTrace();
        }
        return "-- --";
    }

    public static boolean isBase64(String tmp) {
        if (tmp != null && tmp.length() > 100)
            return true;
        return false;
    }

    public static boolean checkFileExistsLocally(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String readAssetsStyleFile(Context context) {

        String ret = "";

        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream =  assetManager.open("style/style1_0.css");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(Constant.APP_TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(Constant.APP_TAG, "Can not read file: " + e.toString());
        }
        return ret;
    }

    public static void playPurchaseSuccessSound(Context context) {

        try {

            String path = "android.resource://" + context.getPackageName() + "/" + R.raw.command_success_hold_on;
            Log.d(Constant.APP_TAG, path);
            Uri soundUri = Uri.parse(path);

            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(context, soundUri);
            mp.prepare();
            mp.setVolume(1f, 1f);
            mp.setLooping(false);
            mp.start();

            int duration = 1000;
            vibrate(context, duration);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void vibrate(Context context, int duration) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else{
            //deprecated in API 26
            v.vibrate(duration);
        }
    }

    public static Restaurant_Menu_FoodEntity basketItemToFoodEntity(BasketInItem basketInItem) {

        Restaurant_Menu_FoodEntity foodEntity = new Restaurant_Menu_FoodEntity();
        foodEntity.id = basketInItem.id;
        foodEntity.name = basketInItem.name;
        foodEntity.price = basketInItem.price;
        foodEntity.pic = basketInItem.pic;
        foodEntity.food_details_pictures = basketInItem.food_details_pictures;
        return foodEntity;
    }

    public static Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }


    /* convert working hour to a more human working hour
     * -
     *
     * */
    public static String strToHour(String working_hour) {

        String str = "";

        if (working_hour == null || "".equals(working_hour)){
            return str;
        }

        try {
            if (working_hour != null || working_hour.length() >= 9) {
                str += (working_hour.substring(0, 2) + "h" + working_hour.substring(3, 5));
                str += "-";
                str += (working_hour.substring(6, 8) + "h" + working_hour.substring(9));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isPhoneNumber_TGO (String phone_no) {

        String regex = "^[9,7]{1}[0,1,2,3,6,7,8,9]{1}[0-9]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone_no);
        boolean res = matcher.matches();
        return res;
    }

    public static boolean isPhoneNumber_Tgcel (String phone_no) {

        String regex = "^[9,7]{1}[0-3]{1}[0-9]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone_no);
        boolean res = matcher.matches();
        return res;
    }

    public static boolean isPhoneNumber_Moov (String phone_no) {

        String regex = "^9[6-9]{1}[0-9]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone_no);
        boolean res = matcher.matches();
        return res;
    }

    public static String reverseString(String mbalance) {

        if (mbalance == null || "".equals(mbalance) || mbalance.length() == 0) {
            return mbalance;
        }

        String good = "";
        for (int i = mbalance.length()-1; i >= 0; i--) {
            good += (mbalance.charAt(i));
        }
        return good;
    }

    public static String intToMoney (String balance) {

        if (balance == null || "".equals(balance))
            balance = "0";

        if (Integer.valueOf(balance) < 1000) {
            return balance;
        }

        String mbalance = reverseString(balance);
        try {
            Log.d(Constant.APP_TAG, balance);
            String res = "";

            for (int cx = 0; cx < mbalance.length(); cx++) {
                res += (mbalance.charAt(cx));
                if (cx % 3 == 2 && cx != 0 && cx+1 != mbalance.length()) {
                    res += ".";
                }
            }

            Log.d(Constant.APP_TAG, reverseString(mbalance));

            return reverseString(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "...";
    }

    public static List<Transaction> reverseArray(List<Transaction> transactions) {

        List<Transaction> trans = new ArrayList<>();
        for (int i = transactions.size()-1; i >= 0; i--) {
            trans.add(transactions.get(i));
        }
        return trans;
    }

    public static void StoreBalance (Context context, String balance){
        SharedPreferences preferences = context.getSharedPreferences(Config.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.BALANCE_FIELD, balance);
        editor.commit();
    }

    public static String encryptKabaWay (Context context, String code) {

        String hash = "";

        return hash;
    }


    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
}

