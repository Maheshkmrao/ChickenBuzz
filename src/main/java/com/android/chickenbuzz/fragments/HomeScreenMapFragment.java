package com.android.chickenbuzz.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.activity.BaseActivity;
import com.android.chickenbuzz.activity.MainActivity;
import com.android.chickenbuzz.utils.Constant;
import com.android.chickenbuzz.utils.DirectionsJSONParser;
import com.android.chickenbuzz.utils.NetworkUtils;
import com.android.chickenbuzz.utils.Utility;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenMapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerDragListener {

    private BaseActivity mActivity;

    private MapFragment mMapFragment;
    // Google Map
    private GoogleMap googleMap;

//    private Spinner mLocationsSpinner;
//    private TextInputLayout mNicknameTextLayout;
//    private EditText mNicknameText;
//    private EditText mAddressText;
//    private ImageButton mAddLocationButton;
//    private ImageButton mDeleteLocationButton;
//    private Button mNextButton;
    private String REQUEST_TAG = "AddLocation";

    private String[] mMyLocations;

    private double mLatitute, mLongitude;

    private boolean isLocationPermissionEnabled;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    LatLng mnewAddressLatlng;
    Marker currLocationMarker;
    Marker mNewAddressLocationMarker;
    private LatLng mMyCurrentLocation;

    ArrayList<LatLng> markerPoints;

    public HomeScreenMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreenMapFragment newInstance(String param1, String param2) {
        HomeScreenMapFragment fragment = new HomeScreenMapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = ((BaseActivity) getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home_screen_maps, container, false);

//        isLocationPermissionEnabled = Utility.checkPermission(mActivity, "Location");
//        mActivity.showToastMsg(mActivity,"onCreateView");
//        initilizeUIControls(view);

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Loading map
        initilizeMap();
//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        mMap = ((MapFragment) getActivity().getFragmentManager()
//                .findFragmentById(R.id.map)).getMapAsync(this);

        return view;
    }

//    private void initilizeUIControls(View view) {
//        mNicknameText = (EditText) view.findViewById(R.id.editext_nickname);
//        mAddressText = (EditText) view.findViewById(R.id.searchview);
//        mNicknameTextLayout = (TextInputLayout) view.findViewById(R.id.input_layout_nickname);
//        mLocationsSpinner = (Spinner) view.findViewById(R.id.locations_spinner);
//
//        mMyLocations = new String[]{"All", "My Location 1","My Location 2"};
//        CustomSpinnerAdapter mspinnerAdapter = new CustomSpinnerAdapter(mActivity, mMyLocations);
//        mLocationsSpinner.setAdapter(mspinnerAdapter);
//
////        mNicknameTextLayout.setVisibility(View.GONE);
////        mLocationsSpinner.setVisibility(View.VISIBLE);
////        mAddressText.setVisibility(View.GONE);
//        mAddressText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int actionId, KeyEvent keyEvent) {
//                if((keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
//                        || actionId == EditorInfo.IME_ACTION_DONE){
//                    //get Location from Address & moveing Camera.
//                    mnewAddressLatlng = getLatLongFromPlace(mAddressText.getText().toString());
//                    if(mnewAddressLatlng != null) {
//                        String address = getCompleteAddressString(mnewAddressLatlng.latitude, mnewAddressLatlng.longitude);
//
//                        addNewMarker(mnewAddressLatlng, address);
//                        moveCameraToPosition(mnewAddressLatlng);
//                    }
//                }
//                return false;
//            }
//        });
//        mAddLocationButton = (ImageButton) view.findViewById(R.id.add_location_button);
//        mDeleteLocationButton = (ImageButton) view.findViewById(R.id.delete_location_button);
//        mNextButton = (Button) view.findViewById(R.id.btn_next);
//        if(((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails() == null &&
//                !Constant.IS_HOME_SCREEN_DISPLAYED) {
//            mNextButton.setVisibility(View.VISIBLE);
//        }
//
//        mAddLocationButton.setOnClickListener(this);
//        mDeleteLocationButton.setOnClickListener(this);
//        mNextButton.setOnClickListener(this);
//
//    }

    @Override
    public void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
        if (isLocationPermissionEnabled && mMapFragment != null)
            mMapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isLocationPermissionEnabled = Utility.checkPermission(mActivity, "Location");

        if(mActivity instanceof MainActivity){
            Utility.requestGPSProviderPermission(mActivity);
        }
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
        if (isLocationPermissionEnabled && mMapFragment != null)
            mMapFragment.getMapAsync(this);
//        mActivity.showToastMsg(mActivity,"onResume :" + isLocationPermissionEnabled);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            if (mMapFragment != null)
                mActivity.getFragmentManager().beginTransaction().remove(mMapFragment).commit();
        }catch(Exception ex){}
    }

    /**
     * function to load map If map is not created it will create it for you
     * */
    private void initilizeMap() {

        mMapFragment = (MapFragment) mActivity.getFragmentManager().findFragmentById(R.id.map);

        if (isLocationPermissionEnabled)
            mMapFragment.getMapAsync(this);
        else {
//            mActivity.showToastMsg(mActivity, "You can't access this feature until you grant Location permissions.");
//            mMapFragment.getMapAsync(this);
//            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mActivity.showToastMsg(mActivity, "onRequestPermissionsResult");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(mMapFragment != null)
                mMapFragment.getMapAsync(this);
        } else {
            //code for deny
            mActivity.showToastMsg(mActivity, getString(R.string.err_msg_cant_access_location_feature));
//            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
//        mActivity.showToastMsg(mActivity, "onMapReady getting google Maps ");

        // check if map is created successfully or not
        if (googleMap == null) {
            Toast.makeText(mActivity,
                    getString(R.string.err_msg_unable_to_create_maps), Toast.LENGTH_SHORT)
                    .show();
        }
        showGoogleMaps();

    }

    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(mActivity, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        if(mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
    }

    private void showGoogleMaps() {
//        mActivity.showToastMsg(mActivity, "showGoogleMaps");

        // Changing map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mActivity.showToastMsg(mActivity, getString(R.string.err_msg_permission_locations));
//            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Enable / Disable my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                mnewAddressLatlng = latLng;
//                String address = getCompleteAddressString(latLng.latitude, latLng.longitude);
//                addNewMarker(latLng, address);
//                if(address != null && !address.equals("")) {
//                    mActivity.showToastMsg(mActivity, getString(R.string.address) + address);
////                    mAddressText.setText(address);
//                }

                /*// Already two locations
                if(markerPoints.size()>1){
                    markerPoints.clear();
                    googleMap.clear();
                }
//                else if(markerPoints.size() == 0){
//                    googleMap.clear();
//                }
                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                *//**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 *//*
                String address = getCompleteAddressString(latLng.latitude, latLng.longitude);
                if(markerPoints.size()==1){
                    options.title(address.equals("") ? "Source" : address);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else if(markerPoints.size()==2){
                    options.title(address.equals("") ? "Destination" : address);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                googleMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if(markerPoints.size() >= 2){
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }*/
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // Already two locations
                if(markerPoints.size()>1){
//                    markerPoints.clear();
                    googleMap.clear();
                    markerPoints.remove(1);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(mMyCurrentLocation);
                    markerOptions.title(getString(R.string.my_location));
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                    googleMap.addMarker(markerOptions);
                }
//                else if(markerPoints.size() == 0){
//                    googleMap.clear();
//                }
                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                String address = getCompleteAddressString(latLng.latitude, latLng.longitude);
                /*if(markerPoints.size()==1){
                    options.title(address.equals("") ? "Source" : address);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else */if(markerPoints.size()==2){
                    options.title(address.equals("") ? "Destination" : address);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                googleMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if(markerPoints.size() >= 2){
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {
                mActivity.showToastMsg(mActivity, getString(R.string.no_address_found));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mActivity.showToastMsg(mActivity, getString(R.string.no_address_found));
        }
        return strAdd;
    }

    private void addNewMarker(LatLng latLng, String address) {
        if(latLng != null) {
            if(mNewAddressLocationMarker != null)
                mNewAddressLocationMarker.remove();
            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(latLng.latitude, latLng.longitude))
                    .title((address.equals("") ? "Marker" : address));

            mNewAddressLocationMarker = googleMap.addMarker(marker);
        }
        else{
            mActivity.showToastMsg(mActivity, getString(R.string.unable_to_find_your_location));
        }
    }

    private double[] createRandLocation(double latitude, double longitude) {

        return new double[]{latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10)};
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.btn_next:
//                mActivity.dismissKeyboard();
//                mActivity.finish();
//                Intent intent = new Intent(mActivity, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
////                addLocation();
//                break;
//            case R.id.add_location_button:
//                mActivity.dismissKeyboard();
//                if(mLocationsSpinner.isShown()){
//                    mLocationsSpinner.setVisibility(View.GONE);
//                    mNicknameTextLayout.setVisibility(View.VISIBLE);
//                    mAddressText.setVisibility(View.VISIBLE);
//                }
//                addLocation();
//                break;
//            case R.id.delete_location_button:
//                mActivity.dismissKeyboard();
//                mNicknameText.setText("");
//                mAddressText.setText("");
//                mLocationsSpinner.setVisibility(View.VISIBLE);
//                mNicknameTextLayout.setVisibility(View.GONE);
//                mAddressText.setVisibility(View.GONE);
//                break;
        }
    }

//    private void addLocation() {
//        if (!validateNickName())
//            return;
//        if (TextUtils.isEmpty(mAddressText.getText().toString())) {
//            mAddressText.setError(getString(R.string.err_msg_enter_address));
//            return;
//        }
//        if (NetworkUtils.checkInternetConnectivity(mActivity)) {
//            if (!mNicknameText.getText().toString().equals("") &&
//                    !mAddressText.getText().toString().equals("")) {
//                if(mnewAddressLatlng != null) {
//                    mActivity.dismissKeyboard();
//                    mActivity.showProgressDialog();
//                    callAddLocationService();
//                }else  NetworkUtils.showAlertDialog(mActivity, getString(R.string.unable_to_find_your_location));
//            } else {
//                NetworkUtils.showAlertDialog(mActivity, getString(R.string.err_msg_mandatory_fields_not_filled));
//            }
//        } else {
//            NetworkUtils.showAlertDialog(mActivity, getString(R.string.check_your_network_connection));
//        }
//    }

    private void callAddLocationService() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            mActivity.hideProgressDialog();
                            JSONObject responseobject = new JSONObject(response);
                            if (responseobject != null) {
                                if (responseobject.has("reponse_code") &&
                                        responseobject.getString("reponse_code").equalsIgnoreCase("0")) {
                                    NetworkUtils.showAlertDialog(mActivity, getString(R.string.err_msg_mandatory_fields_not_filled));
                                } else if (responseobject.has("json_data")) {
                                    JSONObject message = (JSONObject) responseobject.get("json_data");
                                    if (message.has("message")) {
                                        if (message.getString("message").equalsIgnoreCase("Success")) {
                                            NetworkUtils.showAlertDialog(mActivity, getString(R.string.your_location_added_succssfully));
                                        } else {
                                            NetworkUtils.showAlertDialog(mActivity, getString(R.string.error_unable_to_get_response));
                                        }
                                    } else {
                                        NetworkUtils.showAlertDialog(mActivity, getString(R.string.error_unable_to_get_response));
                                    }
                                } else {
                                    NetworkUtils.showAlertDialog(mActivity, getString(R.string.error_unable_to_get_response));
                                }
                            }
                        } catch (Exception error) {
                            NetworkUtils.showAlertDialog(mActivity, error.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkUtils.showAlertDialog(mActivity, error.getMessage());
                        mActivity.hideProgressDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", Utility.getStringFromPreference(mActivity, Constant.KEY_USER_ID));
//                params.put("nick_name", mNicknameText.getText().toString());
//                params.put("address", mAddressText.getText().toString());
                params.put("latitude", (mnewAddressLatlng != null ? ""+mnewAddressLatlng.latitude : ""));
                params.put("longitude", (mnewAddressLatlng != null ? ""+mnewAddressLatlng.longitude : ""));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        stringRequest.setTag(REQUEST_TAG);
        requestQueue.add(stringRequest);
    }

//    private boolean validateNickName() {
//        if (mNicknameText.getText().toString().trim().isEmpty()) {
//            mNicknameTextLayout.setError(getString(R.string.err_msg_nickname));
//            requestFocus(mNicknameText);
//            return false;
//        } else {
//            mNicknameTextLayout.setErrorEnabled(false);
//        }
//
//        return true;
//    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public LatLng getLatLongFromPlace(String place) {
        LatLng placeLatLng = null;
        try {
            Geocoder selected_place_geocoder = new Geocoder(mActivity);
            List<Address> address;

            address = selected_place_geocoder.getFromLocationName(place, 5);

            if (address == null) {

            } else {
                Address location = address.get(0);
                mLatitute = location.getLatitude();
                mLongitude = location.getLongitude();
                placeLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
//            mActivity.showToastMsg(mActivity, "Geocoder LatLang :" + mLatitute + "," + mLongitude);

        } catch (Exception e) {
            e.printStackTrace();
            FetchLatLongFromService fetch_latlng_from_service_abc = new FetchLatLongFromService(
                    place.replaceAll("\\s+", ""));
            fetch_latlng_from_service_abc.execute();

        }
        return placeLatLng;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        Toast.makeText(mActivity, "onConnected", Toast.LENGTH_SHORT).show();

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            String address = getCompleteAddressString(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(address);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home));
            currLocationMarker = googleMap.addMarker(markerOptions);

            markerPoints.add(latLng);

            moveCameraToPosition(latLng);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        catch(Exception ex){
            Toast.makeText(mActivity, "onConnected requestLocationUpdates Exception :" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(mActivity,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        //mGoogleMap.clear();
//        Toast.makeText(mActivity,"onLocationChanged Moving Camera",Toast.LENGTH_SHORT).show();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getString(R.string.my_location));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = googleMap.addMarker(markerOptions);

//        currLocationMarker.setDraggable(true);

//        if(mMyCurrentLocation == null)
//            moveCameraToPosition(latLng);
//        Toast.makeText(mActivity,"onLocationChanged Moving Camera",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        if(mMyCurrentLocation == null)
            mMyCurrentLocation = latLng;

        //If you only need one location, unregister the listener
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void moveCameraToPosition(LatLng latLng){
        if(latLng != null) {
            //zoom to current position:
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(14).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mActivity,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if(mMyCurrentLocation != null) {
//            Toast.makeText(mActivity,"onMyLocationButtonClick :" + mMyCurrentLocation.latitude +","+mMyCurrentLocation.longitude,
//                    Toast.LENGTH_SHORT).show();
            moveCameraToPosition(mMyCurrentLocation);
        }
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        moveCameraToPosition(marker.getPosition());
    }

    //Sometimes happens that device gives location = null

    public class FetchLatLongFromService extends
            AsyncTask<Void, Void, StringBuilder> {
        String place;


        public FetchLatLongFromService(String place) {
            super();
            this.place = place;
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                String googleMapUrl = Constant.URL_GET_LOCATION_ADDRESS
                        + this.place + Constant.SENSOR_END;

                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                String a = "";
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if(result != null && result.equals("")) {
                    JSONObject jsonObj = new JSONObject(result.toString());
                    JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                    // Extract the Place descriptions from the results
                    // resultList = new ArrayList<String>(resultJsonArray.length());

                    JSONObject before_geometry_jsonObj = resultJsonArray
                            .getJSONObject(0);

                    JSONObject geometry_jsonObj = before_geometry_jsonObj
                            .getJSONObject("geometry");

                    JSONObject location_jsonObj = geometry_jsonObj
                            .getJSONObject("location");

                    String lat_helper = location_jsonObj.getString("lat");
                    mLatitute = Double.valueOf(lat_helper);


                    String lng_helper = location_jsonObj.getString("lng");
                    mLongitude = Double.valueOf(lng_helper);


                    LatLng point = new LatLng(mLatitute, mLongitude);

//                    mActivity.showToastMsg(mActivity, "Google LatLang :" + mLatitute + "," + mLongitude);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("TAG", "Exception while downloading " + e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mActivity.showProgressDialog();
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            mActivity.hideProgressDialog();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
        }
    }

}
