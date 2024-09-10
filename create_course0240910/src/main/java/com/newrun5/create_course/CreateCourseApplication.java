package com.newrun5.create_course;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.newrun5.create_course") // 기본 패키지 설정
@MapperScan("com.newrun5.create_course.dao") // Mapper가 위치한 패키지 설정
public class CreateCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateCourseApplication.class, args);
	}

}
