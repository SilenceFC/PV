package com.gdut.myproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gdut.myproject.PVMonitor.CurrentActivity;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

import com.gdut.myproject.Bean.API;
import com.gdut.myproject.Bean.MainEvent;
import de.greenrobot.event.EventBus;
import com.gdut.myproject.handler.MinaIoHandler;

/**
 * Created by Administrator on 2016/11/14.
 * 使用了mina框架，基于NIO的socket连接。
 */
public class MinaSocketClient {
    private static NioSocketConnector connector;
    private String result;
    private int num=0;
    private SharedPreferences sp;
    private MinaSocketClient(){
        sp = ContextUtils.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
        if(connector == null){
            connector = new NioSocketConnector();
        }
        result = null;
        connector.setConnectTimeoutMillis(3000);

        TextLineCodecFactory factory = new TextLineCodecFactory();
        factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
        factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
        connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(factory));
    }

    private static class MinaSocketClientHolder{
        private static final MinaSocketClient client = new MinaSocketClient();
    }

    public static MinaSocketClient getInstance(){
        return MinaSocketClientHolder.client;
    }

    /**
     * 打开mina客户端，向服务端发送请求
     * @param content 发送给服务端的信息
     * @return
     */
    public String openMinaSocket(String content){
        result = null;
        connector.setHandler(new MinaIoHandler(new CurrentActivity.MinaCallBack() {
            @Override
            public void onResponse(Object msg) {
                result = (String) msg;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("state",API.SUCCESS_SERVER);
                editor.commit();
            }

            @Override
            public void onError(Throwable throwable) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("state",API.ERROR_SERVER);
                editor.commit();
                throwable.printStackTrace();
            }
        }));
        ConnectFuture future = connector.connect(new InetSocketAddress(API.ADDRESS,API.PORT));
        future.awaitUninterruptibly();
        IoSession session;
        try {
            session = future.getSession();
            LogUtils.Loge("openMinaSocket","content:"+content);
            if(content!=null &&!"".equals(content)){
                session.write(content);
            }else{
                session.write("AllData");
            }
            while(result==null){
                try {
                    if(num!=200){
                        LogUtils.Loge("openMinaSocket","未跳出result循环");
                        Thread.sleep(50);
                        num++;
                    }else{
                        num=0;
                        result = "null";
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("state",API.ERROR_SERVER);
            editor.commit();
            LogUtils.Loge("minaSocketClient","未获取到session");
            EventBus.getDefault().post(new MainEvent("服务器连接异常，请检查网络设置"));
            e.printStackTrace();
        }
        LogUtils.Loge("while循环","循环结束："+result);
        return result;
    }
}
