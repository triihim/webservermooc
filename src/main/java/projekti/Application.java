package projekti;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {

    @Autowired
    Environment env;
    
    @PostConstruct
    public void printInfo() {
        String[] profiles = env.getActiveProfiles();
        System.out.println("ACTIVE PROFILES: " + profiles.length);
        for(String profile : profiles) {
            System.out.println(profile);
        }
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
