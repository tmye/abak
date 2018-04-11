package tg.tmye.kaba._commons.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import tg.tmye.kaba.data.customer.Customer;


/**
 * By abiguime on 2017/12/6.
 * email: 2597434002@qq.com
 */

public class UtilFunctions {

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

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
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
