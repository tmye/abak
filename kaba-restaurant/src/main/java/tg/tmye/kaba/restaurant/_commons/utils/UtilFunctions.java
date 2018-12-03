package tg.tmye.kaba.restaurant._commons.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import tg.tmye.kaba.restaurant.R;
import tg.tmye.kaba.restaurant.syscore.Constant;


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
        long unixSeconds = Long.parseLong(timeStamp);
        Date commandTime = new Date(unixSeconds*1000L);

        Date currentTime = new Date();

        String pattern_not_today = "yyyy-MM-dd HH:mm:ss";
        String pattern_today = "HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        String formattedDate = "";

        if (commandTime.getDay() == currentTime.getDay() && commandTime.getMonth() == currentTime.getMonth() &&
                commandTime.getYear() == currentTime.getYear()) {

            sdf = new SimpleDateFormat(pattern_today);
            formattedDate =  sdf.format(commandTime);
            formattedDate = context.getResources().getString(R.string.today)+" "+formattedDate;
        } else {
            sdf = new SimpleDateFormat(pattern_not_today);
            formattedDate =  sdf.format(commandTime);
        }

        return formattedDate;
    }

    public static String timeStampToDateDayOnly (Context context, String timeStamp) {
        long unixSeconds = Long.parseLong(timeStamp);
        Date commandTime = new Date(unixSeconds*1000L);

        String pattern_not_today = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        String formattedDate = "";
        sdf = new SimpleDateFormat(pattern_not_today);
        formattedDate =  sdf.format(commandTime);

        return formattedDate;
    }

/*
    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }
*/

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


    private static void vibrate(Context context, int duration) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(duration);
        }
    }

    public static void playSuccessSound(Context context) {

        try {

            String path = "android.resource://" + context.getPackageName() + "/" + R.raw.operation_success;
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

    public static String reverseString(String mbalance) {

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

    public static String getDay  (Context context, String timeStamp) {
        long unixSeconds = Long.parseLong(timeStamp);
        Date commandTime = new Date(unixSeconds*1000L);
        /* day */
        String[] days_of_week = context.getResources().getStringArray(R.array.days_of_week);
        return days_of_week[commandTime.getDay()];
    }

    /*
    * if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    * */
}
