package com.android.chickenbuzz.global;

import android.app.Application;

import com.android.chickenbuzz.beans.UserDetails;

/**
 * Created by krelluru on 8/26/2016.
 */

public class ChickenBuzzApplication extends Application {

    private UserDetails mUserAboutDetails;

    @Override
    public void onCreate() {
        super.onCreate();

        //SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
    }

    public UserDetails getmUserAboutDetails() {
        return mUserAboutDetails;
    }

    public void setmUserAboutDetails(UserDetails mUserAboutDetails) {
        this.mUserAboutDetails = mUserAboutDetails;
    }
}
