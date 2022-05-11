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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private ImageView loginButton;
    private TextView signUp;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginEmailText = (EditText) findViewById(R.id.username_signup);
        loginPassText = (EditText) findViewById(R.id.password_signup);
        loginButton = (ImageView) findViewById(R.id.login_btn);
        signUp = (TextView) findViewById(R.id.SignUp_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intent =  new Intent(Login_Activity.this,SignUpActivity.class);
                startActivity(signup_intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass  = loginPassText.getText().toString();
                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                checkIfEmailVerified();
                            }
                            else
                            {
                                String e = task.getException().getMessage();
                                Toast.makeText(Login_Activity.this,"ERROR : "+e,Toast.LENGTH_LONG).show();
                            }

                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else
                {
                    Toast.makeText(Login_Activity.this,"ERROR : PLEASE FILL THE REQUIRED FIELD",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();
        /* IF USER IS ALREADY LOGIN THEN THIS METHOD WILL TAKE PLACE */
        if(currUser != null)
        {
            sendToMain();
        }

    }
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            sendToMain();
            Toast.makeText(Login_Activity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(Login_Activity.this,"Please verify your email by visiting the link\n sent to you by our team on gmail\n",Toast.LENGTH_LONG).show();
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            //restart this activity
        }
    }
    private void sendToMain() {
        Intent main_Intent = new Intent(Login_Activity.this,MainActivity.class);
        startActivity(main_Intent);
        finish();
    }
}