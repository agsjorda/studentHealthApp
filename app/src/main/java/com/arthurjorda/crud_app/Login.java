package com.arthurjorda.crud_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    Button loginBtn;
    //declare instance of FirebaseAuth
    FirebaseAuth mAuth;

    //check if user is logged in
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //initializing editText and button
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_circular);

        //create onclick for login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set visibility of progressbar
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                //check if email or password inputs are empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // sign in existing users
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Login Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Move to main activity
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Login.this, "Login failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        TextView loginRedirectText = findViewById(R.id.gotoRegisterText);
        // Create a clickable span for "Register"
        ClickableSpan registerSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle the click on "Register"
                startActivity(new Intent(Login.this, Register.class));
            }
        };

        // Create a SpannableString and set the clickable span
        SpannableString spannableString = new SpannableString(loginRedirectText.getText());
        spannableString.setSpan(registerSpan, 23, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Adjust the start and end indexes as needed
        
        // Set the modified text to the TextView
        loginRedirectText.setText(spannableString);

        // Enable the TextView to handle clicks
        loginRedirectText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
}
