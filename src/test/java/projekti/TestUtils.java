package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import projekti.DTO.RegistrationDTO;
import projekti.services.AccountService;

@Service
public class TestUtils {
    
    private static AccountService accountService;
    
    private static boolean isTestUserRegistered = false;
    private static boolean isMockAccountsPopulated = false;
    
    @Autowired
    private TestUtils(AccountService accountService) {
        TestUtils.accountService = accountService;
    }
    
    public static void registerTestUser() {
        if(isTestUserRegistered) return;
        RegistrationDTO dto = new RegistrationDTO();
        dto.setFirstName("testi");
        dto.setLastName("testaaja");
        dto.setUsername("tester");
        dto.setPassword("password1234");
        dto.setPasswordConfirm("password1234");
        accountService.register(dto);
        isTestUserRegistered = true;
    }
    
    public static MockHttpSession loginTestUser(MockMvc mockMvc) throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "tester")
            .param("password", "password1234"))
                .andReturn();
        
        return (MockHttpSession) result.getRequest().getSession(false);
    }
    
    public static void populateMockAccounts() {
        if(isMockAccountsPopulated) return;
        List<RegistrationDTO> mocks = new ArrayList<>();
        mocks.add(createMockAccount("John", "Smith", "jsmith"));
        mocks.add(createMockAccount("Ned", "Currey", "mcurrey"));
        mocks.add(createMockAccount("Penelope", "Davis", "pdavis"));
        mocks.add(createMockAccount("Bridget", "Macy", "bmacy"));
        mocks.add(createMockAccount("Jane", "Schultz", "jschults"));
        mocks.forEach(mock -> accountService.register(mock));
        isMockAccountsPopulated = true;
    }
    
    private static RegistrationDTO createMockAccount(String fname, String lname, String uname) {
        return new RegistrationDTO(fname, lname, uname, "password1234", "password1234");
    }
    
}
