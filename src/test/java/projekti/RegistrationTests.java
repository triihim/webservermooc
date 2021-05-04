package projekti;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import projekti.repositories.AccountRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Test
    public void registrationSucceedsWithProperCredentials() throws Exception {
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "fname")
                .param("lastName", "lname")
                .param("username", "uname")
                .param("password", "password1234")
                .param("passwordConfirm", "password1234"))
                    .andExpect(redirectedUrl("/login"))
                    .andExpect(status().is3xxRedirection());
        
        Assert.assertTrue(accountRepository.findByUsername("uname") != null);
    }
    
    @Test
    public void registrationFailsWithNonMatchingPasswords() throws Exception {
        MvcResult result = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "fname")
                .param("lastName", "lname")
                .param("username", "uname2")
                .param("password", "password1234")
                .param("passwordConfirm", "invalid"))
                    .andReturn();
        
        String content = result.getResponse().getContentAsString();
        
        Assert.assertTrue(content.contains("passwords do not match"));
        Assert.assertTrue(accountRepository.findByUsername("uname2") == null);
    }
            
    
}
