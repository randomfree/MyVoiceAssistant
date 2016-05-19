package com.muzhihudong.myprojectlib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muzhihudong.myprojectlib.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ͨ�õ�viewhodler
 *
 * @author Administrator
 */
public class MyViewHolder {

    private final SparseArray<View> mViews;
    private View mContentView;
    private Context context;
    private View.OnClickListener onClickListener;

    private MyViewHolder(Context context, ViewGroup parent, int layoutId,
                         int position) {
        this.mViews = new SparseArray<View>();
        mContentView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mContentView.setTag(this);

        this.context = context;
    }

    public static MyViewHolder get(Context context, View contentView,
                                   ViewGroup parent, int layoutId, int position) {
        if (contentView == null) {
            return new MyViewHolder(context, parent, layoutId, position);
        }
        return (MyViewHolder) contentView.getTag();
    }

    public <T extends View> T getView(int resId) {
        View view = mViews.get(resId);
        if (view == null) {
            view = mContentView.findViewById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }


    public View getContentView() {
        return mContentView;
    }

    public MyViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public MyViewHolder setImageResource(int viewId, int resid) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resid);
        return this;
    }

    public MyViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bm);
        return this;
    }

    public MyViewHolder setImageUrl(int viewId, String url) {
        ImageView iv = getView(viewId);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = ImageUtils
                .getDefaultImageLoaderOprations();
        imageLoader.displayImage(url, iv, options);
        return this;

    }

    public MyViewHolder setImageUri(int viewId, Uri uri, Context context) {
        ImageView iv = getView(viewId);
        ImageUtils.setImageViewURIImage(uri, iv, context, true);
//        iv.setImageURI(uri);
        return this;
    }


    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
