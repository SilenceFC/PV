package com.gdut.myproject.handler;

import android.util.Log;

import com.gdut.myproject.PVMonitor.CurrentActivity;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.gdut.myproject.utils.ContextUtils;
import com.gdut.myproject.utils.ParseUtil;

/**
 * Created by Administrator on 2016/11/14.
 */
public class MinaIoHandler extends IoHandlerAdapter {
    private String result;
    private CurrentActivity.MinaCallBack mCallBack;
    public MinaIoHandler(){}
    public MinaIoHandler(CurrentActivity.MinaCallBack callBack){
        mCallBack = callBack;
    }
    @Override
    public void inputClosed(IoSession session) throws Exception {
        session.close();
        Log.e("MinaIoHandler", "inputClosed:已关闭sisson");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        Log.e("MinaIoHandler", "~~~messageSent~~~");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        result = (String) message;
        Log.e("MinaIoHandler", "~~~messageReceived~~~"+result);
//        EventBus.getDefault().post(new DataEvent(result));
        if(result.contains("data")){
            ParseUtil.getInstance().parseData(ContextUtils.getInstance(),result);
        }
        if(mCallBack!=null){
           mCallBack.onResponse(result);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session,cause);
        Log.e("MinaIoHandler", "~~~exceptionCaught~~~");
        mCallBack.onError(cause);
        cause.printStackTrace();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        Log.e("MinaIoHandler", "~~~sessionIdle~~~");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.e("MinaIoHandler", "~~~sessionClosed~~~");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        Log.e("MinaIoHandler", "~~~sessionOpened~~~");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        Log.e("MinaIoHandler", "~~~sessionCreated~~~");
    }

}
