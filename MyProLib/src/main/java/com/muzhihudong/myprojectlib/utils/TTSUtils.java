package com.muzhihudong.myprojectlib.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Administrator on 2016/5/19.
 */
public class TTSUtils {
    private static TTSUtils ttsUtils;
    private static TextToSpeech tts;

    private TTSUtils() {
    }

    public static TTSUtils getInstance(final Context context) {
        if (ttsUtils == null) {
            if (tts == null) {
                tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            int result = tts.setLanguage(Locale.CHINA);
                            if (result == TextToSpeech.LANG_MISSING_DATA
                                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                ToastUtils.showToast(context, "语言不支持");
                            }
                        }
                    }
                });
            }
            ttsUtils = new TTSUtils();
        }
        return ttsUtils;
    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
