package com.android.chickenbuzz.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chickenbuzz.R;

import java.io.ByteArrayOutputStream;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Shreya Kotak on 12/05/16.
 */
public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 101;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 103;

    private static final String TAG = Utility.class.getSimpleName();

    private static final SimpleDateFormat NORMAL_INPUT_DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static final SimpleDateFormat INPUT_DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss" /*"dd-MM-yyyy HH:mm:ss"*/);
    private static final SimpleDateFormat OUTPUT_DATE_FORMATTER = new SimpleDateFormat(
			/*"yyyy-MM-dd"*/"dd-MM-yyyy");

    private static final SimpleDateFormat INPUT_OTRS_DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssZ");

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, String permission)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if(permission.equalsIgnoreCase("Storage")) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(context.getString(R.string.title_permission_necessary));
                        alertBuilder.setMessage(context.getString(R.string.err_msg_external_storage_permission_necessary));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            }
            else if(permission.equalsIgnoreCase("Camera")) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(context.getString(R.string.title_permission_necessary));
                        alertBuilder.setMessage(context.getString(R.string.err_msg_camera_permission_necessary));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return false;
                } else {
                    return true;
                }
            }
            else if(permission.equalsIgnoreCase("Location")) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(context.getString(R.string.title_permission_necessary));
                        alertBuilder.setMessage(context.getString(R.string.err_msg_location_permission_necessary));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                    return false;
                } else {
                    return true;
                }
            }
            else if(permission.equalsIgnoreCase("Call")) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(context.getString(R.string.title_permission_necessary));
                        alertBuilder.setMessage(context.getString(R.string.err_msg_call_permission_necessary));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                    }
                    return false;
                } else {
                    return true;
                }
            }

        } else {
            return true;
        }
        return true;
    }

    public static void requestGPSProviderPermission(final Context mActivity){
        LocationManager lm = (LocationManager)mActivity.getSystemService(mActivity.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
            dialog.setMessage(mActivity.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(mActivity.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mActivity.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(mActivity.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }

    /**
     * Used to convert String date into formated String date.
     *
     * @param dateString
     * @return
     */
    public static String getFormattedDate(String dateString) {
        String formattedDate = "";
        try {
            Date date = NORMAL_INPUT_DATE_FORMATTER.parse(dateString);
            formattedDate = OUTPUT_DATE_FORMATTER.format(date);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing date " + dateString + " " + e.toString());
        }

        return formattedDate;
    }

    /**
     * Used to convert String date into Date as INPUT_DATE_FORMATTER
     *
     * @param response_date
     * @return
     */
    public static Date convertStringToDate(String response_date) {
        Date date = new Date();
        try {
            date = NORMAL_INPUT_DATE_FORMATTER.parse(response_date);
        } catch (ParseException pex) {
                Log.d(TAG,
                        "Exception in convertStringToDate ::"
                                + pex.getMessage());
        }

        return date;
    }

    public static Date convertStringToOTRSDate(String response_date) {
        Date date = new Date();
        try {
            date = INPUT_OTRS_DATE_FORMATTER.parse(response_date);
        } catch (ParseException pex) {
                Log.d(TAG,
                        "Exception in convertStringToDate ::"
                                + pex.getMessage());
        }

        return date;
    }

    /**
     * Used to save the String data into SP into the corresponding KEY.
     *
     * @param activity
     * @param key
     * @param value
     */
    public static void saveStringPreference(Activity activity, String key,
                                            String value) {
        SharedPreferences preference = activity.getSharedPreferences(
                Constant.SHARED_PREFERENCE_NAME,
                android.content.Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preference.edit();
        //Encrypted String Value.
        try{
            Log.d(TAG, "Encryption Value for ::" + value);
            if(!TextUtils.isEmpty(value))
//                value = AESHelper.encrypt(value);
//                value = EncEngine.encrypt(activity, activity.getResources().getString(R.string.key), value);
            
                Log.d(TAG, "Encryption Value isss ::" + value);
        }catch (Exception ex){
            Log.d(TAG, "Encryption Exception ::" + ex.getMessage());
        }
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * Used to save the Boolean data into SP into the corresponding KEY.
     *
     * @param activity
     * @param key
     * @param value
     */
    public static void saveBooleanPreference(Activity activity, String key,
                                             boolean value) {
        SharedPreferences preference = activity.getSharedPreferences(
                Constant.SHARED_PREFERENCE_NAME,
                android.content.Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    /**
     * Used to get the String from the saved SP from the corresponding KEY.
     *
     * @param activity
     * @param key
     * @return
     */
    public static String getStringFromPreference(Activity activity, String key) {
        SharedPreferences preference = activity.getSharedPreferences(
                Constant.SHARED_PREFERENCE_NAME,
                android.content.Context.MODE_PRIVATE);
        String preferenceValue = preference.getString(key, "");

        try {
            Log.d(TAG, "Decryption Value for ::" + preferenceValue);
            if(!TextUtils.isEmpty(preferenceValue))
//                preferenceValue = AESHelper.decrypt(preferenceValue);
//            preferenceValue = EncEngine.decrypt(activity, activity.getResources().getString(R.string.key), preferenceValue);

            Log.d(TAG, "Decryption Value isss ::" + preferenceValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceValue;
    }

    /**
     * Used to get the boolean from the saved SP from the corresponding KEY.
     *
     * @param activity
     * @param key
     * @return
     */
    public static boolean getBooleanFromPreference(Activity activity, String key) {
        SharedPreferences preference = activity.getSharedPreferences(
                Constant.SHARED_PREFERENCE_NAME,
                android.content.Context.MODE_PRIVATE);
        return preference.getBoolean(key, false);
    }


    public static Bitmap getCircularBitmap(Bitmap bitmap){
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

        return circleBitmap;
    }

    public static String convertiBitpmapToString(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return image_str;
    }

    /**
     * This method used to asks user for logout
     *
     * @param
     */
    public static void logoutAlert(final Activity _activity, String message,
                                   android.content.DialogInterface.OnClickListener positiveListener,
                                   android.content.DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setMessage(message)
                .setPositiveButton(
                        _activity.getResources().getString(R.string.yes),
                        positiveListener)
                .setNegativeButton(
                        _activity.getResources().getString(R.string.no),
                        negativeListener);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Used to display dialog with specific Listener to handle the Ok Button.
     *
     * @param mActivity
     * @param message
     *
     */
    public static void displayCallLayoutDialog(Activity mActivity,
                                              String message, String closeButton, View.OnClickListener yesListener, View.OnClickListener noListener) {
        try {
            // Custom Dialog
            final Dialog dialog = new Dialog(mActivity, R.style.myDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_buttons_dialog);
            dialog.setCancelable(false);
            // dialog.setTitle(mActivity.getResources().getString(R.string.result));
            mActivity.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            TextView text = (TextView) dialog.findViewById(R.id.textView1);
            text.setText(message);

            Button okButton = (Button) dialog.findViewById(R.id.btn_cancel);
            Button noButton = (Button) dialog.findViewById(R.id.btn_start);
            if(closeButton.equalsIgnoreCase("")){
                okButton.setOnClickListener(yesListener);
                noButton.setOnClickListener(noListener);
            }
            else{
                okButton.setVisibility(View.GONE);
                noButton.setText(mActivity.getResources().getString(R.string.button_close));
                noButton.setOnClickListener(noListener);
            }

            dialog.show();
        } catch (Exception ex) {
            Log.d(TAG, "Exception :" + ex.getMessage());
        }
    }

    /**
     * Make a call to the specific number also if the number is any USSD code
     * then dials the USSD code. <uses-permission
     * android:name="android.permission.CALL_PHONE" />
     */
    public static void callNumber(Activity _activity, String number) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if (number.contains("#")) {
                callIntent.setData(Uri.parse("tel:"
                        + number.replaceAll("#", Uri.encode("#"))));
            } else {
                callIntent.setData(Uri.parse("tel:" + number));
            }
            _activity.startActivity(callIntent);

        } catch (ActivityNotFoundException activityException) {
            Log.e("hello android", "Call failed",
                    activityException);
        }
        catch (SecurityException securityException) {
            Toast.makeText(_activity, "Please grant Call permission in application Settings and try again later.", Toast.LENGTH_LONG).show();

        }
    }

}
