package com.paranj.hireme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GridView gridView = findViewById(R.id.gridLayout);
        gridView.setAdapter(new GridAdapter(MainActivity.this));

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
    public void onBackPressed(){
        super.onBackPressed();
    }

}

class GridAdapter extends BaseAdapter{
    Context context;
    String strings = "123456789";

    public GridAdapter (Context c){
       context = c;
    }


    @Override
    public int getCount() {
        return strings.length();
    }

    @Override
    public Object getItem(int i) {
        return strings.charAt(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.engineering);
        return imageView;
    }
}
