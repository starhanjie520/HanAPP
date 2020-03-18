package com.dalin.shiming.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.dalin.shiming.Config;
import com.dalin.shiming.LoginActivity;
import com.dalin.shiming.MainActivity;
import com.dalin.shiming.MyUtil;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.dalin.shiming.WebChatLoginActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.dalin.shiming.R;

import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Log.d("ffffffffffff","ddddddddddddddd");
        api = WXAPIFactory.createWXAPI(this, Config.appid,true);
        Intent intent = getIntent();
        api.handleIntent(intent, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        String access_token = "";
        String openid  = "";
        int errorCode = resp.errCode;
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                String code = ((SendAuth.Resp) resp).code;
                String appid = Config.appid;
                String secret = Config.secret;
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
                String aa = MyUtil.get(url);
                try {
                    JSONObject jsonObject = new JSONObject(aa);
                    access_token = jsonObject.getString("access_token");
                    openid = jsonObject.getString("openid");
                }catch (Exception e){
                    openid = "error";
                }
                Log.d("------------- access_token -------- ",access_token);
                Log.d("------------- openid -------- ",openid);
                if(openid.length()>10){
                    String url2 = Config.mobile_webchat_register + "?get_type=openidexist"+"&openid="+openid;
                    String returncode = MyUtil.get(url2);
                    Log.d("------------- returncode 222 -------- ",returncode);
                    if(returncode.contains("exist")){
                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class) ;
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(WXEntryActivity.this, WebChatLoginActivity.class) ;
                        intent.putExtra("openid", openid);
                        startActivity(intent);
                        finish();

                    }
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class) ;
                startActivity(intent);
                Toast.makeText(this, "WebChat Login Fail",Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Intent intent2 = new Intent(WXEntryActivity.this, LoginActivity.class) ;
                startActivity(intent2);
                Toast.makeText(this, "WebChat Login Fail",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


}
