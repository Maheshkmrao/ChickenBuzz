package com.android.chickenbuzz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.componenets.SmoothCheckBox;
import com.android.chickenbuzz.utils.Constant;
import com.android.chickenbuzz.utils.NetworkUtils;
import com.android.chickenbuzz.utils.Utility;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krelluru on 8/26/2016.
 */

public class RegistrationActivity extends BaseActivity implements View.OnClickListener,SmoothCheckBox.OnCheckedChangeListener{

    private EditText inputPhoneNumber, inputVerificationcode,inputpin,inputconfirmPin,inputemergencyPin,inputconfirmEmergencyPin;
    private TextInputLayout inputLayoutVerificationcode, inputLayoutPin, inputLayoutPhoneNumber,inputLayoutConfirmPin,inputLayoutEmergencyPin,inputLayoutConfirmEmergencyPin;
    private Button btnregister;
//    private SmoothCheckBox checkBox;

    public static final String REQUEST_TAG = "RegistrationActivity";
    private String registerUrl = Constant.REGISTER_USER;
    private String androidDeviceId;

    private String inter_phoneNumber,registerpin,emergencyPin,invitationnCode,verification_code,isd_code;
//    boolean checkbox_toggle=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(getString(R.string.title_registration));
        intilizeRegistrationUI();
        intializeTextChange();
        getIntentData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    private void getIntentData(){
        try {
            androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            inter_phoneNumber = getIntent().getExtras().getString("MOBILE");
            isd_code = getIntent().getExtras().getString("ISDCODE");
            verification_code=getIntent().getExtras().getString("VERIFICATIONCODE");

//            showToastMsg(RegistrationActivity.this, verification_code);

            if(inter_phoneNumber!=null && verification_code!=null ) {
                inputPhoneNumber.setText(inter_phoneNumber);
                inputPhoneNumber.setEnabled(false);
//                inputVerificationcode.setText(verification_code);
//                inputVerificationcode.setEnabled(false);
            }

        }catch (Exception e){
            Log.v("Device-details", e.getMessage());
        }
    }

    private void intilizeRegistrationUI() {
        inputLayoutPhoneNumber = (TextInputLayout) findViewById(R.id.input_layout_reg_phonenumber);
        inputLayoutVerificationcode = (TextInputLayout) findViewById(R.id.input_layout_reg_smsverificationcode);
        inputLayoutPin = (TextInputLayout) findViewById(R.id.input_layout_reg_pin);
        inputLayoutConfirmPin = (TextInputLayout) findViewById(R.id.input_layout_reg_confirm_pin);
        inputLayoutEmergencyPin = (TextInputLayout) findViewById(R.id.input_layout_reg_emergency_pin);
        inputLayoutConfirmEmergencyPin = (TextInputLayout) findViewById(R.id.input_layout_reg_confirm_emergency_pin);

        inputPhoneNumber = (EditText) findViewById(R.id.editext_reg_phonenumber);
        inputVerificationcode = (EditText) findViewById(R.id.editext_reg_smsverificationcode);
        inputpin = (EditText) findViewById(R.id.editext_reg_pin);
        inputconfirmPin = (EditText) findViewById(R.id.editext_reg_confirm_pin);
        inputemergencyPin = (EditText) findViewById(R.id.editext_reg_emergency_pin);
        inputconfirmEmergencyPin = (EditText) findViewById(R.id.editext_reg_confirm_emergency_pin);

//        checkBox=(SmoothCheckBox) findViewById(R.id.smoothcheckbox_accept_terms);
        btnregister = (Button) findViewById(R.id.btn_reg_register);
    }

