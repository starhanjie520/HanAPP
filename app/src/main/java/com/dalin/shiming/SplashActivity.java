package com.dalin.shiming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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
//        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("telephonenumber", "01053869009");
//        editor.commit();


        SharedPreferences login_sp = getSharedPreferences("userInfo", 0);
        String telephonenumber=login_sp.getString("telephonenumber", "");
        final String webchatopenid=login_sp.getString("webchatopenid", "");
        Log.d("webchatopenid : -===---  ",webchatopenid);

        new Thread(){
            @Override
            public void run() {
                super.run();
                Log.d("33333  ","ㄴ11111111111ㅇ");
            //    SystemClock.sleep(1000);
                if(webchatopenid.equals("")){   // 설정 파일이 있을경우
                    Log.d("webchatopenid=====  exist----  ",webchatopenid);
                    //startMainActviity();

                }else {
                    Log.d("webchatopenid : ----not  exist----  ",webchatopenid);
//                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                }
            }
        }.start();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(et_username.trim().equals("")||et_password.trim().equals("")){
//                    startMainActviity();
//                }else {
//                    if(IdPwCheck()){
//                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        finish();
//                    }
//                }
//            }
//        }, 200);
    }


    private boolean IdPwCheck(){


        return true;
    }

    private void startMainActviity() {
        if(!isStartMain){
            isStartMain = true;
            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
      //  startMainActviity();
        return super.onTouchEvent(event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
