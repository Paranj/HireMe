package com.paranj.hireme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView forgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        loginButton = (Button)findViewById(R.id.registerButton);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    public void loginUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //Make Toast
            return;
        }

        if(TextUtils.isEmpty(password)){
            //Make Toast
            return;
        }

        progressDialog.setMessage("Loggin In, Please Wait....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Message: ", "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LogIn.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Message: ", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == loginButton){
            loginUser();
        }
        else if(view == forgotPassword){
            Intent intent = new Intent();
        }
    }
}
