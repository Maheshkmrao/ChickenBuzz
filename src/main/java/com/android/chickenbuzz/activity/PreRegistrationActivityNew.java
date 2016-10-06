package com.android.chickenbuzz.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.beans.Country;
import com.android.chickenbuzz.componenets.CountriesFetcher;
import com.android.chickenbuzz.componenets.CountrySpinnerAdapter;
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

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by krelluru on 8/26/2016.
 */

public class PreRegistrationActivityNew extends BaseActivity implements View.OnClickListener, SmoothCheckBox.OnCheckedChangeListener,
        IntlPhoneInput.IntlPhoneInputListener{
//Response.Listener,Response.ErrorListener

    private PreRegistrationActivityNew mActivity;

    private EditText /*inputPhoneNo,*/ inputEmail, inputInvitationCode;
    private SmoothCheckBox checkBox;
    private TextInputLayout inputLayoutPhoneNo, inputLayoutInvitationCode, inputLayoutEmail ;
    private ImageButton btnSMScode;
    private TextView mTcAcceptTextview;

//    private IntlPhoneInput phoneInputView;
    private String internationalphoneNumber,countrycode,invitationnCode,emailAddress;

    public static final String REQUEST_TAG = "PreRegistrationActivity";
    private String authenticateUrl = Constant.AUTHENTICATE_USER;
    private String verification_code;
    boolean checkbox_toggle=false;
    boolean isTCAccepted;

    private final String DEFAULT_COUNTRY = Locale.getDefault().getCountry();
    private Spinner mCountrySpinner;
    private EditText mPhoneEdit;
    private CountrySpinnerAdapter mCountrySpinnerAdapter;
    private Country mSelectedCountry;
    private int mDefaultCountryPosition;
    private CountriesFetcher.CountryList mCountries;
    private AdapterView.OnItemSelectedListener mCountrySpinnerListener;
    private PhoneNumberWatcher mPhoneNumberWatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_preregistrationnew);

            mActivity = this;

            setTitle(getString(R.string.title_pre_registration));
            intilizeUI();
            intializeTextChange();

