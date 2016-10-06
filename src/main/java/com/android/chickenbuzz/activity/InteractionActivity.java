package com.android.chickenbuzz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.android.chickenbuzz.R;

/**
 * Created by krelluru on 8/26/2016.
 */

public class InteractionActivity extends BaseActivity implements View.OnClickListener{

    private Button button_begin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_intraction);
        setTitle(getString(R.string.title_instraction));
        button_begin=(Button)findViewById(R.id.btn_begin);
        button_begin.setOnClickListener(this);
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
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_begin:
                startMainActivity();
                break;
        }
    }

    private void startMainActivity(){
        finish();
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
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
