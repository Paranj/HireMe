package com.paranj.hireme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth firebaseAuth;

    private Object curUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        addressLine2 = findViewById(R.id.addressline2);
        phoNumber = findViewById(R.id.phoneNumber);
        textViewEmail = findViewById(R.id.email);
        addressLine1 = findViewById(R.id.addressLine1);
        editImage  = findViewById(R.id.editImage);
        firstName  = findViewById(R.id.firstName);
        lastName  = findViewById(R.id.lastName);
        zip  = findViewById(R.id.zipCode);
        city  = findViewById(R.id.city);
        state  = findViewById(R.id.state);

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot snap = dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid());

                firstName.setText(snap.child("first").getValue().toString());
                lastName.setText(snap.child("last").getValue().toString());
                textViewEmail.setText(snap.child("email").getValue().toString());

               //Log.e("Snapshot", );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });


        //user = mRef.child("Users").child(currentUser.getUid()).child("first").;

    }

//    @Override
//    public void onStart(){
//        super.onStart();
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int count = 0;
//                    for(DataSnapshot snap: dataSnapshot.getChildren()){
//
//                        Log.e("Snapshot", snap.child(firebaseAuth.getCurrentUser().getUid()).child("first").getValue().toString());
//                    }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //
//            }
//        });
//
//    }
}
