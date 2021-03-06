package com.paranj.hireme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText countCode;
    private EditText phoNumber;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView signIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private EditText firstName;
    private EditText lastName;
    private Users user;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        registerButton = (Button)findViewById(R.id.registerButton);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.password);
        signIn = (TextView) findViewById(R.id.forgotPassword);
        firstName = (EditText)findViewById(R.id.first);
        lastName = (EditText)findViewById(R.id.last);
        countCode = (EditText)findViewById(R.id.countryCode);
        phoNumber = (EditText)findViewById(R.id.phoneNumber);

        registerButton.setOnClickListener(this);
        signIn.setOnClickListener(this);



    }

    private void registerUser(){
       final String email = editTextEmail.getText().toString().trim();
       final String password = editTextPassword.getText().toString().trim();
       final String first = firstName.getText().toString().trim();
       final String last = lastName.getText().toString().trim();
       final String countryCode = countCode.getText().toString().trim();
       final String phoneNumber = phoNumber.getText().toString().trim();

       if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(first) ||
               TextUtils.isEmpty(last) || TextUtils.isEmpty(phoneNumber)){

           Toast.makeText(Register.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();

           return;
       }


        progressDialog.setMessage("Registering User, Please Wait....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String uId = "Random User ID";
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(first + " " + last).build();

                            currentUser.updateProfile(profileUpdates);

                            try {
                                uId = currentUser.getUid();
                                
                            } catch (NullPointerException e){
                                Log.e("Null Pointer Exception", "At uID of current user ");
                            }

                            user = new Users(uId, first, last, phoneNumber, email, countryCode);

                            initFireBase();
                            addEventFirebaseListener();

                            mRef.child("Users").child(user.getuId()).setValue(user);

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Message", "createUserWithEmail:success");
                            Toast.makeText(Register.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            loginUser(user.getEmail(), password);

                        } else {

                            String error = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                error = "Invalid Email!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error = "Invalid Credentials!";
                            } catch (Exception e) {
                                error = "Default error!";
                                e.printStackTrace();
                            }

                            progressDialog.cancel();
                            Toast.makeText(Register.this, error, Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }

    public void loginUser(final String email, final String password){

        progressDialog.setMessage("Loggin In, Please Wait....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            progressDialog.cancel();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Message: ", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });

    }


    private void addEventFirebaseListener() {

        mRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // String value = dataSnapshot.getValue(String.class);
               // Log.d("Don't know whats this", "Value is: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("Problemo", "Failed to read value.");
            }
        });

    }

    private void initFireBase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

    }


    @Override
    public void onClick(View view) {
        if(view == registerButton){
            registerUser();

        }
        else if(view == signIn){

            Intent intent = new Intent(Register.this, LogIn.class);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
