package org.realEstate.myRealEstateService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RequestMapping("/api")
public class MyRealEstateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRealEstateServiceApplication.class, args);
	}

}
