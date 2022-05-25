package com.xgw.serverFireWall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.xgw.serverFireWall.**"})
public class ServerFireWallApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerFireWallApplication.class, args);
	}

}
