package com.android.chickenbuzz.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.fragments.AboutMeFragment;
import com.android.chickenbuzz.utils.Constant;

public class AboutMeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        setTitle(getResources().getString(R.string.title_aboutme));
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
            AboutMeFragment firstFragment = new AboutMeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, firstFragment).commit();
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
            if(Constant.IS_HOME_SCREEN_DISPLAYED)
                finish();

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Constant.IS_HOME_SCREEN_DISPLAYED)
            finish();
    }

}
