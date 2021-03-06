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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView forgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        progressDialog = new ProgressDialog(this);
        register = (TextView)findViewById(R.id.signUp);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = (Button)findViewById(R.id.registerButton);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    public void loginUser(final String email, final String password){

        if(TextUtils.isEmpty(email)){
            Toast.makeText(LogIn.this, "Please enter your email",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(LogIn.this, "Please enter your password",
                    Toast.LENGTH_SHORT).show();
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

                            progressDialog.cancel();

                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

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
            loginUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
        }
        else if(view == forgotPassword){
        }

        else if(view == register){
            Intent intent = new Intent(LogIn.this, Register.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
