package org.jeecg.tio;

import java.util.Objects;

import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import lombok.extern.slf4j.Slf4j;

@TioServerMsgHandler
@Slf4j
public class SpringBootWebSocketMsgHandler implements IWsMsgHandler {
	@Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
		String robotId = httpRequest.getParam("robotId");
		log.info("收到"+robotId+"的握手协议");
		//Tio.bindUser(channelContext, robotId);
    	Tio.bindBsId(channelContext, robotId); // BS ID 和 ChannelContext 是一对一的
		return httpResponse;
    }

	/**
	 * 绑定机器人和channelContext
	 * ws客户端在连接服务端的url中必须带上机器人的ID
	 */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
    	//channelContext.setAttribute('user', user); // ChannelContext 中存储用户, 就不需要使用其他数据结构来保存了
    	String robotId = httpRequest.getParam("robotId");
    	Tio.bindBsId(channelContext, robotId); // BS ID 和 ChannelContext 是一对一的
    	log.info("握手成功->"+robotId);
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
    	log.info("接收到bytes消息");
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
    	log.info(channelContext.getBsId()+"断开连接");
    	Tio.remove(channelContext, "receive close flag");
    	return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {
    	WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();
		HttpRequest httpRequest = wsSessionContext.getHandshakeRequest();//获取websocket握手包
		log.info("握手包:{}", httpRequest);

		if (Objects.equals("心跳内容", s)) {
			return null;
		}
		
		String msg = "{name:'" + channelContext.getBsId() + "',message:'" + s + "'}";


		log.info("接收到"+channelContext.getBsId()+"发送的文本消息："+msg);
		
        //Tio.sendToAll(channelContext.getTioConfig(), WsResponse.fromText("服务端收到了消息："+s,"utf-8"));
		Tio.sendToBsId(channelContext.getTioConfig(), channelContext.getBsId(), WsResponse.fromText(msg,"utf-8"));
		return null;
    }
}
