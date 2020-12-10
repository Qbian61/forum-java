package pub.developers.forum.starter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "pub.developers.forum")
@MapperScan(value = {"pub.developers.forum.infrastructure.dal.dao"})
public class ForumJavaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ForumJavaApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ForumJavaApplication.class);
	}
}
