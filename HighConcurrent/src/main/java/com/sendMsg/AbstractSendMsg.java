package com.sendMsg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSendMsg {
    /**
     * 成功
     * @param msg
     */
    public abstract void success(String msg);

    /**
     * 失败
     * @param e
     * @param msg
     */
    public abstract void failure(Throwable e,String msg);

    /**
     * 发送状态
     * @param msg
     */
    public void sendStatus(String msg){
        try{
            if(msg.length()>5){
                this.failure(new Throwable("消息太长了"),msg);
                return;
            }
            this.success(msg);
        }catch (Throwable t) {
            this.failure(t,msg);
            log.error("发送未知异常：",t);
        }
    }
}