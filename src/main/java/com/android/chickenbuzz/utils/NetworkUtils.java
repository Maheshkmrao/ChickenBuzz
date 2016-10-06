package com.android.chickenbuzz.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.android.chickenbuzz.R;


/**
 * Created by krelluru on 8/27/2016.
 */

public class NetworkUtils {

    /**
     * Checks if Network connectivity is available
     */
    public final static boolean checkInternetConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean status=netInfo != null && netInfo.isConnectedOrConnecting();
        if(status){
            return true;
        }else{
            return false;
        }
    }

    public static void showToast(Context context,String Msg){
        Toast.makeText(context, Msg, Toast.LENGTH_LONG).show();
    }

    public static void showAlertDialog(Context context,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppThemeDialog));
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(msg);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });*/
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
