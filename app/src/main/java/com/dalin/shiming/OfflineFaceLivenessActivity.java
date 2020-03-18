/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.dalin.shiming;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.dalin.shiming.faceid.APIService;
import com.dalin.shiming.faceid.AccessToken;
import com.dalin.shiming.faceid.FaceException;
import com.dalin.shiming.faceid.LivenessVsIdcardResult;
import com.dalin.shiming.faceid.OnResultListener;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.baidu.idl.face.platform.utils.Base64Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OfflineFaceLivenessActivity extends FaceLivenessActivity {

    private String bestImagePath;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int PERMISSIONS_REQUEST_CAMERA = 800;
    private static final int PERMISSIONS_EXTERNAL_STORAGE = 801;
    private boolean waitAccesstoken = true;


    private String username;
    private String idnumber;

    public Boolean PolicyResult = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(OfflineFaceLivenessActivity.this,new String[]{Manifest.permission.CAMERA},PERMISSIONS_REQUEST_CAMERA);
//            return;
//        }
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            idnumber = intent.getStringExtra("idnumber");
        }
        initAccessToken();
        initFaceSDK();
        setFaceConfig();
    }

    /**
     * 初始化SDK
     */
    private void initFaceSDK() {
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
    }
    private void initAccessToken() {
        APIService.getInstance().init(this);
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                Log.i("wtf", "AccessToken->" + result.getAccessToken());
            }
            @Override
            public void onError(FaceException error) {
                Log.e("xx", "AccessTokenError:" + error);
                error.printStackTrace();
            }
        }, Config.apiKey, Config.secretKey);
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        List<LivenessTypeEnum> livenessList = new ArrayList<>();
        livenessList.add(LivenessTypeEnum.Mouth);
        livenessList.add(LivenessTypeEnum.Eye);
        // livenessList.add(LivenessTypeEnum.HeadUp);
        // livenessList.add(LivenessTypeEnum.HeadDown);
        // livenessList.add(LivenessTypeEnum.HeadLeft);
        // livenessList.add(LivenessTypeEnum.HeadRight);
        config.setLivenessTypeList(livenessList);

        // 设置 活体动作是否随机 boolean
        config.setLivenessRandom(true);
        // 模糊度范围 (0-1) 推荐小于0.7
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        // 光照范围 (0-1) 推荐大于40
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        // 人脸置信度（0-1）推荐大于0.6
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        // 是否进行质量检测
        config.setCheckFaceQuality(true);
        // 人脸检测使用线程数
        // config.setFaceDecodeNumberOfThreads(4);
        // 是否开启提示音
        config.setSound(true);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
             Toast.makeText(this, "活体检测成功", Toast.LENGTH_SHORT).show();
            saveImage(base64ImageMap);
            policeVerify(bestImagePath);
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
             Toast.makeText(this, "活体检测采集超时", Toast.LENGTH_SHORT).show();
            resultIntent();
        }
    }

    private void policeVerify(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            PolicyResult = false;
        }
        APIService.getInstance().policeVerify(username, idnumber, filePath, new
                OnResultListener<LivenessVsIdcardResult>() {
                    @Override
                    public void onResult(LivenessVsIdcardResult result) {
                        if (result != null && result.getScore() >= 80) {
                            delete();
                            PolicyResult = true;
                            Log.d("核身成功 公安验证分数success----------",""+result.getScore());
                            resultIntent();
                        } else {
                            PolicyResult = false;
                            Log.d("公安验证分数过低fail----------",""+result.getScore());
                            resultIntent();
                        }
                    }
                    @Override
                    public void onError(FaceException error) {
                        delete();
                        PolicyResult = false;
                        Log.d("fail--公安身份核实失败--------","");
                        resultIntent();
                    }
                }

        );
    }

    public void resultIntent(){

        Intent intent = new Intent();
        intent.putExtra("result", "some value");
        if(PolicyResult){
            setResult(Activity.RESULT_OK, intent);
        }else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();

    }


    @Override
    public void finish() {
        super.finish();
    }

    private void saveImage(HashMap<String, String> imageMap) {

        String bestimageBase64 = imageMap.get("bestImage0");
        Bitmap bmp = base64ToBitmap(bestimageBase64);
        try {
            File file = File.createTempFile("face", ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

            bestImagePath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void delete() {
        File file = new File(bestImagePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}