package com.paranj.hireme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {

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
    private Spinner mySpinner;
    private TextView textViewState;

    private Toolbar mToolBar;
    private CircleImageView profileImage;
    private ProgressDialog mProgressDialog;
    private EditText address1_1;
    private EditText address2_1;
    private EditText zip_1;
    private EditText city_1;

    private StorageReference mImageStorage;

    public static final int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewState = findViewById(R.id.states);
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        editImage = findViewById(R.id.edit_Image);
        profileImage = findViewById(R.id.profile_image);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        address1_1 = findViewById(R.id.editTextAddress1);
        address1_1.setVisibility(View.INVISIBLE);
        address2_1 = findViewById(R.id.editTextAddress2);
        address2_1.setVisibility(View.INVISIBLE);
        zip_1 = findViewById(R.id.editTextZip);
        zip_1.setVisibility(View.INVISIBLE);
        city_1 = findViewById(R.id.editTextCity);
        city_1.setVisibility(View.INVISIBLE);
        logOut = findViewById(R.id.logOut);
        mySpinner = findViewById(R.id.mySpinner);
        mySpinner.setVisibility(View.INVISIBLE);

        editImage.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    textViewState.setText(adapterView.getItemAtPosition(i).toString());
                    user.setState(adapterView.getItemAtPosition(i).toString());
                    mRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                } catch (NullPointerException w){
                    w.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        logOut.setOnClickListener(this);
        home = (ImageButton)findViewById(R.id.homeButton);
        home.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        addressLine2 = (TextView)findViewById(R.id.addressline2);
        phoNumber = (TextView)findViewById(R.id.phoneNumber);
        textViewEmail = (TextView)findViewById(R.id.email);
        addressLine1 =(TextView) findViewById(R.id.addressLine1);
        editImage = (TextView)findViewById(R.id.edit_Image);
        firstName = (TextView)findViewById(R.id.firstName);
        lastName =(TextView) findViewById(R.id.lastName);
        zip = (TextView)findViewById(R.id.zipCode);
        city = (TextView)findViewById(R.id.city);
        RelativeLayout backGroundLayout = (RelativeLayout) findViewById(R.id.layoutBackground);

        addressLine1.setOnClickListener(this);
        addressLine2.setOnClickListener(this);
        textViewEmail.setOnClickListener(this);
        zip.setOnClickListener(this);
        city.setOnClickListener(this);
        textViewState.setOnClickListener(this);
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
                if(user.getState() != null){
                    textViewState.setText(user.getState());
                }

                if(user.getProImgUrl() != null) {
                    Picasso.with(Profile.this).load(user.getProImgUrl()).placeholder(R.drawable.account).into(profileImage);
                }
                else {
                    Picasso.with(Profile.this).load("https://foremployers.voya.com/sites/unit.voya.com/themes/custom/voya_com/img/common/account-access-card.jpg").into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case Gallery_Pick:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, Gallery_Pick);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();

            CropImage.activity(imageUri).setAspectRatio(1,1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(Profile.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while the image is uploading.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();
                StorageReference filepath = mImageStorage.child("profile_images").child(user.getuId() + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            String imageUrl = task.getResult().getDownloadUrl().toString();
                            user.setProImgUrl(imageUrl);
                            mRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        mProgressDialog.dismiss();
                                        Toast.makeText(Profile.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(Profile.this, "Error in Uploading", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();


            }
        }
    }

    @Override
    public void onClick(View view) {

        if(view == home){
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);
        }
        if(view == logOut){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Profile.this, LogIn.class);
            startActivity(intent);
            finish();
        }

        if(view == editImage){
            try {
                if (ActivityCompat.checkSelfPermission(Profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Profile.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Gallery_Pick);
                } else {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"),Gallery_Pick);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(view == addressLine1){
            Log.e("address1_1", "Clicked");
            addressLine1.setVisibility(View.INVISIBLE);
            address1_1.setVisibility(View.VISIBLE);


            address2_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);
            mySpinner.setVisibility(View.INVISIBLE);

            addressLine2.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);
            textViewState.setVisibility(View.VISIBLE);

        }
        else if(view == addressLine2){
            Log.e("address1_1", "Clicked2");
            addressLine2.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);
            textViewState.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);
            mySpinner.setVisibility(View.INVISIBLE);
        }

        else if(view == zip){
            zip.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            addressLine2.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);
            textViewState.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);
            mySpinner.setVisibility(View.INVISIBLE);

        }
        else if(view == city){
            city.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.VISIBLE);

            addressLine1.setVisibility(View.VISIBLE);
            addressLine2.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);
            textViewState.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            mySpinner.setVisibility(View.INVISIBLE);


        }

        else if(view == textViewState){
            textViewState.setVisibility(View.INVISIBLE);
            mySpinner.setVisibility(View.VISIBLE);
            Log.e("Spinner be active","");

            addressLine1.setVisibility(View.VISIBLE);
            addressLine2.setVisibility(View.VISIBLE);
            zip.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);

            address1_1.setVisibility(View.INVISIBLE);
            address2_1.setVisibility(View.INVISIBLE);
            zip_1.setVisibility(View.INVISIBLE);
            city_1.setVisibility(View.INVISIBLE);

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
            mySpinner.setVisibility(View.INVISIBLE);
            textViewState.setVisibility(View.VISIBLE);

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

        try {
            mRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
        }
        catch (NullPointerException e){

        }
    }

}
