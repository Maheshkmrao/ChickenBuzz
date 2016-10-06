package com.android.chickenbuzz.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.beans.UserDetails;
import com.android.chickenbuzz.fragments.HomeScreenMapFragment;
import com.android.chickenbuzz.fragments.SettingsFragment;
import com.android.chickenbuzz.fragments.WatchMeFragment;
import com.android.chickenbuzz.global.ChickenBuzzApplication;
import com.android.chickenbuzz.utils.Constant;
import com.android.chickenbuzz.utils.NetworkUtils;
import com.android.chickenbuzz.utils.Utility;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
//    TextView mtootlbarTitle;
    HomeScreenMapFragment mMapFragmant;

    private UserDetails muserDetails;
    private TextView nameTv;
    private TextView phoneNoTv;
    private ImageView ivVectorImage;
    public static final String REQUEST_TAG = "MainActivity";

    private RelativeLayout mSettingsLayout, mHomeLayout, mBuzzLayout, mTrackMeLayout;
    private RelativeLayout mCallPoliceLayout, mMedicalHelpLayout, mBuzzContactsLayout, mWatchMeLayout;
    private Dialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setTitle(getString(R.string.app_name));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mtootlbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        ivVectorImage = (ImageView) header.findViewById(R.id.imageView);
//        ivVectorImage.setColorFilter(getResources().getColor(R.color.colorPrimary));

        nameTv = (TextView) header.findViewById(R.id.textview_name);
        phoneNoTv = (TextView) header.findViewById(R.id.textview_phonenumber);

//        nameTv.setText(Utility.getStringFromPreference(MainActivity.this, Constant.KEY_EMAIL));
//        phoneNoTv.setText(Utility.getStringFromPreference(MainActivity.this, Constant.KEY_MOBILE_NO));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.closeDrawer(GravityCompat.START);
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        displayMapFragment();

        initialiseUIControls();

        setSelectedLayout(1);
    }

    private void initialiseUIControls() {
        //Header Buttons
        mSettingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout);
        mBuzzLayout = (RelativeLayout) findViewById(R.id.buzz_layout);
        mTrackMeLayout = (RelativeLayout) findViewById(R.id.track_me_layout);
        //Footer Buttons
        mCallPoliceLayout = (RelativeLayout) findViewById(R.id.call_police_layout);
        mMedicalHelpLayout = (RelativeLayout) findViewById(R.id.medical_help_layout);
        mBuzzContactsLayout = (RelativeLayout) findViewById(R.id.buzz_contacts_layout);
        mWatchMeLayout = (RelativeLayout) findViewById(R.id.watch_me_layout);

        mSettingsLayout.setOnClickListener(this);
        mHomeLayout.setOnClickListener(this);
        mBuzzLayout.setOnClickListener(this);
        mTrackMeLayout.setOnClickListener(this);
        mCallPoliceLayout.setOnClickListener(this);
        mMedicalHelpLayout.setOnClickListener(this);
        mBuzzContactsLayout.setOnClickListener(this);
        mWatchMeLayout.setOnClickListener(this);

        LinearLayout follow_you_button_2 = (LinearLayout) findViewById(R.id.follow_you_button_2);
        follow_you_button_2.setSelected(true);
        follow_you_button_2.setBackgroundColor(getColor(R.color.colorAccent));
    }

    private void setSelectedLayout(int selectedLayout) {
        if(selectedLayout == 0){
            mSettingsLayout.setActivated(true);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(false);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(true);
        }
        else if(selectedLayout == 1){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(true);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(false);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(true);
        }
        else if(selectedLayout == 2){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(true);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(false);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(true);
        }
        else if(selectedLayout == 3){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(true);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(false);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(true);
        }

        else if(selectedLayout == 4){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(true);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(false);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(true);
        }
        else if(selectedLayout == 5){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(true);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(false);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(true);
        }
        else if(selectedLayout == 6){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(true);
            mWatchMeLayout.setActivated(false);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(false);
            mWatchMeLayout.setClickable(true);
        }
        else if(selectedLayout == 7){
            mSettingsLayout.setActivated(false);
            mHomeLayout.setActivated(false);
            mBuzzLayout.setActivated(false);
            mTrackMeLayout.setActivated(false);
            mCallPoliceLayout.setActivated(false);
            mMedicalHelpLayout.setActivated(false);
            mBuzzContactsLayout.setActivated(false);
            mWatchMeLayout.setActivated(true);

            mSettingsLayout.setClickable(true);
            mHomeLayout.setClickable(true);
            mBuzzLayout.setClickable(true);
            mTrackMeLayout.setClickable(true);
            mCallPoliceLayout.setClickable(true);
            mMedicalHelpLayout.setClickable(true);
            mBuzzContactsLayout.setClickable(true);
            mWatchMeLayout.setClickable(false);
        }
    }

    private void displayMapFragment() {
        HomeScreenMapFragment homeScreenMapFragment = new HomeScreenMapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, homeScreenMapFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Constant.IS_HOME_SCREEN_DISPLAYED = true;
        muserDetails = ((ChickenBuzzApplication)getApplication()).getmUserAboutDetails();
        if(muserDetails == null) {
            if (NetworkUtils.checkInternetConnectivity(MainActivity.this)) {
                showProgressDialog();
                callAboutMeSpi();
            } else {
                NetworkUtils.showAlertDialog(MainActivity.this, getString(R.string.check_your_network_connection));
            }
        }
    }

    private void callAboutMeSpi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_GET_ABOUTME_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
