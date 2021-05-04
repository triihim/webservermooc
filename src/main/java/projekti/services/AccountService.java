package projekti.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekti.DTO.AccountDTO;
import projekti.DTO.RegistrationDTO;
import projekti.repositories.AccountRepository;
import projekti.models.Account;

@Service
public class AccountService {
    
    Logger logger = LoggerFactory.getLogger(AccountService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void register(RegistrationDTO dto) {
        logger.info("Registering user: " + dto.getUsername());
        Account account = new Account();
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        accountRepository.save(account);
        logger.info("Registered user: " + dto.getUsername());
    }
    
    public AccountDTO getAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        
        if(account == null) return null;
        
        AccountDTO dto = new AccountDTO();
        
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setUsername(account.getUsername());
        dto.setProfilePictureId(account.getProfilePictureId());
        
        return dto;
    }
    
}
