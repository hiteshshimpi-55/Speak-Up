package com.example.apollo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mainToolbar;
    private ImageView addPost_btn;

    private BottomNavigationView main_bottom_nav_view;
    private FirebaseAuth mAuth;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment accountFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        //Bottom navigation bar
        main_bottom_nav_view = findViewById(R.id.mainBottomNav);
        //Fragment
        homeFragment  = new HomeFragment();
        notificationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();
        //Post Button
        addPost_btn = (ImageView) findViewById(R.id.addPostBtn);
        replaceFragment(homeFragment);
        addPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post_Intent = new Intent(MainActivity.this,NewPostActivity.class);
                startActivity(post_Intent);
            }
        });
        main_bottom_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.bottom_home:
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.bottom_post:
                        replaceFragment(notificationFragment);
                        return true;
                    case R.id.bottom_acc:
                        replaceFragment(accountFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currUser==null)
        {
            sendToLogin();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        Intent login_Intent = new Intent(MainActivity.this,Login_Activity.class);
        startActivity(login_Intent);
        finish();
    }

    private void replaceFragment(Fragment newFragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,newFragment);
        fragmentTransaction.commit();
    }
}