package com.android.chickenbuzz.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.fragments.SendingInviteFragment;
import com.android.chickenbuzz.global.ChickenBuzzApplication;
import com.android.chickenbuzz.utils.Constant;

public class SendingInviteActivity extends BaseActivity {

    private SendingInviteActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about_me);
            mActivity = this;
            setTitle(getString(R.string.title_sending_invite));
            if(Constant.IS_HOME_SCREEN_DISPLAYED)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (findViewById(R.id.fragment_container) != null) {

                // However, if we're being restored from a previous state,
                // then we don't need to do anything and should return or else
                // we could end up with overlapping fragments.
                if (savedInstanceState != null) {
                    return;
                }

                // Create a new Fragment to be placed in the activity layout
                SendingInviteFragment firstFragment = new SendingInviteFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container, firstFragment).commit();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mActivity.showToastMsg(mActivity, getString(R.string.permission_granted_contacts));
                } else {
                    mActivity.showToastMsg(mActivity, getString(R.string.err_msg_permission_contacts));
                }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails() != null &&
                    Constant.IS_HOME_SCREEN_DISPLAYED) {
                finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(((ChickenBuzzApplication)mActivity.getApplication()).getmUserAboutDetails() != null &&
                Constant.IS_HOME_SCREEN_DISPLAYED) {
            finish();
        }
    }

}