    private void intializeTextChange() {
        inputPhoneNumber.addTextChangedListener(new RegistrationTextWatcher(inputPhoneNumber));
        inputVerificationcode.addTextChangedListener(new RegistrationTextWatcher(inputVerificationcode));
        inputpin.addTextChangedListener(new RegistrationTextWatcher(inputpin));
        inputconfirmPin.addTextChangedListener(new RegistrationTextWatcher(inputconfirmPin));
        inputemergencyPin.addTextChangedListener(new RegistrationTextWatcher(inputemergencyPin));
        inputconfirmEmergencyPin.addTextChangedListener(new RegistrationTextWatcher(inputconfirmEmergencyPin));

//        checkBox.setOnCheckedChangeListener(this);
        btnregister.setOnClickListener(this);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg_register:
                dismissKeyboard();
                submitRegister();
                break;
        }
    }

    private void submitRegister() {
        if (!validatePhoneNumber()) {
            return;
        }if (!validateSMSVerificationCode()) {
            return;
        }if (!validateRegisterPin()) {
            return;
        }if (!validateConfirmRegisterPin()) {
            return;
        }if (!validateEmergencyPin()) {
            return;
        }if (!validateConfirmEmergencyPin()) {
            return;
        }/*if(!checkbox_toggle){
            return;
        }*/
        if (NetworkUtils.checkInternetConnectivity(this)) {
            if (isd_code != null && inter_phoneNumber != null && verification_code != null
                    && registerpin != null && emergencyPin != null && androidDeviceId != null) {
                showProgressDialog();
                registerUser();
            }else{
                NetworkUtils.showAlertDialog(RegistrationActivity.this, getString(R.string.err_msg_mandatory_fields_not_filled));
            }
        } else {
            NetworkUtils.showAlertDialog(this, getString(R.string.check_your_network_connection));
        }
    }

    private void startMainActivity(){
        finish();
        Intent intent = new Intent(RegistrationActivity.this, InteractionActivity.class);
        startActivity(intent);
    }
    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        Log.d("SmoothCheckBox", String.valueOf(isChecked));
