package com.android.chickenbuzz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.utils.Constant;
import com.android.chickenbuzz.utils.Utility;

/**
 * Created by krelluru on 8/25/2016.
 */

public class WelcomeScreen extends AppCompatActivity{

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        startPreRegistration();
    }

    private void startPreRegistration(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                // This method will be executed once the timer is over
                // Start your app main activity
                if(Utility.getStringFromPreference(WelcomeScreen.this, Constant.KEY_USER_ID).equalsIgnoreCase("")){
                    i = new Intent(WelcomeScreen.this, PreRegistrationActivityNew.class);
                    Constant.IS_HOME_SCREEN_DISPLAYED = false;
//                    i = new Intent(WelcomeScreen.this, MainActivity.class);
//                    Constant.IS_HOME_SCREEN_DISPLAYED = true;
                }
                else{
                    Toast.makeText(WelcomeScreen.this, "User ID :" +
                            Utility.getStringFromPreference(WelcomeScreen.this, Constant.KEY_USER_ID), Toast.LENGTH_LONG).show();
                    i = new Intent(WelcomeScreen.this, MainActivity.class);
                    Constant.IS_HOME_SCREEN_DISPLAYED = true;
                }
//                i = new Intent(WelcomeScreen.this, PreRegistrationActivityNew.class);
//                Intent i = new Intent(WelcomeScreen.this, SendingInviteActivity.class);
//                Intent i = new Intent(WelcomeScreen.this, MainActivity.class);
//                Intent i = new Intent(WelcomeScreen.this, MyLocationsActivity.class);
//                Intent i = new Intent(WelcomeScreen.this, IntlActivity.class);
//                Intent i = new Intent(WelcomeScreen.this, AboutMeActivity.class);_
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
