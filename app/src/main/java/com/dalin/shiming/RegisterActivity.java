package com.dalin.shiming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private EditText register_et_username;
    private EditText register_et_idcardnunber;
    private EditText register_et_password;
    private EditText register_et_password2;
    private EditText register_et_telephonenumber;
    private EditText register_et_sms;

    private Button register_btn;
    private Button register_cancel_btn;
    private Button register_btn_sms;
    public static String policyverity = "0";
    public static String verifyCode = "0000";
    private static final int SHOW_TEXT = 5;
    private Spinner spinner;
    private static final String[] m={"한국","중국"};
    private static String country = "KR";
    private ArrayAdapter<String> adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    register_btn_sms.setText(msg.obj.toString());
                    break;
                case 2:
                    register_btn_sms.setEnabled(true);
                    register_btn_sms.setText("문자인증");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        spinner = (Spinner) findViewById(R.id.Spinner01);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner.setVisibility(View.VISIBLE);

        register_et_telephonenumber = (EditText) findViewById(R.id.register_et_telephonenumber);
        register_et_sms = (EditText) findViewById(R.id.register_et_sms);
        register_et_password = (EditText) findViewById(R.id.register_et_password);
        register_et_password2 = (EditText) findViewById(R.id.register_et_password2);
        register_et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        register_et_password2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        register_et_username = (EditText) findViewById(R.id.register_et_username);
        register_et_idcardnunber = (EditText) findViewById(R.id.register_et_idcardnunber);


        register_btn = (Button) findViewById(R.id.register_btn);
        register_cancel_btn = (Button) findViewById(R.id.register_cancel_btn);
        register_btn_sms = (Button) findViewById(R.id.register_btn_sms);

        register_btn.setOnClickListener(mListener);
        register_cancel_btn.setOnClickListener(mListener);
        register_btn_sms.setOnClickListener(mListener);


    }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            Log.d("region : ",m[arg2]);
            if(m[arg2].equals("한국")){
                country = "KR";
            }else {
                country = "CN";
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sms:
                    SendSms();
                    break;
                case R.id.register_btn:
                    Register();
                    break;
                case R.id.register_cancel_btn:
                    finish();
                    break;
            }
        }
    };

    public void SendSms() {
        Toast.makeText(this, "문자 인증 발송!", Toast.LENGTH_SHORT).show();
        register_btn_sms.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                for (int i = 30; i > 0; i--) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = i;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
                Message message2 = new Message();
                message2.what = 2;
                message2.obj = 2;
                handler.sendMessage(message2);
            }
        }.start();

        Random random = new Random();
        int ttverifyCode = 1000 + random.nextInt(8888);
        verifyCode = Integer.toString(ttverifyCode);
        Log.d("veryfycode :------------------- ", verifyCode);
        new Thread() {
            @Override
            public void run() {
                String number = register_et_telephonenumber.getText().toString().trim();
                Log.d("11111111-------------- ", country);
                MyUtil.SendSMS(country,number, verifyCode);
            }
        }.start();
    }

    public void Register() {
        if (isUserNameAndPwdValid()) {
            if (policyverity.equals("1")) {           //실명 인증  한번 실행 후
                RegisterToServer();
            } else {
                Intent intent = new Intent(this, OfflineFaceLivenessActivity.class);
                intent.putExtra("username", register_et_username.getText().toString().trim());
                intent.putExtra("idnumber", register_et_idcardnunber.getText().toString().trim());
                startActivityForResult(intent, 100);
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                policyverity = "1";
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                RegisterToServer();

            } else {
                policyverity = "0";
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                RegisterToServer();
            }
        }
    }

    public void RegisterToServer() {

        try {
            String telephonenumber = register_et_telephonenumber.getText().toString().trim();
            String password = register_et_password.getText().toString().trim();
            String username = register_et_username.getText().toString().trim();
            String idcardnunber = register_et_idcardnunber.getText().toString().trim();
            String policVerify = policyverity;
            //  username = URLEncoder.encode(username, "UTF-8");
            String url = Config.mobile_register + "?get_type=register&telephonenumber=" + telephonenumber + "&password=" + password + "&username=" + username + "&idcardnunber=" + idcardnunber + "&policVerify=" + policVerify;
            Log.d("url--------------", url);
            String returncode = MyUtil.get(url);
            if (returncode.contains("exist")) {
                Toast.makeText(this, "사용중인 아이디!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            } else if (returncode.contains("success")) {
                Toast.makeText(this, "가입 성공!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "가입 실패!ㅋㅋㅋㅋ", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "알수 없는 에러!", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isUserNameAndPwdValid() {
        if (register_et_username.getText().toString().trim().equals("")) {
            Toast.makeText(this, "사용자 이를 넣어 주세요!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (register_et_password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "비밀 번호를 넣어 주세요!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (register_et_password2.getText().toString().trim().equals("")) {
            Toast.makeText(this, "확인 비밀 번호를 넣어 주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!register_et_password.getText().toString().trim().equals(register_et_password2.getText().toString().trim())) {
            Toast.makeText(this, "2번 입력한 비밀 번호가 다릅니다!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (verify(register_et_password.getText().toString().trim())) {
            Toast.makeText(this, "6 자리 이상 영문 수자 사용 권장!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (register_et_telephonenumber.getText().toString().trim().equals("")) {
            Toast.makeText(this, "전화 번호를 넣어 주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!register_et_sms.getText().toString().trim().equals(verifyCode)) {
            Toast.makeText(this, "SMS 인증번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean verify(String password) {

        String passwordPolicy = "((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9가-힣]).{6,})";

        Pattern pattern = Pattern.compile(passwordPolicy);
        Matcher matcher = pattern.matcher(password);
        Log.d("matcher-----", Boolean.toString(matcher.matches()));
        return matcher.matches();
    }




}
