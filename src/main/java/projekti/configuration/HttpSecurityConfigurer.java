package projekti.configuration;

import org.springframework.stereotype.Component;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Component
public class HttpSecurityConfigurer {
    
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/static/**", "/css/**", "/js/**").permitAll() // Some claim these are permitted by default. Did not work.
            .antMatchers("/login*").permitAll()
            .antMatchers("/register").permitAll()
            .antMatchers("/account").hasAnyAuthority("USER")
            .antMatchers("/photos").hasAnyAuthority("USER")
            .antMatchers("/api*").hasAnyAuthority("USER")
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/account", true)
            .and()
            .logout().logoutSuccessUrl("/");
        
        http.csrf().ignoringAntMatchers("/api/**", "/account/profile-picture");
        
        // Fixes randomly occurring session errors.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }
}
