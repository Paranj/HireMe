package com.paranj.hireme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        registerButton = findViewById(R.id.registerButton);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.forgotPassword);
        firstName = findViewById(R.id.first);
        lastName = findViewById(R.id.last);
        countCode = findViewById(R.id.countryCode);
        phoNumber = findViewById(R.id.phoneNumber);

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
           if(TextUtils.isEmpty(email)){
               editTextEmail.setBackgroundColor(getResources().getColor(R.color.colorAccent));

           }

           if(TextUtils.isEmpty(password)){
                editTextPassword.setBackgroundColor(getResources().getColor(R.color.colorAccent));
           }
           if(TextUtils.isEmpty(first)){
                firstName.setBackgroundColor(getResources().getColor(R.color.colorAccent));
           }
           if(TextUtils.isEmpty(last)){
               lastName.setBackgroundColor(getResources().getColor(R.color.colorAccent));
           }
           if(TextUtils.isEmpty(phoneNumber)){
                phoNumber.setBackgroundColor(getResources().getColor(R.color.colorAccent));
           }


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
                            if(firebaseAuth.getCurrentUser().toString().contains("FirebaseAuthInvalidCredentialsException")){
                                Log.e("Message", "Invalid Email Format");
                                Toast.makeText(Register.this, "Invaild Email. Please Try Again",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                            else if(firebaseAuth.getCurrentUser().toString().contains("FirebaseAuthUserCollisionException")){
                                Log.e("Message", "Failed" + firebaseAuth.getCurrentUser().toString(), task.getException());
                                Toast.makeText(Register.this, "Email already exists. Please SignIn",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                            else if(firebaseAuth.getCurrentUser().toString().contains("FirebaseAuthWeakPasswordException")){
                                // If sign in fails, display a message to the user.
                                Log.e("Message", "Failed" + firebaseAuth.getCurrentUser().toString(), task.getException());
                                Toast.makeText(Register.this, "Password should be at least 6 characters",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                            else{
                                Log.e("Message", "Failed", task.getException());
                                Toast.makeText(Register.this, "Authentication Failed. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }

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
                            saveLogInInfo(email, password);
                            Intent intent = new Intent(Register.this, MainActivity.class);
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

    private void saveLogInInfo(String email, String password) {
        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE );
        SharedPreferences.Editor editor = userInfo.edit();

        editor.putString("userEmail", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void addEventFirebaseListener() {

        mRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // String value = dataSnapshot.getValue(String.class);
                Log.d("Don't know whats this", "Value is: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Problemo", "Failed to read value.");
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
