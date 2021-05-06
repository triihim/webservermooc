package projekti;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
