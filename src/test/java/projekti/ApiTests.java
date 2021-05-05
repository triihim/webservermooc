package projekti;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import projekti.repositories.AccountRepository;
import org.junit.Assert;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRepository;
    
    private MockHttpSession session;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Before
    public void setup() throws Exception {
        TestUtils.registerTestUser();
        TestUtils.populateMockAccounts();
        session = TestUtils.loginTestUser(mockMvc);
    }
    
    @Test
    public void searchAccountNamesWithoutFilterReturnsAll() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/accounts").session(session))
                .andExpect(status().isOk())
                .andReturn();
        
        String resultStr = result.getResponse().getContentAsString();
        List<String> accountNames = mapper.readValue(resultStr, new TypeReference<List<String>>(){});
        
        Assert.assertEquals(accountNames.size(), accountRepository.findAll().size());
    }
    
    @Test
    public void searchAccountNamesWithFilterReturnsCorrectNames() throws Exception {
        String filter = "js"; // Should match the two usernames: jsmith and jschultz. (populated in TestUtils.populateMockAccounts).
        
        MvcResult result = mockMvc.perform(get("/api/accounts?filter=" + filter).session(session))
                .andExpect(status().isOk())
                .andReturn();
        
        String resultStr = result.getResponse().getContentAsString();
        List<String> accountNames = mapper.readValue(resultStr, new TypeReference<List<String>>(){});
        
        Assert.assertEquals(2, accountNames.size());
    }
 
}
