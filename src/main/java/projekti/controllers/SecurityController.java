package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.DTO.RegistrationDTO;
import projekti.helpers.SecurityHelper;
import projekti.services.AccountService;

@Controller
public class SecurityController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/login")
    public String loginPage() {
        if(SecurityHelper.isLoggedIn()) {
            System.out.println("LOGGED IN: " + SecurityHelper.requesterUsername());
            return "redirect:/";
        }
        return "login";
    }
    
    @GetMapping("/register")
    public String registrationPage(@ModelAttribute RegistrationDTO registrationDTO) {
        if(SecurityHelper.isLoggedIn()) return "redirect:/";
        return "registration";
    }
    
    @PostMapping("/register")
    public String register(@Validated @ModelAttribute RegistrationDTO dto, BindingResult bindingResult, Model model) {
        
        if(!dto.passwordsMatch()) {
            bindingResult.rejectValue("password", "registration.error", "passwords do not match");
        }
        
        if(accountService.getAccount(dto.getUsername()) != null) {
            bindingResult.rejectValue("username", "registration.error", "username already taken");
        }
        
        if(bindingResult.hasErrors()) {
            return "registration";
        }
        
        accountService.register(dto);
        
        return "redirect:/login";
    }
    
}
