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
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText username,emailsign,passwordsign;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();

        username=(EditText) findViewById(R.id.usernamehint);
        emailsign=(EditText) findViewById(R.id.emailsignhint);
        passwordsign=(EditText) findViewById(R.id.paaswordsignhint);
        signup=(Button) findViewById(R.id.signup);

        Intent intentlogin=new Intent(this,Login.class);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupuser();
            }

            private void signupuser() {
                String name=username.getText().toString().trim();
                String email=emailsign.getText().toString().trim();
                String password=passwordsign.getText().toString().trim();
                if(name.isEmpty()){
                    username.setError("Name is required!..");
                    username.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    emailsign.setError("Email id is required!..");
                    emailsign.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailsign.setError("Please provide valid email id!...");
                    emailsign.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    passwordsign.setError("Password is required!..");
                    passwordsign.requestFocus();
                    return;
                }
                if(password.length()<8){
                    passwordsign.setError("Min Password length is 8 characters!... ");
                    passwordsign.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user=new User (name,email);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUp.this,"User has been registered!..",Toast.LENGTH_LONG).show();
                                                startActivity(intentlogin);
                                            }
                                            else{
                                                Toast.makeText(SignUp.this,"Failed to register! Try again!..",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(SignUp.this,"Failed to register! Try again!..",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}