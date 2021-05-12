package projekti.services;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekti.DTO.AccountDTO;
import projekti.DTO.RegistrationDTO;
import projekti.DTO.UserSearchDTO;
import projekti.helpers.SecurityHelper;
import projekti.repositories.AccountRepository;
import projekti.models.Account;
import projekti.models.Photo;
import projekti.repositories.PhotoRepository;

@Service
public class AccountService {
    
    Logger logger = LoggerFactory.getLogger(AccountService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void register(RegistrationDTO dto) {
        logger.info("Registering user: " + dto.getUsername());
        Account account = new Account();
        account.setFirstName(capitalize(dto.getFirstName()));
        account.setLastName(capitalize(dto.getLastName()));
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        accountRepository.save(account);
        logger.info("Registered user: " + dto.getUsername());
    }
    
    public AccountDTO getAccount(String username) {
        Account account = accountRepository.findByUsernameIgnoreCase(username);
        
        if(account == null) return null;
        
        AccountDTO dto = new AccountDTO();
        
        dto.setFirstName(account.getFirstName());
        dto.setLastName(account.getLastName());
        dto.setUsername(account.getUsername());
        dto.setProfilePictureId(account.getProfilePictureId());
        
        return dto;
    }
    
    public UserSearchDTO getAccountNamesContaining(String partial, int page) {
        final int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("lastName").ascending().and(Sort.by("firstName").ascending()));
        List<AccountDTO> accounts = accountRepository.findByPartialNameOrUsername(partial, pageable)
                .stream()
                .map(account -> {
                    AccountDTO dto = new AccountDTO();
                    dto.setFirstName(account.getFirstName());
                    dto.setLastName(account.getLastName());
                    dto.setUsername(account.getUsername());
                    dto.setProfilePictureId(account.getProfilePictureId());
                    return dto;
                })
                .collect(Collectors.toList());
        
        int totalPages = partial.length() > 0 ? (accounts.size() + pageSize - 1) / pageSize
                : (int)(accountRepository.count() + pageSize - 1) / pageSize;
        
        totalPages += totalPages == 0 ? 1 : 0;
        
        if(page > totalPages) throw new RuntimeException("Page cannot exceed total pages");
        
        return new UserSearchDTO(accounts, partial, page, totalPages);
    }
    
    public void setProfilePicture(long photoId) {
        Account account = accountRepository.getOne(SecurityHelper.requesterId());
        Photo photo = photoRepository.findWithOwnerById(photoId);
        
        logger.info(SecurityHelper.requesterUsername() + " attempts to set profile picture to photo: " + photo.getId());
        
        if(photo == null) {
            throw new RuntimeException("No photo found with id " + photoId);
        }
        
        if(!photo.getOwner().getId().equals(account.getId())) {
            throw new RuntimeException("Photo " + photoId + " is not owned by " + account.getUsername());
        }
        
        account.setProfilePicture(photo);
        accountRepository.save(account);
    }
    
    private String capitalize(String str) {
        return  str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
}
