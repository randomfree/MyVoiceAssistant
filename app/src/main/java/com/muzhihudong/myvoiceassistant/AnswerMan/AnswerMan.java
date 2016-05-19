package com.muzhihudong.myvoiceassistant.AnswerMan;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AnswerMan {

    private static AnswerMan answerMan;

    private AnswerMan() {
    }

    public static AnswerMan getInstance() {
        if (answerMan == null) {
            answerMan = new AnswerMan();
        }
        return answerMan;
    }

    public String getAnswer(String str) {
        return "不知道";
    }

}
