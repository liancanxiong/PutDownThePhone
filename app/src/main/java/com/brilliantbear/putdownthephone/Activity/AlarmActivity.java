package com.brilliantbear.putdownthephone.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.brilliantbear.putdownthephone.R;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent home = new Intent(Intent.ACTION_MAIN);

        home.addCategory(Intent.CATEGORY_HOME);

        startActivity(home);

        finish();
    }



}
