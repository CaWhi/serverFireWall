package com.xgw.serverFireWall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.xgw.serverFireWall.**"})
@MapperScan("com.xgw.serverFireWall.dao.mapper")
public class ServerFireWallApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerFireWallApplication.class, args);
	}

}
