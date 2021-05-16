package projekti;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
        TestUtils.registerTestUser();
    }
    
    @Test
    public void loginRouteIsAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }
    
    @Test
    public void registrationRouteIsAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/register")).andExpect(status().isOk());
    }
  
    @Test
    public void accountRouteIsNotAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/account")).andExpect(redirectedUrlPattern("**/login")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/account/tester")).andExpect(redirectedUrlPattern("**/login")).andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void photosRouteIsNotAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/photos")).andExpect(redirectedUrlPattern("**/login")).andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void apiIsNotAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/ping")).andExpect(redirectedUrlPattern("**/login")).andExpect(status().is3xxRedirection());
    }

}
