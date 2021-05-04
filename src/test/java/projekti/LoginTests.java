package projekti;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import projekti.DTO.RegistrationDTO;
import projekti.services.AccountService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountService accountService;
    
    private static boolean isInitialized = false;
    
    @Before
    public void setup() {
        // Workaround to initialize user in the db only once. 
        // @BeforeAll, @BeforeClass has issues with @Autowired services.
        if(!isInitialized) {
            RegistrationDTO dto = new RegistrationDTO();
            dto.setFirstName("testi");
            dto.setLastName("testaaja");
            dto.setUsername("tester");
            dto.setPassword("password1234");
            dto.setPasswordConfirm("password1234");
            accountService.register(dto);
            isInitialized = true;
        }
    }
    
    @Test
    public void loginFailsWithIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "tester")
            .param("password", "1234"))
                .andExpect(redirectedUrl("/login?error"));
    }
    
    @Test
    public void loginSucceedsWithCorrectCredentials() throws Exception {
        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "tester")
            .param("password", "password1234"))
                .andExpect(redirectedUrl("/account"));
    }
  
    
}
