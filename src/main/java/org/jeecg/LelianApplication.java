package org.jeecg;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.tio.websocket.starter.EnableTioWebSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableTioWebSocketServer
@SpringBootApplication
public class LelianApplication {
	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(LelianApplication.class, args);
	    Environment env = application.getEnvironment();
	    String ip = InetAddress.getLocalHost().getHostAddress();
	    String port = env.getProperty("server.port");
	    String path = env.getProperty("server.servlet.context-path");
	    log.info("\n----------------------------------------------------------\n\t" +
	            "Application Jeecg-Boot-Lelian is running! Access URLs:\n\t" +
	            "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
	            "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
	            "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
	            "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
	            "----------------------------------------------------------");
	}
}
