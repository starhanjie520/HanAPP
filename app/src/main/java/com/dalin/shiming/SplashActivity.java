package com.dalin.shiming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class SplashActivity extends AppCompatActivity {


    private Handler handler = new Handler();
    private boolean isStartMain = false;
    private SharedPreferences login_sp;
    private String et_username;
    private String et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        login_sp = getSharedPreferences("userInfo", 0);
        et_username=login_sp.getString("et_username", "");
        et_password =login_sp.getString("et_password", "");
       // final boolean AutoLogin =login_sp.getBoolean("mRememberCheck", false);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(et_username.trim().equals("")||et_password.trim().equals("")){
                    startMainActviity();
                }else {
                    if(IdPwCheck()){
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        finish();
                    }
                }
            }
        }, 200);
    }


    private boolean IdPwCheck(){


        return true;
    }

    private void startMainActviity() {
        if(!isStartMain){
            isStartMain = true;
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        startMainActviity();
        return super.onTouchEvent(event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
