package com.example.apollo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_confirm_pass;
    private Button signupBtn;
    private ImageView goToLogin;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        signup_email = (EditText) findViewById(R.id.username_signup);
        signup_password = (EditText) findViewById(R.id.password_signup);
        signup_confirm_pass = (EditText) findViewById(R.id.confirm_pass_signup);
        signupBtn       = (Button) findViewById(R.id.signup_btn);
        progressBar     = (ProgressBar) findViewById(R.id.progressBar_signup);
        goToLogin       = (ImageView) findViewById(R.id.Login_btn);

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent =  new Intent(SignUpActivity.this,Login_Activity.class);
                startActivity(login_intent);
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email  = signup_email.getText().toString();
                String pass   = signup_password.getText().toString();
                String c_pass = signup_confirm_pass.getText().toString();


                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(c_pass))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    if(pass.equals(c_pass))
                    {
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    sendToMain();
                                }else
                                {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignUpActivity.this,"ERROR : "+error,Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this,"PASSWORDS DON'T MATCH",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"ERROR : PLEASE FILL THE REQUIRED FIELD",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();

        if(currUser != null)
        {
            sendToMain();
        }
    }

    private void sendToMain() {

        Intent intent = new Intent(SignUpActivity.this,Login_Activity.class);
        startActivity(intent);
        finish();
    }
}