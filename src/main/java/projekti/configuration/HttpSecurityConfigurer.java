package projekti.configuration;

import org.springframework.stereotype.Component;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Component
public class HttpSecurityConfigurer {
    
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/register").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/account").hasAnyAuthority("USER")
            .antMatchers("/api").hasAnyAuthority("USER")
            .anyRequest().authenticated();

            http.formLogin().permitAll().defaultSuccessUrl("/account");
            http.logout();
    }
    
}
