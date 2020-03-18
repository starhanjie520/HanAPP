package com.dalin.shiming;

import android.util.Log;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.HashMap;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class MyUtil {



    public static String get(String url){
        String rresponse = "";
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            rresponse = result;

        }catch (Exception e){
            e.printStackTrace();
            rresponse = "error";

        }
        return rresponse;
    }

    public String post(String url){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                // .add(key,value)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    try {

                    }catch (Exception e){
                    }
                }
            }
        });
        return "";
    }

    public static String SendSMS(String ccountry , String telphoneNum,String text){

        String country = ccountry;
        Log.d("sssssws----------------",country);

        String api_key = Config.sms_api_key;
        String api_secret = Config.sms_api_secret;
        String sms_frome = Config.sms_frome;
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", telphoneNum); // 수신번호
        params.put("from", sms_frome); // 발신번호
        params.put("type", "SMS"); // Message type ( SMS, LMS, MMS, ATA )
        params.put("text", "본인확인 인증번호 ["+text+"]를 입력해주세요."); // 문자내용
        //   params.put("app_version", "JAVA SDK v1.2"); // application name and version

        // Optional parameters for your own needs
        // params.put("image", "desert.jpg"); // image for MMS. type must be set as "MMS"
        // params.put("image_encoding", "binary"); // image encoding binary(default), base64
        // params.put("mode", "test"); // 'test' 모드. 실제로 발송되지 않으며 전송내역에 60 오류코드로 뜹니다. 차감된 캐쉬는 다음날 새벽에 충전 됩니다.
        // params.put("delay", "10"); // 0~20사이의 값으로 전송지연 시간을 줄 수 있습니다.
        // params.put("force_sms", "true"); // 푸시 및 알림톡 이용시에도 강제로 SMS로 발송되도록 할 수 있습니다.
        // params.put("refname", ""); // Reference name
         params.put("country", country); // Korea(KR) Japan(JP) America(USA) China(CN) Default is Korea
        // params.put("sender_key", "5554025sa8e61072frrrd5d4cc2rrrr65e15bb64"); // 알림톡 사용을 위해 필요합니다. 신청방법 : http://www.coolsms.co.kr/AboutAlimTalk
        // params.put("template_code", "C004"); // 알림톡 template code 입니다. 자세한 설명은 http://www.coolsms.co.kr/AboutAlimTalk을 참조해주세요.
        // params.put("datetime", "20140106153000"); // Format must be(YYYYMMDDHHMISS) 2014 01 06 15 30 00 (2014 Jan 06th 3pm 30 00)
        // params.put("mid", "mymsgid01"); // set message id. Server creates automatically if empty
        // params.put("gid", "mymsg_group_id01"); // set group id. Server creates automatically if empty
        // params.put("subject", "Message Title"); // set msg title for LMS and MMS
        // params.put("charset", "euckr"); // For Korean language, set euckr or utf-8
        // params.put("app_version", "Purplebook 4.1") // 어플리케이션 버전

        try {
            JSONObject obj =coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

        return "";
    }




}
