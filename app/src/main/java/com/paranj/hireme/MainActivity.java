package com.paranj.hireme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       // Intent intent = new Intent(this, Register.class);

        ImageButton btn = findViewById(R.id.profileButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent coupon = new Intent(MainActivity.this, Profile.class);
                startActivity(coupon);
            }
        });



    }

    @Override
    public void onBackPressed()
    {
        //Do nothing
    }

}
