package com.android.chickenbuzz.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.activity.AboutMeActivity;
import com.android.chickenbuzz.activity.BaseActivity;
import com.android.chickenbuzz.activity.FollowYouActivity;
import com.android.chickenbuzz.beans.UserDetails;
import com.android.chickenbuzz.componenets.VolleyMultipartRequest;
import com.android.chickenbuzz.componenets.VolleySingleton;
import com.android.chickenbuzz.global.ChickenBuzzApplication;
import com.android.chickenbuzz.utils.Constant;
import com.android.chickenbuzz.utils.NetworkUtils;
import com.android.chickenbuzz.utils.Utility;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by krelluru on 8/28/2016.
 */

public class AboutMeFragment extends Fragment implements View.OnFocusChangeListener,View.OnClickListener{

    private Activity mActivity;

    private DatePickerDialog aboutmeDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText inputfirstname, inputlastname,inputdate_of_birth,inputHeight,inputAlternative_phno,inputEmail, input_phno;
    private TextInputLayout inputLayoutFirstName,inputLayoutLastName,inputLayoutDOB, inputLayoutheight,inputLayoutAlternativePhno,inputLayoutEmail,inputLayoutPhno ;
    private ImageView profilepic;
    private AppCompatButton savebutton, resetButton;
    private Spinner genderspinner;
    private String gender;

    public static final String REQUEST_TAG = "AboutMeDetails";
    private String aboutMeUpdateUrl = Constant.URL_UPDATE_ABOUTME_DETAILS;
    private static final int MY_PERMISSIONS_REQUESTCAMERA = 100;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 101;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private UserDetails mUserDetailsBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateFormatter = new SimpleDateFormat(Constant.NORMAL_DATE_FORMAT, Locale.US);