//        if(isChecked){
//            checkbox_toggle=true;
//        }else{
//            checkbox_toggle=false;
//            NetworkUtils.showAlertDialog(this,"Check Accept & Terms and Conditions");
//        }
    }

    private class RegistrationTextWatcher implements TextWatcher {

        private View view;

        private RegistrationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editext_reg_phonenumber:
                    validatePhoneNumber();
                    break;
                case R.id.editext_reg_smsverificationcode:
                    validateSMSVerificationCode();
                    break;
                case R.id.editext_reg_pin:
                    validateRegisterPin();
                    break;
                case R.id.editext_reg_confirm_pin:
                    validateConfirmRegisterPin();
                    break;
                case R.id.editext_reg_emergency_pin:
                    validateEmergencyPin();
                    break;
                case R.id.editext_reg_confirm_emergency_pin:
                    validateConfirmEmergencyPin();
                    break;
            }
        }
    }

    private boolean validatePhoneNumber() {
        if (inputPhoneNumber.getText().toString().trim().isEmpty()) {
            inputLayoutPhoneNumber.setError(getString(R.string.err_msg_phonenumber));
            requestFocus(inputPhoneNumber);
            return false;
        } else {
            inputLayoutPhoneNumber.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSMSVerificationCode() {
        if (inputVerificationcode.getText().toString().trim().isEmpty()) {
            inputLayoutVerificationcode.setError(getString(R.string.err_msg_smsverfication_pin));
            requestFocus(inputVerificationcode);
            return false;
        }
        else if(!inputVerificationcode.getText().toString().equalsIgnoreCase(verification_code)){
            inputLayoutVerificationcode.setError(getString(R.string.err_msg_smsverfication_pin));
            requestFocus(inputVerificationcode);
            return false;
        }
        else {
            inputLayoutVerificationcode.setErrorEnabled(false);
        }

        return true;
    }

     private boolean validateRegisterPin() {
        if (inputpin.getText().toString().trim().isEmpty()) {
            inputLayoutPin.setError(getString(R.string.err_msg_register_pin));
            requestFocus(inputpin);
            return false;
        }
        else {
            registerpin=inputpin.getText().toString().trim();
            inputLayoutPin.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmRegisterPin() {
        if (inputconfirmPin.getText().toString().trim().isEmpty()) {
            inputLayoutConfirmPin.setError(getString(R.string.err_msg_confirm_register_pin));
            requestFocus(inputconfirmPin);
            return false;
        } else {
            if (TextUtils.equals(inputconfirmPin.getText(), inputpin.getText())) {
                inputLayoutConfirmPin.setErrorEnabled(false);
            } else {
                inputLayoutConfirmPin.setError(getString(R.string.err_msg_same_pin));
                inputLayoutConfirmPin.setErrorEnabled(true);
                requestFocus(inputconfirmPin);
                return false;
            }
        }

        return true;
    }

    private boolean validateEmergencyPin() {
        if (inputemergencyPin.getText().toString().trim().isEmpty()) {
            inputLayoutEmergencyPin.setError(getString(R.string.err_msg__emergency_pin));
            requestFocus(inputemergencyPin);
            return false;
        } else {
            emergencyPin=inputemergencyPin.getText().toString().trim();
            inputLayoutEmergencyPin.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmEmergencyPin() {
        if (inputconfirmEmergencyPin.getText().toString().trim().isEmpty()) {
            inputLayoutConfirmEmergencyPin.setError(getString(R.string.err_msg_confirm_emergency_pin));
            requestFocus(inputconfirmEmergencyPin);
            return false;
        } else {
            if (TextUtils.equals(inputemergencyPin.getText(), inputconfirmEmergencyPin.getText())) {
                inputLayoutConfirmEmergencyPin.setErrorEnabled(false);
            } else {
                inputLayoutConfirmEmergencyPin.setError(getString(R.string.err_msg_same_emergency_pin));
                inputLayoutConfirmEmergencyPin.setErrorEnabled(true);
                requestFocus(inputconfirmEmergencyPin);
                return false;
            }
        }
        return true;
    }

    private void registerUser(){

        showToastMsg(RegistrationActivity.this, "My Location:" + Utility.getStringFromPreference(RegistrationActivity.this, Constant.KEY_CURRENT_LOCATION_LATITUDE) +","+
                Utility.getStringFromPreference(RegistrationActivity.this, Constant.KEY_CURRENT_LOCATION_LONGITUDE));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hideProgressDialog();
                            JSONObject responseobject = new JSONObject(response);
                            if(responseobject != null) {
                                if (responseobject.has("reponse_code") &&
                                        responseobject.getString("reponse_code").equalsIgnoreCase("0")) {
                                    NetworkUtils.showAlertDialog(RegistrationActivity.this,
                                            ((responseobject.has("Error") ? responseobject.getString("Error") :
                                                    getString(R.string.err_msg_mandatory_fields_not_filled))));
                                } else if (responseobject.has("json_data")){
                                    JSONObject message = (JSONObject) responseobject.get("json_data");
                                    if(message.has("message")) {
                                        if (message.getString("message").equalsIgnoreCase("Success")) {
                                            Utility.saveStringPreference(RegistrationActivity.this, Constant.KEY_USER_ID,
                                                    (message.has("user_id") ? message.getString("user_id") : ""));
                                            startMainActivity();
                                        }
                                        else{
                                            NetworkUtils.showAlertDialog(RegistrationActivity.this, message.getString("message"));
                                        }
                                    }
                                    else{
                                        NetworkUtils.showAlertDialog(RegistrationActivity.this,getString(R.string.error_unable_to_get_response));
                                    }
                                }
                                else{
                                    NetworkUtils.showAlertDialog(RegistrationActivity.this, getString(R.string.error_unable_to_get_response));
                                }
                            }
                            else{
                                NetworkUtils.showAlertDialog(RegistrationActivity.this, getString(R.string.error_unable_to_get_response));
                            }
                        } catch (Exception error) {
                            NetworkUtils.showAlertDialog(RegistrationActivity.this, getString(R.string.error_unable_to_get_response)
                                    + " due to :"+error.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkUtils.showAlertDialog(RegistrationActivity.this,error.getMessage());
                        hideProgressDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("isd_code", isd_code);
                params.put("mobile_no", inter_phoneNumber);
                params.put("verification_code", verification_code);
                params.put("pin", registerpin);
                params.put("emergency_pin", emergencyPin);
                params.put("device_token", androidDeviceId);
                params.put("device_type", "1");
                params.put("mac_address", Utility.getMacAddr());
                params.put("lat", Utility.getStringFromPreference(RegistrationActivity.this, Constant.KEY_CURRENT_LOCATION_LATITUDE));
                params.put("long", Utility.getStringFromPreference(RegistrationActivity.this, Constant.KEY_CURRENT_LOCATION_LONGITUDE));

                return params;
            }

        };

//        showToastMsg(RegistrationActivity.this, "Request :" + isd_code+ ","+inter_phoneNumber);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(REQUEST_TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
