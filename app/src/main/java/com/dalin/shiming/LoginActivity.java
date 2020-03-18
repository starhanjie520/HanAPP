package com.dalin.shiming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_cancel;
    private Button btn_webchat_login;
    private Button btn_register;

    private EditText et_username;
    private EditText et_password;

    private SharedPreferences login_sp;

    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        btn_webchat_login = (Button)findViewById(R.id.btn_webchat_login);
        btn_webchat_login.setOnClickListener(mListener);

//        btn_login = (Button)findViewById(R.id.btn_login);
//        btn_cancel = (Button)findViewById(R.id.btn_cancel);

//        btn_register = (Button)findViewById(R.id.btn_register);
//
//        et_username = (EditText)findViewById(R.id.et_username);
//        et_password = (EditText)findViewById(R.id.et_password);
//        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//        btn_login.setOnClickListener(mListener);
//        btn_cancel.setOnClickListener(mListener);

       // btn_register.setOnClickListener(mListener);
//        login_sp = getSharedPreferences("userInfo", 0);
//        String et_username=login_sp.getString("et_username", "");
//        String et_password =login_sp.getString("et_password", "");

        api = WXAPIFactory.createWXAPI(this, Config.appid,true);


    }
    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.btn_login:
//                   // login();
//                    break;
//                case R.id.btn_cancel:
//                    finish();
//                    break;
                case R.id.btn_webchat_login:
                    Webchatlogin();
                    finish();
                    break;
//                case R.id.btn_register:
//                //    register();
//                    break;
    }
}
    };

    public void Webchatlogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        api.sendReq(req);
        Log.d("Webchat Login---------------","Webchat Login---------------");
    }

//    public void register() {
//
//        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class) ;
//        startActivity(intent);
//        finish();
//    }
//
//
//    public void login() {
//        if (isUserNameAndPwdValid()) {
//            String userName = et_username.getText().toString().trim();
//            String userPwd = et_password.getText().toString().trim();
//            SharedPreferences.Editor editor =login_sp.edit();
//
//            String url = Config.mobile_login + "?get_type=login&telephonenumber=" + userName + "&password=" + userPwd ;
//            Log.d("url--------------", url);
//            String returncode = MyUtil.get(url);
//            Log.d("returncode--------------", returncode);
//            if(returncode.contains("loginsuccess")){
//                Toast.makeText(this, "Login Success ^_^",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class) ;
//                startActivity(intent);
//                finish();
//            }else{
//                Toast.makeText(this, "Login Fail !",Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//    }
//    public boolean isUserNameAndPwdValid() {
//        if (et_username.getText().toString().trim().equals("")) {
//            Toast.makeText(this, "사용자 이를 넣어 주세요!",Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (et_password.getText().toString().trim().equals("")) {
//            Toast.makeText(this,"비밀 번호를 넣어 주세요!",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }





}