//                        showToastMsg(MainActivity.this, "Response ::" + response);
                        String result = parseAboutMeDetailsResponse(response);
                        if(result.equalsIgnoreCase("SUCCESS")){
                            updateAboutUI();
                        }
                        else
                            showToastMsg(MainActivity.this, "Failed ::" + result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkUtils.showAlertDialog(MainActivity.this,error.getMessage());
                        hideProgressDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", Utility.getStringFromPreference(MainActivity.this, Constant.KEY_USER_ID));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(REQUEST_TAG);
        requestQueue.add(stringRequest);
    }

    private void updateAboutUI() {
        nameTv.setText(Utility.getStringFromPreference(MainActivity.this, Constant.KEY_EMAIL));
        phoneNoTv.setText(Utility.getStringFromPreference(MainActivity.this, Constant.KEY_MOBILE_NO));

    }

    private String parseAboutMeDetailsResponse(String response) {
        String result = "SUCCESS";
        try {
            if(response != null && !response.equals("")) {
                JSONObject responseobject = new JSONObject(response);
                if (responseobject != null) {
                    if (responseobject.has("reponse_code") &&
                            responseobject.getString("reponse_code").equalsIgnoreCase("0")) {
                        NetworkUtils.showAlertDialog(MainActivity.this, getString(R.string.err_msg_mandatory_fields_not_filled));
                    } else if (responseobject.has("json_data")) {
                        JSONObject message = (JSONObject) responseobject.get("json_data");
                        if (message.has("message")) {
                            if (message.getString("message").equalsIgnoreCase("Success")) {
                                if(message.has("data")) {
                                    JSONArray data = (JSONArray) message.getJSONArray("data");
                                    muserDetails = new UserDetails();
                                    if(data != null && data.length() > 0) {
                                        for (int count = 0; count < data.length(); count++) {
                                            JSONObject dataObject = (JSONObject) data.get(count);
                                            if (dataObject != null) {
                                                muserDetails.setUser_id((dataObject.has("user_id") ? dataObject.getString("user_id") : ""));
                                                muserDetails.setFirst_name((dataObject.has("first_name") ? dataObject.getString("first_name") : ""));
                                                muserDetails.setLast_name((dataObject.has("last_name") ? dataObject.getString("last_name") : ""));
                                                muserDetails.setHeight((dataObject.has("height") ? dataObject.getString("height") : ""));
                                                muserDetails.setEmail((dataObject.has("email") ? dataObject.getString("email") : ""));
                                                muserDetails.setGender((dataObject.has("gender") ? dataObject.getString("gender") : ""));
                                                muserDetails.setDob((dataObject.has("dob") ? dataObject.getString("dob") : ""));
                                                muserDetails.setImage((dataObject.has("image") ? dataObject.getString("image") : ""));
                                                muserDetails.setIsd_code((dataObject.has("isd_code") ? dataObject.getString("isd_code") : ""));
                                                muserDetails.setMobile_no((dataObject.has("mobile_no") ? dataObject.getString("mobile_no") : ""));
                                                muserDetails.setAlternate_number((dataObject.has("alternate_number") ? dataObject.getString("alternate_number") : ""));
                                                muserDetails.setPin((dataObject.has("pin") ? dataObject.getString("pin") : ""));
                                                muserDetails.setEmergency_pin((dataObject.has("emergency_pin") ? dataObject.getString("emergency_pin") : ""));
                                                muserDetails.setM_verify_code((dataObject.has("m_verify_code") ? dataObject.getString("m_verify_code") : ""));
                                                muserDetails.setRefer_code((dataObject.has("refer_code") ? dataObject.getString("refer_code") : ""));
                                                muserDetails.setRef_user_id((dataObject.has("ref_user_id") ? dataObject.getString("ref_user_id") : ""));
                                                muserDetails.setIs_active((dataObject.has("is_active") ? dataObject.getString("is_active") : ""));
                                                muserDetails.setDate((dataObject.has("date") ? dataObject.getString("date") : ""));

                                            }
                                        }

                                        ((ChickenBuzzApplication)getApplication()).setmUserAboutDetails(muserDetails);
                                    }
                                    else {
                                        result = "FAILED";
                                    }
                                }
                                else {
                                    result = "FAILED";
                                }
                            } else {
                                result = getString(R.string.error_unable_to_get_response);
//                                NetworkUtils.showAlertDialog(MainActivity.this, getString(R.string.error_unable_to_get_response));
                            }
                        } else {
                            result = getString(R.string.error_unable_to_get_response);
//                            NetworkUtils.showAlertDialog(MainActivity.this, getString(R.string.error_unable_to_get_response));
                        }
                    } else {
                        result = getString(R.string.error_unable_to_get_response);
//                        NetworkUtils.showAlertDialog(MainActivity.this, getString(R.string.error_unable_to_get_response));
                    }
                }
            }
        } catch (Exception error) {
            result = error.getMessage();
            NetworkUtils.showAlertDialog(MainActivity.this,error.getMessage());
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if(menuItem.isChecked()){
            menuItem.setChecked(false);
        }else{
            menuItem.setChecked(true);
            //setTitle(menuItem.getTitle());
//            mtootlbarTitle.setText(menuItem.getTitle());
        }

        //Closing drawer on item click
        drawerLayout.closeDrawers();
        Intent intent;

        switch (menuItem.getItemId()){
            case R.id.nav_about_me:
//                mMapFragmant = null;
//                mtootlbarTitle.setText(getString(R.string.title_aboutme));
//                AboutMeFragment aboutmeFragment = new AboutMeFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, aboutmeFragment);
//                fragmentTransaction.commit();

                intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_my_locations:
//                NetworkUtils.showAlertDialog(this,"Need Client Confirmation");
//                mtootlbarTitle.setText(getString(R.string.title_my_locations));

//                MyLocationsFragment mapsFragment = new MyLocationsFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//                if(mMapFragmant == null) {
//                    mMapFragmant = mapsFragment;
//                    fragmentTransaction.replace(R.id.flContent, mapsFragment);
//                    fragmentTransaction.commit();
//                }
//                else {
//                    showToastMsg(MainActivity.this, getString(R.string.err_msg_already_opening_fragment));
//                }

                Intent i = new Intent(this, MyLocationsActivity.class);
                startActivity(i);
                return true;
            case R.id.nav_monitor_me:
//                NetworkUtils.showAlertDialog(this,"Need Client Confirmation");

//                mMapFragmant = null;
//                FollowMeFragment followMeFragment = new FollowMeFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, followMeFragment);
//                fragmentTransaction.commit();

                intent = new Intent(MainActivity.this, FollowMeActivity.class);
                startActivity(intent);

                return true;
            case R.id.nav_moniering_you:
//                mMapFragmant = null;
//                FollowYouFragment followYouFragment = new FollowYouFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, followYouFragment);
//                fragmentTransaction.commit();

                intent = new Intent(MainActivity.this, FollowYouActivity.class);
                startActivity(intent);

                return true;
            case R.id.nav_invite_friends_family:
//                mMapFragmant = null;
//                InviteFragment inviteFragment = new InviteFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, inviteFragment);
//                fragmentTransaction.commit();

                intent = new Intent(MainActivity.this, InviteActivity.class);
                startActivity(intent);

                return true;
            case R.id.nav_my_alerts:
//                mMapFragmant = null;
                intent = new Intent(MainActivity.this, MyAlertsActivity.class);
                startActivity(intent);
//                NetworkUtils.showAlertDialog(this,"Need Client Confirmation");
                return true;
            case R.id.nav_settings:
//                mMapFragmant = null;
//                SettingsFragment settingsFragment = new SettingsFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, settingsFragment);
//                fragmentTransaction.commit();

                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

                return true;

            case R.id.nav_change_pin:
//                mMapFragmant = null;
//                NetworkUtils.showAlertDialog(this,"Need Client Confirmation");
                intent = new Intent(MainActivity.this, ChangePinActivity.class);
                startActivity(intent);
                return true;

        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utility.logoutAlert(
                    MainActivity.this,
                    getResources().getString(
                            R.string.do_you_want_to_exit_application),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // If Clicked on YES, then need to exit the
                            // application.
                            MainActivity.this.finish();
                        }
                    }, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int paramInt) {
                            dialog.cancel();
                        }
                    });

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = null;

        switch (view.getId()) {
            case R.id.settings_layout:
                setSelectedLayout(0);

                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, settingsFragment);
                fragmentTransaction.commit();

                break;

            case R.id.home_layout:
                setSelectedLayout(1);

                HomeScreenMapFragment homeFragment = new HomeScreenMapFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, homeFragment);
                fragmentTransaction.commit();

                mMapFragmant = homeFragment;
                break;

            case R.id.buzz_layout:
                setSelectedLayout(2);
