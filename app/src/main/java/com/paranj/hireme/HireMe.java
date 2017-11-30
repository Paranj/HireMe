package com.paranj.hireme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Paranj on 11/25/17.
 */

public class HireMe extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(HireMe.this, SplashActivity.class);
        startActivity(intent);
    }
}
