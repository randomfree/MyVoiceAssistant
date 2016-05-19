package com.muzhihudong.myvoiceassistant.Bean;

/**
 * Created by Administrator on 2016/5/19.
 */
public class MessageBean {
    /**
     * 消息的类型，普通聊天
     */
    public static final int MESSAGE_TYPE_CONTENT = 1;
    //消息方向
    public static final int MESSAGE_DIRECTION_LEFT = 2;
    public static final int MESSAGE_DIRECTION_RIGHT = 3;


    public String message;//消息
    public int direction;//消息方向
    public int type;

    /**
     * 默认消息方式是普通聊天的构造
     *
     * @param message   消息
     * @param derection {@link MessageBean#MESSAGE_DIRECTION_LEFT} or {@link MessageBean#MESSAGE_DIRECTION_RIGHT} 左边或者右边
     */
    public MessageBean(String message, int derection) {
        this.message = message;
        this.direction = derection;
        this.type = MESSAGE_TYPE_CONTENT;
    }


}
