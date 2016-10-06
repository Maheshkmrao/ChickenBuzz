package com.android.chickenbuzz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.chickenbuzz.R;

/**
 * Created by krelluru on 8/26/2016.
 */

public class TermsConditionsActivity extends BaseActivity implements View.OnClickListener{

    private Button button_accept, button_decline;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_terms_conditions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mtootlbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mtootlbarTitle.setText(getString(R.string.title_terms_conditions));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        button_accept = (Button)findViewById(R.id.btn_accept);
        button_decline = (Button)findViewById(R.id.btn_decline);

        button_accept.setOnClickListener(this);
        button_decline.setOnClickListener(this);
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
            case R.id.btn_accept:
                startMainActivity(true);
                break;
            case R.id.btn_decline:
                startMainActivity(false);
                break;
        }
    }

    private void startMainActivity(boolean isAccepted){
        Intent intent = new Intent();
        intent.putExtra("IS_ACCEPTED", isAccepted);
        setResult(100, intent);
        finish();
//        startActivity(intent);
    }
}
