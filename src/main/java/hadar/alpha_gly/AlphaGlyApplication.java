package hadar.alpha_gly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // for the scheduler
public class AlphaGlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphaGlyApplication.class, args);
	}

}
