package com.paranj.hireme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.*;
import android.content.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth firebaseAuth;

    private ImageButton home;
    private Users user;
    private TextView logOut;
    private TextView addressLine2;
    private TextView phoNumber;
    private TextView textViewEmail;
    private TextView addressLine1;
    private TextView editImage;
    private TextView firstName;
    private TextView lastName;
    private TextView zip;
    private TextView city;
    private TextView state;

    private EditText address1_1;
    private EditText address2_1;
    private EditText zip_1;
    private EditText city_1;
    private RelativeLayout backGroundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        address1_1 = findViewById(R.id.editTextAddress1);
        address1_1.setVisibility(View.INVISIBLE);
        address2_1 = findViewById(R.id.editTextAddress2);
        address2_1.setVisibility(View.INVISIBLE);
        zip_1 = findViewById(R.id.editTextZip);
        zip_1.setVisibility(View.INVISIBLE);
        city_1 = findViewById(R.id.editTextCity);
        city_1.setVisibility(View.INVISIBLE);
        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(this);
        home = findViewById(R.id.homeButton);
        home.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        addressLine2 = findViewById(R.id.addressline2);
        phoNumber = findViewById(R.id.phoneNumber);
        textViewEmail = findViewById(R.id.email);
        addressLine1 = findViewById(R.id.addressLine1);
        editImage = findViewById(R.id.editImage);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        zip = findViewById(R.id.zipCode);
        city = findViewById(R.id.city);
//        state = findViewById(R.id.state);
        backGroundLayout = findViewById(R.id.layoutBackground);

        addressLine1.setOnClickListener(this);
        addressLine2.setOnClickListener(this);
        zip.setOnClickListener(this);
        city.setOnClickListener(this);
        backGroundLayout.setOnClickListener(this);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = new Users();
                user = dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).getValue(Users.class);
                firstName.setText(user.getFirst());
                lastName.setText(user.getLast());
                textViewEmail.setText(user.getEmail());
                phoNumber.setText(user.getPhoneNumber());

                if(user.getAddress1() != null){
                    addressLine1.setText(user.getAddress1());
                }
                if(user.getAddress2() != null){
                    addressLine2.setText(user.getAddress2());
                }
                if(user.getZip() != null){
                    zip.setText(user.getZip());
                }
                if(user.getCity() != null){
                    city.setText(user.getCity());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view == home){
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);
        }
        if(view == logOut){
            //prompt User for yes or no
            // if yes
            SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE );
            SharedPreferences.Editor editor = userInfo.edit();

            editor.putString("userEmail", "");
            editor.putString("password", "");
            editor.apply();
            Intent intent = new Intent(Profile.this, LogIn.class);
            startActivity(intent);
        }

        if(view == addressLine1){
            Log.e("address1_1", "Clicked");
            addressLine1.setVisibility(View.INVISIBLE);
            address1_1.setVisibility(View.VISIBLE);


            address2_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);

            addressLine2.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);

        }
        else if(view == addressLine2){
            Log.e("address1_1", "Clicked2");
            addressLine2.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);
        }

        else if(view == zip){
            zip.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            addressLine2.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);

        }
        else if(view == city){
            city.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            addressLine2.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);


        }

        else{
            address1_1.setVisibility(View.INVISIBLE);
            addressLine1.setVisibility(View.VISIBLE);
            address2_1.setVisibility(View.INVISIBLE);
            addressLine2.setVisibility(View.VISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            zip.setVisibility(View.VISIBLE);
            city_1.setVisibility(View.INVISIBLE);
            city.setVisibility(View.VISIBLE);

        }
        if(!address1_1.getText().toString().matches("")){
            addressLine1.setText(address1_1.getText());
            user.setAddress1(addressLine1.getText().toString());
            //Log.e("This Ran")
        }
        if(!address2_1.getText().toString().matches("")){
            addressLine2.setText(address2_1.getText());
            user.setAddress2(addressLine2.getText().toString());
        }

        if(!zip_1.getText().toString().matches("")){
            zip.setText(zip_1.getText());
            user.setZip(zip.getText().toString());
        }
        if(!city_1.getText().toString().matches("")){
            city.setText(city_1.getText());
            user.setCity(city.getText().toString());
        }

        mRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
    }

}