//                HomeScreenMapFragment homeFragment = new HomeScreenMapFragment();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.flContent, homeFragment);
//                fragmentTransaction.commit();

                break;

            case R.id.track_me_layout:
                setSelectedLayout(3);
                if(mMapFragmant == null) {
                    HomeScreenMapFragment trackMeFragment = new HomeScreenMapFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, trackMeFragment);
                    fragmentTransaction.commit();
                }
                break;

            case R.id.call_police_layout:
                setSelectedLayout(4);
                displayCallLayoutDialog(MainActivity.this, getResources().getString(R.string.call_to_police_no),
                        "", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAlertDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(Utility.checkPermission(MainActivity.this, "Call")){
                                    Utility.callNumber(MainActivity.this, "911");
                                }
                                else
                                    showToastMsg(MainActivity.this, getString(R.string.err_msg_permission_call));
                            }
                        });
                break;
            case R.id.medical_help_layout:
                setSelectedLayout(5);
                displayCallLayoutDialog(MainActivity.this, getResources().getString(R.string.call_to_ambulance),
                        "", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAlertDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(Utility.checkPermission(MainActivity.this, "Call")){
                                    Utility.callNumber(MainActivity.this, "108");
                                }
                                else
                                    showToastMsg(MainActivity.this, getString(R.string.err_msg_permission_call));
                            }
                        });
                break;
            case R.id.buzz_contacts_layout:
                setSelectedLayout(6);
                displayCallLayoutDialog(MainActivity.this, getResources().getString(R.string.buzz_contacts_dialog_title),
                        "Close", null, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAlertDialog.dismiss();
                            }
                        });
                break;
            case R.id.watch_me_layout:
                setSelectedLayout(7);

                WatchMeFragment watchMeFragment = new WatchMeFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, watchMeFragment);
                fragmentTransaction.commit();

                break;

        }
    }

    /**
     * Used to display mAlertDialog with specific Listener to handle the Ok Button.
     *
     * @param mActivity
     * @param message
     *
     */
    private void displayCallLayoutDialog(Activity mActivity,
                                               String message, String closeButton, View.OnClickListener yesListener, View.OnClickListener noListener) {
        try {
            // Custom Dialog
            mAlertDialog = new Dialog(mActivity, R.style.Theme_Dialog);
            mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mAlertDialog.setContentView(R.layout.custom_buttons_dialog);
            mAlertDialog.setCancelable(false);
            // mAlertDialog.setTitle(mActivity.getResources().getString(R.string.result));
            mActivity.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            TextView text = (TextView) mAlertDialog.findViewById(R.id.textView1);
            text.setText(message);

            Button okButton = (Button) mAlertDialog.findViewById(R.id.btn_cancel);
            Button noButton = (Button) mAlertDialog.findViewById(R.id.btn_start);
            if(closeButton.equalsIgnoreCase("")){
                okButton.setOnClickListener(yesListener);
                noButton.setOnClickListener(noListener);
            }
            else{
                okButton.setVisibility(View.GONE);
                noButton.setText(mActivity.getResources().getString(R.string.button_close));
                noButton.setOnClickListener(noListener);
            }

            mAlertDialog.show();
        } catch (Exception ex) {
            Log.d("MainActivity", "Exception :" + ex.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showToastMsg(MainActivity.this, getString(R.string.permission_granted_call));
        } else {
            showToastMsg(MainActivity.this, getString(R.string.err_msg_permission_call));
        }
    }

}
