package projekti.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import projekti.DTO.AccountDTO;
import projekti.DTO.UserSearchDTO;
import projekti.services.AccountService;
import projekti.helpers.SecurityHelper;
import projekti.services.FollowingService;

@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private FollowingService followingService;
    
    @GetMapping("/account")
    public String accountPageRedirect(Authentication authentication) {
        return "redirect:/account/" + authentication.getName();
    }
    
    @GetMapping("/account/{username}")
    public String accountPage(Model model, @PathVariable String username, Authentication authentication) {
        AccountDTO dto = accountService.getAccount(username);
        
        if(dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        model.addAttribute("owner", SecurityHelper.accessorIsLoggedInUser(username));
        model.addAttribute("account", dto);
        model.addAttribute("isBlocked", followingService.isBlocked(username, SecurityHelper.requesterUsername()));
        
        return "profile";
    }
    
    @GetMapping("/account-search")
    public String usersPage(@RequestParam String query, @RequestParam Optional<Integer> page, Model model) {
        if(page.isPresent() && page.get() < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page cannot be negative");
        UserSearchDTO result = accountService.getAccountNamesContaining(query, page.orElse(0));
        model.addAttribute("users", result.getUsers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("totalPages", result.getTotalPages());
        return "users";
    }
    
    @PostMapping("/account/profile-picture")
    public String setProfilePicture(@RequestParam("photoId") Long photoId) {
        accountService.setProfilePicture(photoId);
        return "redirect:/";
    }
    
    
}
