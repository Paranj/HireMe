package com.paranj.hireme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Main2Activity.class);

        Button btn = (Button) findViewById(R.id.findWork);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent coupon = new Intent(MainActivity.this, FindWork.class);
                startActivity(coupon);
            }
        });







    }
}
