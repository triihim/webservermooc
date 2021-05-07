package projekti.configuration;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projekti.repositories.AccountRepository;
import projekti.models.Account;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if(account == null) {
            logger.info("No user found with username: " + username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        } else {
            logger.info("User found with username: " + username);
            return new CustomUserDetails(account.getId(), account.getUsername(), account.getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
    }
    
}
