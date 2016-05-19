package com.muzhihudong.myprojectlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.List;

/**
 * 语音识别相关工具
 */
public class VoiceAssistantUtils {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;   //startActivityForResult操作要求的标识码

    /**
     * 本地是否有语音识别程序
     *
     * @return
     */
    public static boolean hasLocalVoiceAssistant(Context context) {
        PackageManager pm = context.getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0); // 本地识别程序
        return activities.size() != 0;
    }

    public static void startVoiceRecognitionActivity(Activity activity) {
        if (!hasLocalVoiceAssistant(activity)) {
            ToastUtils.showToast("没有语音识别系统");
            return;
        }
        // 通过Intent传递语音识别的模式，开启语音
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // 语言模式和自由模式的语音识别
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // 提示语音开始
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
        // 开始语音识别
        activity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        // 调出识别界面
    }

    public static String getResultData(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString = "";
            for (int i = 0; i < results.size(); i++) {
                resultString += results.get(i);
            }
            LogUtils.writeLog("dataresult===" + resultString);
            return resultString;
        }
        return null;
    }
}
