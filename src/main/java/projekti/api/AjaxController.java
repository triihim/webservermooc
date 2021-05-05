package projekti.api;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projekti.DTO.PostDTO;
import projekti.services.AccountService;

@RestController
@RequestMapping("/api")
public class AjaxController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/accounts")
    public List<String> accounts(@RequestParam Optional<String> filter) {
        return accountService.getAccountNamesContaining(filter.orElse(""));
    }
    
    @PostMapping("/posts")
    public PostDTO createPost(@RequestBody PostDTO dto) {
        
    }
    
}
