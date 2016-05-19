package com.muzhihudong.myvoiceassistant.adapter;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muzhihudong.myvoiceassistant.Bean.MessageBean;
import com.muzhihudong.myvoiceassistant.R;

import java.util.ArrayList;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {

    private ArrayList<MessageBean> mData;
    private Context context;

    public MessageListAdapter(Context context, ArrayList<MessageBean> mData) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_list, null);
        MessageListViewHolder holder = new MessageListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageListViewHolder holder, int position) {
        MessageBean bean = mData.get(position);
        if (bean.type == MessageBean.MESSAGE_TYPE_CONTENT) {
            if (bean.direction == MessageBean.MESSAGE_DIRECTION_LEFT) {
                holder.leftTv.setVisibility(View.VISIBLE);
                holder.leftTv.setText(bean.message);
                holder.rightTv.setVisibility(View.GONE);
            }
            if (bean.direction == MessageBean.MESSAGE_DIRECTION_RIGHT) {
                holder.rightTv.setVisibility(View.VISIBLE);
                holder.rightTv.setText(bean.message);
                holder.leftTv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MessageListViewHolder extends RecyclerView.ViewHolder {
        private TextView leftTv;
        private TextView rightTv;

        public MessageListViewHolder(View itemView) {
            super(itemView);
            leftTv = (TextView) itemView.findViewById(R.id.message_left_tv);
            rightTv = (TextView) itemView.findViewById(R.id.message_right_tv);
        }
    }
}
