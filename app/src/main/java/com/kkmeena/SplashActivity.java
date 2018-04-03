package com.kkmeena;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.sax.StartElementListener;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.kkmeena.MainActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.kkmeena.R.color.colorPrimary;

public class SplashActivity extends Activity {

    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    TextView load;
    AnimationDrawable animationDrawable;


    long Delay=4000;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        relativeLayout=(RelativeLayout)findViewById(R.id.container2);
        progressBar=(ProgressBar) findViewById(R.id.progress);
        load = (TextView)findViewById(R.id.laod);


        netcheck();

    }

    private boolean isWorkingInternetPresent()  {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile!=null&&mobile.isConnectedOrConnecting())||(wifi!=null&&wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else {
            return false;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void netcheck() {
        if (!isWorkingInternetPresent()) {
            Snackbar snackbar = Snackbar.make(relativeLayout, "Network not available", Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    netcheck();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            load.setText("");
            progressBar.setVisibility(View.GONE);
        } else{
            load.setText("Loading...");
            progressBar.setVisibility(View.VISIBLE);

            //(new LoadBackgroundData()).execute();



            Timer RunSplash = new Timer();

            //task when timer end
            TimerTask ShowSplash = new TimerTask() {
                @Override
                public void run() {
                    //close splashactivity.class


                    //start mainactivity
                    (new LoadBackgroundData()).execute();
                    Intent myintent = new Intent(SplashActivity.this, MainActivity.class);
                    myintent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(myintent);
                    finish();
                }
            };

            //start timer
            RunSplash.schedule(ShowSplash, Delay);
        }
    }

    class LoadBackgroundData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        protected void onPostExecute(String result){
            startActivity(new Intent(getBaseContext(),MainActivity.class));
            finish();
        }
    }


    /*
    private void showSplash(){
        Thread thread=new Thread(){
            @Override
            public void run() {
                ProgressBar progressBar=(ProgressBar) findViewById(R.id.progress);
                synchronized (this){
                    try{
                        progressBar.setVisibility(View.VISIBLE);
                        wait(6000);
                    } catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                        finish();
                    }
                }
                super.run();
            }
        };
        thread.start();
    }*/

}