//            showAlertDialogWithListview();
        }
        catch(Exception ex){
            showToastMsg(PreRegistrationActivityNew.this, "Exception :" + ex.getMessage());
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_sms_code:
                submitSMSCode();
                break;
            case R.id.tc_text_button:
                Intent intent = new Intent(PreRegistrationActivityNew.this, TermsConditionsActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public void done(View view, boolean isValid) {
        if(isValid) {
//            internationalphoneNumber = String.valueOf(phoneInputView.getPhoneNumber().getNationalNumber());
//            internationalphoneNumber = String.valueOf(phoneInputView.getNumber());
            internationalphoneNumber = getCorrectPhoneNumber(mPhoneEdit.getText().toString());
            countrycode = ""+mSelectedCountry.getDialCode()/*String.valueOf("+"+phoneInputView.getSelectedCountry().getDialCode())*/;
        }
    }

    private void intilizeUI() {
        inputLayoutPhoneNo = (TextInputLayout) findViewById(R.id.input_layout_phoneno);
        inputLayoutInvitationCode = (TextInputLayout) findViewById(R.id.input_layout_invitationcode);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        phoneInputView = (IntlPhoneInput)findViewById(R.id.intlphone_input_phonenumber);
//        inputPhoneNo = (EditText) findViewById(R.id.editext_phoneno);
        inputInvitationCode = (EditText) findViewById(R.id.editext_invitationcode);
        inputInvitationCode = (EditText) findViewById(R.id.editext_invitationcode);
        inputEmail = (EditText) findViewById(R.id.editext_email);
        checkBox = (SmoothCheckBox) findViewById(R.id.smoothcheckbox_accept_terms);
        mTcAcceptTextview = (TextView) findViewById(R.id.tc_text_button);

        this.mCountrySpinnerListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCountry = mCountrySpinnerAdapter.getItem(position);
                mPhoneNumberWatcher = new PhoneNumberWatcher(mSelectedCountry.getIso());
                setHint();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        init();

        btnSMScode = (ImageButton) findViewById(R.id.btn_send_sms_code);
    }

    private void intializeTextChange() {
        inputInvitationCode.addTextChangedListener(new PreRegistrationTextWatcher(inputInvitationCode));
        inputEmail.addTextChangedListener(new PreRegistrationTextWatcher(inputEmail));
//        inputPhoneNo.addTextChangedListener(new PreRegistrationTextWatcher(inputPhoneNo));
        checkBox.setOnCheckedChangeListener(this);
        mTcAcceptTextview.setOnClickListener(this);

//        phoneInputView.setOnValidityChange(this);
//        phoneInputView.setOnKeyboardDone(this);
        btnSMScode.setOnClickListener(this);
    }

    private void init() {
        this.mPhoneNumberWatcher = new PhoneNumberWatcher(this.DEFAULT_COUNTRY);
        this.mCountrySpinner = (Spinner)this.findViewById(net.rimoto.intlphoneinput.R.id.intl_phone_edit__country);
        this.mCountrySpinnerAdapter = new CountrySpinnerAdapter(mActivity);
        this.mCountrySpinner.setAdapter(this.mCountrySpinnerAdapter);
        this.mCountries = CountriesFetcher.getCountries(mActivity);
        mDefaultCountryPosition = getDefaultCountry();
        this.mCountrySpinnerAdapter.addAll(mCountries);
        this.mCountrySpinner.setOnItemSelectedListener(this.mCountrySpinnerListener);
        this.mPhoneEdit = (EditText)this.findViewById(net.rimoto.intlphoneinput.R.id.intl_phone_edit__phone);
        this.mPhoneEdit.addTextChangedListener(mPhoneNumberWatcher);

        this.mCountrySpinner.setSelection(mDefaultCountryPosition);

        this.setDefault();
    }

    private int getDefaultCountry() {
        int defaultCountry = 0;

        if(mCountries != null){
            for(int count=0; count<mCountries.size(); count++){
                Country country = mCountries.get(count);
                if(country.getIso().equalsIgnoreCase("IN")){
                    defaultCountry = count;
                    return defaultCountry;
                }
            }
        }
        return defaultCountry;
    }

    private void setHint() {
        if(this.mPhoneEdit != null && this.mSelectedCountry != null && this.mSelectedCountry.getIso() != null) {
            this.mPhoneEdit.setHint(""+this.mSelectedCountry.getDialCode());
        }
    }

    public void setDefault() {
        try {
            TelephonyManager e = (TelephonyManager)mActivity.getSystemService(Context.TELEPHONY_SERVICE);
            String phone = e.getLine1Number();
            if(phone != null && !phone.isEmpty()) {
                this.mPhoneEdit.setHint(phone);
            } else {
                String iso = e.getNetworkCountryIso();
//                this.setEmptyDefault(iso);
                this.mPhoneEdit.setHint(iso);
            }
        } catch (SecurityException var4) {
//            this.setEmptyDefault();
        }

    }

    private class PhoneNumberWatcher extends PhoneNumberFormattingTextWatcher {
        private boolean lastValidity;

        public PhoneNumberWatcher() {
        }

        @TargetApi(21)
        public PhoneNumberWatcher(String countryCode) {
            super(countryCode);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);

            try {
                String validity = null;
                if(mSelectedCountry != null) {
                    validity = mSelectedCountry.getIso();
                }

                if(validity != null) {
                    int countryIdx = mCountries.indexOfIso(validity);
                    mCountrySpinner.setSelection(countryIdx);
                }
            } catch (Exception var8) {
                ;
            }

//            if(mIntlPhoneInputListener != null) {
//                boolean validity1 = isValid();
//                if(validity1 != this.lastValidity) {
//                    mIntlPhoneInputListener.done(IntlPhoneInput.this, validity1);
//                }
//
//                this.lastValidity = validity1;
//            }

        }
    }

    private boolean validatePhoneNumber() {
//        if (phoneInputView.getText()!=null && phoneInputView.getText().toString().trim().isEmpty()) {
//            requestFocus(phoneInputView);
//            return false;
//        }

        String phone = mPhoneEdit.getText().toString().trim();

        if (phone.isEmpty() || /*phone.length() <14 || */!isValidPhone(phone) ) {
            inputLayoutPhoneNo.setError(getString(R.string.err_msg_phonenumber));
//            mPhoneEdit.setError(getString(R.string.err_msg_phonenumber));
            requestFocus(mPhoneEdit);
            return false;
        } else {
            inputLayoutPhoneNo.setErrorEnabled(false);
            internationalphoneNumber=phone;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
            emailAddress=email;
        }

        return true;
    }

    private boolean validateInvitationCode() {
        if (inputInvitationCode.getText().toString().trim().isEmpty()) {
            inputLayoutInvitationCode.setError(getString(R.string.err_msg_invitationcode));
            requestFocus(inputInvitationCode);
            return false;
        } else {
            inputLayoutInvitationCode.setErrorEnabled(false);
            invitationnCode=inputInvitationCode.getText().toString().trim();
        }

        return true;
    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submitSMSCode() {
        dismissKeyboard();
        showToastMsg(PreRegistrationActivityNew.this, "My Location:" + Utility.getStringFromPreference(PreRegistrationActivityNew.this, Constant.KEY_CURRENT_LOCATION_LATITUDE) +","+
                Utility.getStringFromPreference(PreRegistrationActivityNew.this, Constant.KEY_CURRENT_LOCATION_LONGITUDE));

        if (!validatePhoneNumber()) {
            return;
        }if (!validateEmail()) {
            return;
        }if (!validateInvitationCode()) {
            return;
        }if(!checkbox_toggle){
            showToastMsg(PreRegistrationActivityNew.this, getString(R.string.err_msg_accept_tc));
            return;
        }if (NetworkUtils.checkInternetConnectivity(this)) {
            internationalphoneNumber = getCorrectPhoneNumber(mPhoneEdit.getText().toString());
            countrycode = ""+mSelectedCountry.getDialCode()/*String.valueOf("+"+phoneInputView.getSelectedCountry().getDialCode())*/;
            if(countrycode!=null && internationalphoneNumber!=null && invitationnCode!=null && emailAddress!=null) {
//                internationalphoneNumber = String.valueOf(phoneInputView.getPhoneNumber().getNationalNumber());
//                internationalphoneNumber = String.valueOf(phoneInputView.getNumber());
//                countrycode=String.valueOf("+"+phoneInputView.getSelectedCountry().getDialCode());
                internationalphoneNumber = getCorrectPhoneNumber(mPhoneEdit.getText().toString());
                countrycode = ""+mSelectedCountry.getDialCode()/*String.valueOf("+"+phoneInputView.getSelectedCountry().getDialCode())*/;

//                showToastMsg(PreRegistrationActivityNew.this, "Phone No :"+ internationalphoneNumber);

                if(checkbox_toggle) {
                    showProgressDialog();
                    authenticateUser();
                }
                else NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this, getString(R.string.err_msg_accept_tc));
            }else{
                NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this, getString(R.string.err_msg_mandatory_fields_not_filled));
            }
        }else {
            NetworkUtils.showAlertDialog(this, getString(R.string.check_your_network_connection));
        }
    }

    private String getCorrectPhoneNumber(String s) {
        String phoneNo = s;
        if(s != null && !s.equalsIgnoreCase("")){
            if(s.indexOf("(")!=-1)
                phoneNo = phoneNo.replace("(", "");
            if(s.indexOf(")")!=-1)
                phoneNo = phoneNo.replace(")", "");
            if(s.indexOf("-")!=-1)
                phoneNo = phoneNo.replaceAll("-", "");
            if(s.indexOf(" ")!=-1)
                phoneNo = phoneNo.replaceAll(" ", "");
        }
        return phoneNo.trim();
    }

    private void startRegistrationActivity(){
        Intent intent = new Intent(PreRegistrationActivityNew.this, RegistrationActivity.class);
        intent.putExtra("ISDCODE",countrycode);
        intent.putExtra("MOBILE",internationalphoneNumber);
        intent.putExtra("VERIFICATIONCODE",verification_code);

        Utility.saveStringPreference(PreRegistrationActivityNew.this, Constant.KEY_ISD_CODE, countrycode);
        Utility.saveStringPreference(PreRegistrationActivityNew.this, Constant.KEY_MOBILE_NO, internationalphoneNumber);
        Utility.saveStringPreference(PreRegistrationActivityNew.this, Constant.KEY_EMAIL, emailAddress);

        startActivity(intent);
    }

    private void startInstractionActivity(){
        Utility.saveStringPreference(PreRegistrationActivityNew.this, Constant.KEY_ISD_CODE, countrycode);
        Utility.saveStringPreference(PreRegistrationActivityNew.this, Constant.KEY_MOBILE_NO, internationalphoneNumber);
        Utility.saveStringPreference(PreRegistrationActivityNew.this, Constant.KEY_EMAIL, emailAddress);

        Intent intent = new Intent(PreRegistrationActivityNew.this, InteractionActivity.class);
        startActivity(intent);
    }

    private void authenticateUser(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, authenticateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        showToastMsg(PreRegistrationActivityNew.this, "Authenticate-User-onResponse " + response);
                        try {
                            hideProgressDialog();

                            JSONObject responseobject = new JSONObject(response);
                            if(responseobject != null) {
                                if (responseobject.has("response_code") &&
                                        responseobject.getString("response_code").equalsIgnoreCase("0")) {
                                    NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this,
                                            ((responseobject.has("Error") ? responseobject.getString("Error") :
                                                    getString(R.string.err_msg_mandatory_fields_not_filled))));
                                }
                                else if (responseobject.has("json_data")){
                                    JSONObject message = (JSONObject) responseobject.get("json_data");
                                    if(message.has("message")) {
                                        if (message.getString("message").equalsIgnoreCase("Success")) {
                                            verification_code = message.getString("verification_code");
                                            finish();
                                            startRegistrationActivity();
                                        } else if (message.getString("message").equalsIgnoreCase("Already registered")) {
                                            finish();
                                            startInstractionActivity();
                                        }
                                        else {
                                            NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this,message.getString("message"));
                                        }
                                    }
                                    else{
                                        NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this, getString(R.string.error_unable_to_get_response));
                                    }
                                }
                                else{
                                    NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this, getString(R.string.error_unable_to_get_response));
                                }
                            }
                            else{
                                NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this, getString(R.string.error_unable_to_get_response));
                            }
                            /*
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(response);
                            if(rootNode.path("reponse_code").asText().contains("0")){
                                NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this,"Mandatory fields Not filled");
                            }else if (rootNode.path("json_data").path("message").asText().contains("Success")) {
                                verification_code=rootNode.path("json_data").path("verification_code").toString();
                                startRegistrationActivity();
                            }else if(rootNode.path("json_data").path("message").asText().contains("Already registered")){
                                Intent intent = new Intent(PreRegistrationActivityNew.this, InteractionActivity.class);
                                startActivity(intent);
                            }
                            else{
                                NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this,"Unable to connect to Server. Please try again later.");
                            }*/
                        } catch (Exception error) {
                            NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this,error.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this,error.getMessage());
                    }
                }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("isd_code", countrycode);
                    params.put("mobile_no", internationalphoneNumber);
                    params.put("invite_code", invitationnCode);
                    params.put("email", emailAddress);
                    params.put("lat", Utility.getStringFromPreference(PreRegistrationActivityNew.this, Constant.KEY_CURRENT_LOCATION_LATITUDE));
                    params.put("long", Utility.getStringFromPreference(PreRegistrationActivityNew.this, Constant.KEY_CURRENT_LOCATION_LONGITUDE));
                    return params;
                }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(REQUEST_TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        Log.d("SmoothCheckBox", String.valueOf(isChecked));
        PreRegistrationActivityNew.this.dismissKeyboard();
        if(isChecked){
            checkbox_toggle=true;
        }else{
            checkbox_toggle=false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            if(data != null && data.hasExtra("IS_ACCEPTED"))
                isTCAccepted = data.getBooleanExtra("IS_ACCEPTED", false);
            checkbox_toggle = isTCAccepted;
            if(!isTCAccepted)
                NetworkUtils.showAlertDialog(PreRegistrationActivityNew.this, getString(R.string.decline_message));

            checkBox.setChecked(isTCAccepted);
        }
    }

    private class PreRegistrationTextWatcher implements TextWatcher {

        private View view;

        private PreRegistrationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editext_email:
                    validateEmail();
                    break;
                case R.id.editext_invitationcode:
                    validateInvitationCode();
                    break;
            }
        }
    }

}
