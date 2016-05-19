package com.muzhihudong.myprojectlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class MyAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected Context context;
    protected List<T> mData;
    protected int layoutId;
    protected LinearLayout layout;
    protected ArrayList<View> views = new ArrayList<>();

    public MyAdapter(Context context, List<T> mdata, int layoutId) {
        this.context = context;
        this.mData = mdata;
        if (context != null) {
            inflater = LayoutInflater.from(context);
        }
        this.layoutId = layoutId;
    }


    @Override
    public int getCount() {
//        LogUtils.writeLog("========"+mData.size());\*/
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean b = false;
        if (convertView == null) {
            b = true;
        }
        MyViewHolder holder = MyViewHolder.get(context, convertView, parent,
                layoutId, position);

        if (b) {
            views.add(holder.getContentView());
        }
        convert(holder, getItem(position), position);
        return holder.getContentView();
    }

    public abstract void convert(MyViewHolder holder, T item, int position);

    public void recyclerViews() {

    }


    public ArrayList<View> getContentViews() {
        return views;
    }
}
