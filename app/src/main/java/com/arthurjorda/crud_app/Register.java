package com.arthurjorda.crud_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    Button registerBtn;
    //declare instance of FirebaseAuth
    FirebaseAuth mAuth;

    //check if user is logged in
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //initializing editText and button
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerBtn = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progress_circular);

        // create an onclick method to the register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set visibility of progressbar
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                //check if email or password inputs are empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //create user with email and password visit firebase docs at https://firebase.google.com/docs/auth/android/start?hl=en&authuser=0#java_3
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Register.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, Login.class));
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Failed to create account.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        TextView registerRedirectText = findViewById(R.id.gotoLoginText);
        // Create a clickable span for "Register"
        ClickableSpan loginSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle the click on "Login"
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        };

        // Create a SpannableString and set the clickable span
        SpannableString spannableString = new SpannableString(registerRedirectText.getText());
        spannableString.setSpan(loginSpan, 26, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Adjust the start and end indexes as needed

        // Set the modified text to the TextView
        registerRedirectText.setText(spannableString);

        // Enable the TextView to handle clicks
        registerRedirectText.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
}