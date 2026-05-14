package gift.goblin.timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TimesheetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetServiceApplication.class, args);
	}

}
