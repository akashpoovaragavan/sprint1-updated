package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextView signup,forgot;
    private EditText email,password;
    private Button signin;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailhint);
        password = (EditText) findViewById(R.id.passwordhint);
        signin = (Button) findViewById(R.id.signin);
        signup = (TextView) findViewById(R.id.signup);
        forgot = (TextView) findViewById(R.id.forgot);

        mAuth=FirebaseAuth.getInstance();

        Intent intentsignup = new Intent(this, SignUp.class);
        Intent intentforgot = new Intent(this, Forgot.class);
        Intent intenthome = new Intent(this, HomeScreen.class);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSignIn();
            }

            private void UserSignIn() {
                String emailstr = email.getText().toString().trim();
                String passwordstr = password.getText().toString().trim();
                if (emailstr.isEmpty()) {
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailstr).matches()) {
                    email.setError("Please enter valid email!");
                    email.requestFocus();
                    return;
                }
                if (passwordstr.isEmpty()) {
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if (passwordstr.length() < 8) {
                    password.setError("Min Password length is 8 character!");
                    password.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(emailstr, passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()) {

                                startActivity(intenthome);
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(Login.this, "Check your mail to verification", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Failed to Login! Please check your credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentsignup);
            }


        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentforgot);
            }
        });

    }


}