package com.muzhihudong.myvoiceassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.muzhihudong.myprojectlib.BaseActivity;
import com.muzhihudong.myprojectlib.utils.VoiceAssistantUtils;
import com.muzhihudong.myvoiceassistant.AnswerMan.AnswerMan;
import com.muzhihudong.myvoiceassistant.Bean.MessageBean;
import com.muzhihudong.myvoiceassistant.adapter.MessageListAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private ArrayList<MessageBean> messages = new ArrayList<>();
    private Toolbar toolbar;
    RecyclerView recyclerView;
    MessageListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messages.add(new MessageBean("aaa", MessageBean.MESSAGE_DIRECTION_RIGHT));
        messages.add(new MessageBean("aaa", MessageBean.MESSAGE_DIRECTION_LEFT));
        initView();

    }

    private void initView() {
        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        adapter = new MessageListAdapter(MainActivity.this, messages);
        recyclerView.setAdapter(adapter);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        //click
        findViewById(R.id.main_say_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_say_btn:
                VoiceAssistantUtils.startVoiceRecognitionActivity(MainActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String str = VoiceAssistantUtils.getResultData(requestCode, resultCode, data);
        MessageBean messageBean = new MessageBean(str, MessageBean.MESSAGE_DIRECTION_RIGHT);
        messages.add(messageBean);
        adapter.notifyDataSetChanged();
        String answer = AnswerMan.getInstance().getAnswer(str);
        MessageBean answerBean = new MessageBean(answer, MessageBean.MESSAGE_DIRECTION_LEFT);
        messages.add(answerBean);
        adapter.notifyDataSetChanged();
    }
}
