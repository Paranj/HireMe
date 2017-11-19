package com.paranj.hireme;

import android.app.ProgressDialog;
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


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        registerButton = (Button)findViewById(R.id.registerButton);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        signin = (TextView)findViewById(R.id.loginText);

        registerButton.setOnClickListener(this);
        signin.setOnClickListener(this);

    }

    private void registerUser(){
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

        progressDialog.setMessage("Registering User, Please Wait....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Message", "createUserWithEmail:success");
                            Toast.makeText(Main2Activity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Message", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Main2Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == registerButton){
            registerUser();
        }
        else if(view == signin){
            //will open login activity
        }
    }
}
