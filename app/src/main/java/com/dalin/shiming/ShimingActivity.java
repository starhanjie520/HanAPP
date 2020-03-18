package com.dalin.shiming;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ShimingActivity extends AppCompatActivity {

    private EditText shiming_et_username;
    private EditText shiming_et_password;

    private Button shiming_btn;
    private Button shiming_cancel_btn;


    private String username ;
    private String idnumber ;
    private static final int PERMISSIONS_REQUEST_CAMERA = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiming);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ShimingActivity.this,new String[]{Manifest.permission.CAMERA},PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        shiming_et_username = (EditText)findViewById(R.id.shiming_et_username);
        shiming_et_password = (EditText)findViewById(R.id.shiming_et_password);

        shiming_btn = (Button)findViewById(R.id.shiming_btn);
        shiming_cancel_btn = (Button)findViewById(R.id.shiming_cancel_btn);

        shiming_btn.setOnClickListener(mListener);
        shiming_cancel_btn.setOnClickListener(mListener);


    }

    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shiming_btn:
                   policyVeryfy();
                   // finish();
                    break;
                case R.id.shiming_cancel_btn:
                    finish();
                    break;
            }
        }
    };

    public void policyVeryfy(){
        Log.d("qqqqqqqqqqqqq","vvvvvvvvvvvvvv");
        username = shiming_et_username.getText().toString().trim();
        idnumber = shiming_et_password.getText().toString().trim();
        Intent intent = new Intent(this, OfflineFaceLivenessActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("idnumber", idnumber);
        startActivityForResult(intent,100);
        Log.d("aaaa","bbbbbb");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