        mActivity = getActivity();
    }

    public static AboutMeFragment newInstance(int someInt, String someTitle) {
        AboutMeFragment fragmentDemo = new AboutMeFragment();
        /*Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        args.putString("someTitle", someTitle);
        fragmentDemo.setArguments(args);*/
        return fragmentDemo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_aboutme, container, false);

        intializeViewsById(view);
        intializeListeners();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputdate_of_birth.setInputType(InputType.TYPE_NULL);
        inputdate_of_birth.setOnFocusChangeListener(this);
        savebutton.setOnClickListener(this);
        selectGender();
    }

    private void intializeViewsById(View view) {
        inputLayoutFirstName = (TextInputLayout) view.findViewById(R.id.input_layout_first_name);
        inputLayoutLastName = (TextInputLayout) view.findViewById(R.id.input_layout_lastname);
        inputLayoutheight = (TextInputLayout) view.findViewById(R.id.input_layout_height);
        inputLayoutAlternativePhno = (TextInputLayout) view.findViewById(R.id.input_layout_alternative_phno);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutPhno = (TextInputLayout) view.findViewById(R.id.input_layout_phno);

        genderspinner = (Spinner) view.findViewById(R.id.spinner_gender);
        inputdate_of_birth = (EditText) view.findViewById(R.id.editext_dob);
        inputfirstname = (EditText) view.findViewById(R.id.editext_first_name);
        inputlastname = (EditText) view.findViewById(R.id.editext_lastname);
        inputHeight = (EditText) view.findViewById(R.id.editext_height);
        inputAlternative_phno = (EditText) view.findViewById(R.id.editext_alternative_phno);
        inputEmail = (EditText) view.findViewById(R.id.editext_email);
        input_phno = (EditText) view.findViewById(R.id.editext_phno);
        profilepic = (ImageView) view.findViewById(R.id.imageview_profilepic);
        savebutton = (AppCompatButton) view.findViewById(R.id.btn_reg_register);
        resetButton = (AppCompatButton) view.findViewById(R.id.btn_reset);

        mUserDetailsBean = ((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails();

        if(mUserDetailsBean != null){
            setUserDataToControls();
        }
        else {
            inputEmail.setText(Utility.getStringFromPreference(mActivity, Constant.KEY_EMAIL));
            input_phno.setText(Utility.getStringFromPreference(mActivity, Constant.KEY_MOBILE_NO));
        }
        inputEmail.setFocusable(false);
        inputEmail.setEnabled(false);
        input_phno.setFocusable(false);
        input_phno.setEnabled(false);
    }

    private void setUserDataToControls() {
        inputfirstname.setText(mUserDetailsBean.getFirst_name());
        inputlastname.setText(mUserDetailsBean.getLast_name());
        inputdate_of_birth.setText(mUserDetailsBean.getDob());
        if(mUserDetailsBean.getGender().equalsIgnoreCase(getString(R.string.gender_male))){
            genderspinner.setSelection(1);
        }
        else if(mUserDetailsBean.getGender().equalsIgnoreCase(getString(R.string.gender_female))){
            genderspinner.setSelection(2);
        }
        else if(mUserDetailsBean.getGender().equalsIgnoreCase(getString(R.string.gender_others))){
            genderspinner.setSelection(3);
        }
        else
            genderspinner.setSelection(0);

        inputHeight.setText(mUserDetailsBean.getHeight());
        inputAlternative_phno.setText(mUserDetailsBean.getAlternate_number());
        inputEmail.setText(mUserDetailsBean.getEmail());
        input_phno.setText(mUserDetailsBean.getMobile_no());
    }

    private void intializeListeners() {
        inputfirstname.addTextChangedListener(new AboutMeTextWatcher(inputfirstname));
        inputlastname.addTextChangedListener(new AboutMeTextWatcher(inputlastname));
        inputHeight.addTextChangedListener(new AboutMeTextWatcher(inputHeight));
        inputAlternative_phno.addTextChangedListener(new AboutMeTextWatcher(inputAlternative_phno));
        inputdate_of_birth.setOnClickListener(this);

//        inputEmail.addTextChangedListener(new AboutMeTextWatcher(inputEmail));
//        input_phno.addTextChangedListener(new AboutMeTextWatcher(input_phno));

//        checkBox.setOnCheckedChangeListener(this);
        savebutton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        profilepic.setOnClickListener(this);
    }

    private void selectGender(){
        genderspinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((BaseActivity)mActivity).dismissKeyboard();
                return false;
            }
        });
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                ((BaseActivity)mActivity).dismissKeyboard();
                // On selecting a spinner item
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        if(!inputdate_of_birth.getText().toString().equals("")){
            Date userdate = Utility.convertStringToDate(inputdate_of_birth.getText().toString());
            newCalendar.setTime(userdate);
        }
        aboutmeDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -13);

                if(minAdultAge.before(userAge)){
                    ((BaseActivity) mActivity).showToastMsg(mActivity, getString(R.string.err_msg__adult_dob));
//                    inputdate_of_birth.setText("");
                }
                else {
                    newDate.set(year, monthOfYear, dayOfMonth);
                    inputdate_of_birth.setText(dateFormatter.format(newDate.getTime()));
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    /*public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }*/

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            ((BaseActivity)mActivity).dismissKeyboard();
            setDateTimeField();
            aboutmeDatePickerDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg_register:
                submitAboutMe();
//                NetworkUtils.showAlertDialog(getActivity(),"Waiting for Webservice to Upload.");
                break;
            case R.id.imageview_profilepic:
                    selectImage();
                break;
            case R.id.editext_dob:
                ((BaseActivity) mActivity).dismissKeyboard();
                setDateTimeField();
                aboutmeDatePickerDialog.show();
                break;
            case R.id.btn_reset:
                clearAllFeilds();
                break;
        }
    }

    private void clearAllFeilds() {
        inputfirstname.setText("");
        inputlastname.setText("");
        genderspinner.setSelection(0);
        inputdate_of_birth.setText("");
        inputHeight.setText("");
        inputAlternative_phno.setText("");
    }

    private boolean checkRuntimePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUESTCAMERA);
            return false;
        }
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

            return false;
        }
        else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(getString(R.string.take_photo)))
                        cameraIntent();
                    else if(userChoosenTask.equals(getString(R.string.choose_from_gallery)))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.choose_from_gallery),
                getString(R.string.Cancel) };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.add_photo_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.take_photo))) {
                    boolean result=Utility.checkPermission(mActivity, getString(R.string.camera));

                    userChoosenTask =getString(R.string.take_photo);
                    if(result)
                        cameraIntent();
                    else
                        ((BaseActivity)mActivity).showToastMsg(mActivity, getString(R.string.err_msg_permission_camera));

                } else if (items[item].equals(getString(R.string.take_photo))) {
                    boolean result=Utility.checkPermission(mActivity, "Storage");
                    userChoosenTask =getString(R.string.choose_from_gallery);
                    if(result)
                        galleryIntent();
                    else
                        ((BaseActivity)mActivity).showToastMsg(mActivity, getString(R.string.err_msg_permission_write_storage));

                } else if (items[item].equals(getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = Bitmap.createScaledBitmap(thumbnail, 100, 100, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profilepic.setImageBitmap(Utility.getCircularBitmap(thumbnail));
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                bm = Bitmap.createScaledBitmap(bm, 100, 100, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profilepic.setImageBitmap(Utility.getCircularBitmap(bm));
    }

    private void submitAboutMe() {
        if(!validateFirstLastName())
            return;
        if(!validateLastName())
            return;
        if(genderspinner.getSelectedItemPosition() == 0){
            ((BaseActivity) mActivity).showToastMsg(mActivity, getString(R.string.err_msg_gender));
            return;
        }
        if(TextUtils.isEmpty(inputdate_of_birth.getText().toString())) {
            ((BaseActivity) mActivity).showToastMsg(mActivity, getString(R.string.err_msg_dob));
            return;
        }
        if(!validateHeight())
            return;
        if (NetworkUtils.checkInternetConnectivity(mActivity)) {
            ((BaseActivity) mActivity).dismissKeyboard();
            ((BaseActivity) mActivity).showProgressDialog();
            sendAboutMeDetails();
        }
        else {
            NetworkUtils.showAlertDialog(mActivity, getString(R.string.check_your_network_connection));
        }
    }

    private void sendAboutMeDetails() {

//        new callAboutUpdateAsyncTask().execute();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.URL_UPDATE_ABOUTME_DETAILS, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                ((BaseActivity) mActivity).hideProgressDialog();
                String resultResponse = new String(response.data);
                try {
//                    ((BaseActivity)mActivity).showToastMsg(mActivity, "Response from Update :" + resultResponse);
                    JSONObject responseobject = new JSONObject(resultResponse);

                    if(responseobject != null) {
                        if (responseobject.has("response_code") &&
                                responseobject.getString("response_code").equalsIgnoreCase("0")) {
                            NetworkUtils.showAlertDialog(mActivity,
                                    ((responseobject.has("Error") ? responseobject.getString("Error") :
                                            getString(R.string.err_msg_mandatory_fields_not_filled))));
                        } else if (responseobject.has("json_data")){
                            JSONObject message = (JSONObject) responseobject.get("json_data");
                            if(message.has("message")) {
                                if (message.getString("message").equalsIgnoreCase("Success")) {
                                    ((BaseActivity)mActivity).showToastMsg(mActivity, getString(R.string.your_details_updated_succesfully));
                                    if(mActivity instanceof AboutMeActivity) {
                                        mActivity.finish();
                                        Intent intent = new Intent(mActivity, FollowYouActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                             }
                                }
                                else{
                                    NetworkUtils.showAlertDialog(mActivity, message.getString("message"));
                                }
                            }
                            else{
                                NetworkUtils.showAlertDialog(mActivity,getString(R.string.error_unable_to_get_response));
                            }
                        }
                        else{
                            NetworkUtils.showAlertDialog(mActivity, getString(R.string.error_unable_to_get_response));
                        }
                    }
                    else{
                        NetworkUtils.showAlertDialog(mActivity, getString(R.string.error_unable_to_get_response));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    NetworkUtils.showAlertDialog(mActivity, "Exception :"+e.getMessage() +" : " +getString(R.string.error_unable_to_get_response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) mActivity).hideProgressDialog();
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    ((BaseActivity)mActivity).showToastMsg(mActivity, "onErrorResponse from Update :" + result);
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Utility.getStringFromPreference(mActivity, Constant.KEY_USER_ID));
                params.put("isd_code", Utility.getStringFromPreference(mActivity, Constant.KEY_ISD_CODE));
                params.put("mobile_no", Utility.getStringFromPreference(mActivity, Constant.KEY_MOBILE_NO));
                params.put("alternate_number", inputAlternative_phno.getText().toString());
                params.put("first_name", inputfirstname.getText().toString());
                params.put("last_name", inputlastname.getText().toString());
                params.put("gender", gender);
                params.put("dob", inputdate_of_birth.getText().toString());
                params.put("height", inputHeight.getText().toString());
                params.put("email", Utility.getStringFromPreference(mActivity, Constant.KEY_EMAIL));
//                params.put("lat", Utility.getStringFromPreference(mActivity, Constant.KEY_CURRENT_LOCATION_LATITUDE));
//                params.put("long", Utility.getStringFromPreference(mActivity, Constant.KEY_CURRENT_LOCATION_LONGITUDE));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("photo", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(mActivity, profilepic.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        VolleySingleton.getInstance(mActivity).addToRequestQueue(multipartRequest);
    }

    private class AboutMeTextWatcher implements TextWatcher {

        private View view;

        private AboutMeTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.editext_first_name:
                    validateFirstLastName();
                    break;
                case R.id.editext_lastname:
                    validateFirstLastName();
                    break;
                case R.id.editext_height:
                    validateHeight();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private boolean validatePhoneNumber() {
        if (inputAlternative_phno.getText()!=null && inputAlternative_phno.getText().toString().trim().isEmpty()) {
            requestFocus(inputAlternative_phno);
            return false;
        }
        return true;
    }

    private boolean validateFirstLastName() {
        if (inputfirstname.getText().toString().trim().isEmpty()) {
            inputLayoutFirstName.setError(getString(R.string.err_msg_first_name));
            requestFocus(inputLayoutFirstName);
            return false;
        }
        else if(inputfirstname.getText().toString().length() >100){
            inputLayoutFirstName.setError(getString(R.string.err_first_name_toomuch_long));
            requestFocus(inputLayoutFirstName);
            return false;
        }
        else {
            inputLayoutFirstName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (inputlastname.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(getString(R.string.err_msg_last_name));
            requestFocus(inputLayoutLastName);
            return false;
        }
        else if(inputlastname.getText().toString().length() >100){
            inputLayoutLastName.setError(getString(R.string.err_first_name_toomuch_long));
            requestFocus(inputLayoutLastName);
            return false;
        }
        else {
            inputLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateHeight() {
        if (inputHeight.getText().toString().trim().isEmpty()) {
            inputLayoutheight.setError(getString(R.string.err_msg_height));
            requestFocus(inputLayoutheight);
            return false;
        }
        else if(inputHeight.getText().toString().indexOf(".") != -1 &&
                inputHeight.getText().toString().substring(inputHeight.getText().toString().indexOf(".")+1).length() > 2){
            inputLayoutheight.setError(getString(R.string.err_first_name_toomuch_long));
            requestFocus(inputLayoutheight);
            return false;
        }
        else {
            inputLayoutheight.setErrorEnabled(false);
        }

        return true;
    }
}
