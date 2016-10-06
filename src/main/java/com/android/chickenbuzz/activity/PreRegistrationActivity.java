package com.android.chickenbuzz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.utils.Constant;
import com.android.chickenbuzz.utils.CrashReportExceptionHandler;
import com.android.chickenbuzz.utils.NetworkUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krelluru on 8/26/2016.
 */

public class PreRegistrationActivity extends BaseActivity implements View.OnClickListener,
        IntlPhoneInput.IntlPhoneInputListener{

    protected CrashReportExceptionHandler mDamageReport;

//Response.Listener,Response.ErrorListener
    private EditText inputEmail, inputInvitationCode;
    private TextInputLayout inputLayoutInvitationCode, inputLayoutEmail ;
    private ImageButton btnSMScode;

    private IntlPhoneInput phoneInputView;
    private String internationalphoneNumber,countrycode,invitationnCode,emailAddress;

    public static final String REQUEST_TAG = "PreRegistrationActivity";
    private String authenticateUrl = Constant.AUTHENTICATE_USER;
    private String verification_code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {

            setContentView(R.layout.activity_preregistration);
            intilizeUI();
            intializeTextChange();
//        }
//        catch(Exception ex){
//            showToastMsg(PreRegistrationActivity.this, "Exception :" + ex.getMessage());
//        }
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
        }

    }

    @Override
    public void done(View view, boolean isValid) {
        if(isValid) {
//            internationalphoneNumber = String.valueOf(phoneInputView.getPhoneNumber().getNationalNumber());
            internationalphoneNumber = String.valueOf(phoneInputView.getNumber());
            countrycode=String.valueOf("+"+phoneInputView.getSelectedCountry().getDialCode());
        }
    }

    private void intilizeUI() {
        inputLayoutInvitationCode = (TextInputLayout) findViewById(R.id.input_layout_invitationcode);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        phoneInputView = (IntlPhoneInput)findViewById(R.id.intlphone_input_phonenumber);
        inputInvitationCode = (EditText) findViewById(R.id.editext_invitationcode);
        inputEmail = (EditText) findViewById(R.id.editext_email);
        btnSMScode = (ImageButton) findViewById(R.id.btn_send_sms_code);
    }

    private void intializeTextChange() {
        inputInvitationCode.addTextChangedListener(new PreRegistrationTextWatcher(inputInvitationCode));
        inputEmail.addTextChangedListener(new PreRegistrationTextWatcher(inputEmail));
        phoneInputView.setOnValidityChange(this);
        phoneInputView.setOnKeyboardDone(this);
        btnSMScode.setOnClickListener(this);
    }
    private boolean validatePhoneNumber() {
        if (phoneInputView.getText()!=null && phoneInputView.getText().toString().trim().isEmpty()) {
            requestFocus(phoneInputView);
            return false;
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submitSMSCode() {
        showToastMsg(PreRegistrationActivity.this, "Validation :");
        if (!validatePhoneNumber()) {
            showToastMsg(PreRegistrationActivity.this, "Phone Validation Failed:");
            return;
        }if (!validateEmail()) {
            return;
        }if (!validateInvitationCode()) {
            return;
        }if (NetworkUtils.checkInternetConnectivity(this)) {
            showToastMsg(PreRegistrationActivity.this, "Network is there.");
            if(countrycode!=null && internationalphoneNumber!=null && invitationnCode!=null && emailAddress!=null) {
//                internationalphoneNumber = String.valueOf(phoneInputView.getPhoneNumber().getNationalNumber());
                internationalphoneNumber = String.valueOf(phoneInputView.getNumber());
                countrycode=String.valueOf("+"+phoneInputView.getSelectedCountry().getDialCode());
                showToastMsg(PreRegistrationActivity.this, "Phone No :" + countrycode + internationalphoneNumber);
                showProgressDialog();
                authenticateUser();
            }else{
                NetworkUtils.showAlertDialog(PreRegistrationActivity.this,"Enter All Mandatory Fields");
            }
        }else {
            NetworkUtils.showAlertDialog(this,"Check ur INTERNET Connection");
        }
    }

    private void startRegistrationActivity(){
        Intent intent = new Intent(PreRegistrationActivity.this, RegistrationActivity.class);
        intent.putExtra("ISDCODE",countrycode);
        intent.putExtra("MOBILE",internationalphoneNumber);
        intent.putExtra("VERIFICATIONCODE",verification_code);
        startActivity(intent);
    }

    private void authenticateUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, authenticateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Authenticate-User-onResponse " + response);
                        try {
                            hideProgressDialog();

                            JSONObject responseobject = new JSONObject(response);
                            if(responseobject != null) {
                                if (responseobject.has("reponse_code") &&
                                        responseobject.getString("reponse_code").equalsIgnoreCase("0")) {
                                    NetworkUtils.showAlertDialog(PreRegistrationActivity.this, "Mandatory fields Not filled");
                                } else if (responseobject.has("json_data")){
                                    JSONObject message = (JSONObject) responseobject.get("json_data");
                                    if(message.has("message")) {
                                        if (message.getString("message").equalsIgnoreCase("Success")) {
                                            verification_code = message.getString("verification_code");
                                            startRegistrationActivity();
                                        } else if (message.getString("message").equalsIgnoreCase("Already registered")) {
                                            Intent intent = new Intent(PreRegistrationActivity.this, InteractionActivity.class);
                                            startActivity(intent);
                                        }

                                    }
                                    else{
                                        NetworkUtils.showAlertDialog(PreRegistrationActivity.this,"Unable to connect to Server. Please try again later.");
                                    }
                                }
                                else{
                                    NetworkUtils.showAlertDialog(PreRegistrationActivity.this,"Unable to connect to Server. Please try again later.");
                                }
                            }

                            /*
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(response);
                            if(rootNode.path("reponse_code").asText().contains("0")){
                                NetworkUtils.showAlertDialog(PreRegistrationActivity.this,"Mandatory fields Not filled");
                            }else if (rootNode.path("json_data").path("message").asText().contains("Success")) {
                                verification_code=rootNode.path("json_data").path("verification_code").toString();
                                startRegistrationActivity();
                            }else if(rootNode.path("json_data").path("message").asText().contains("Already registered")){
                                Intent intent = new Intent(PreRegistrationActivity.this, InteractionActivity.class);
                                startActivity(intent);
                            }*/
                        } catch (Exception error) {
                            NetworkUtils.showAlertDialog(PreRegistrationActivity.this,error.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        NetworkUtils.showAlertDialog(PreRegistrationActivity.this,error.getMessage());
                    }
                }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("isd_code", countrycode);
                    params.put("mobile_no", internationalphoneNumber);
                    params.put("invite_code", invitationnCode);
                    params.put("email", emailAddress);
                    return params;
                }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(REQUEST_TAG);
        requestQueue.add(stringRequest);
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
