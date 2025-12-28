package com.example.AirbnbSpring;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class AirbnbSpringApplication {

	public static void main(String[] args) {


		Dotenv dotenv=Dotenv.configure().load();

		dotenv.entries().forEach(  entry-> System.setProperty(entry.getKey(),entry.getValue()));

		SpringApplication.run(AirbnbSpringApplication.class, args);

	}

	//1️⃣ JVM starts
	//2️⃣ main() starts
	//3️⃣ Dotenv library reads .env
	//4️⃣ System.setProperty(KEY, VALUE)
	//5️⃣ Spring Boot starts
	//6️⃣ Spring reads application.properties
	//7️⃣ ${PORT}, ${DB_URL} get resolved
	//8️⃣ Beans are created
	//9️⃣ App starts

}
