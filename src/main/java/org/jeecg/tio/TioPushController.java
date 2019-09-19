package org.jeecg.tio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tio.core.Tio;
import org.tio.utils.hutool.StrUtil;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(tags="推送指令")
@RequestMapping("/tio/push")
public class TioPushController {
	
	@Autowired
    private TioWebSocketServerBootstrap bootstrap;

	@ApiOperation(value="移动端推送指令给机器人平板", notes="推送指令")
    @GetMapping("/msg")
    public void pushMessage(String robotId, String msg){
        if (StrUtil.isEmpty(msg)){
            msg = "hello tio websocket spring boot starter";
        }
        log.info("robotId:"+robotId + " msg:"+msg);
       
        msg = "{name:'" + robotId + "',message:'" + msg + "'}";
        
        WsResponse response = WsResponse.fromText(msg,"utf-8");
        Tio.sendToBsId(bootstrap.getServerTioConfig(), robotId, response);
    }

}
