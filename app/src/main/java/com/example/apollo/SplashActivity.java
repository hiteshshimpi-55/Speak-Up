package com.example.apollo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try{

                    sleep(3000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    if(isNetworkAvailable(getApplicationContext()))
                    {

                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        finish();
                        System.exit(0);
                    }

                }
            }
        };
        thread.start();
    }

    /**
     * This method check mobile is connected to network.
     * @param context
     * @return true if connected otherwise false.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}