package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import projekti.DTO.AccountDTO;
import projekti.DTO.RegistrationDTO;
import projekti.services.AccountService;

@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/register")
    public String registrationPage(@ModelAttribute RegistrationDTO registrationDTO) {
        return "registration";
    }
    
    @PostMapping("/register")
    public String register(@Validated @ModelAttribute RegistrationDTO dto, BindingResult bindingResult, Model model) {
        
        if(!dto.passwordsMatch()) {
            bindingResult.rejectValue("password", "registration.error", "passwords do not match");
        }
        
        if(bindingResult.hasErrors()) {
            return "registration";
        }
        
        accountService.register(dto);
        
        return "redirect:/login";
    }
    
    @GetMapping("/account")
    public String accountPageRedirect(Authentication authentication) {
        return "redirect:/account/" + authentication.getName();
    }
    
    @GetMapping("/account/{username}")
    public String accountPage(Model model, @PathVariable String username) {
        AccountDTO dto = accountService.getAccount(username);
        
        if(dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        model.addAttribute("account", dto);
        return "account";
    }
    
}
