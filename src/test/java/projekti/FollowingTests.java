package projekti;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import projekti.repositories.AccountRepository;
import org.junit.Assert;
import projekti.repositories.FollowingRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FollowingTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    @Before
    public void setup() throws Exception {
        TestUtils.registerTestUser();
        TestUtils.populateMockAccounts();
    }
 
    @Test
    public void usersCanToggleFollowing() throws Exception {
        // tester follows jsmith.
        MockHttpSession session = TestUtils.loginTestUser(mockMvc);
        mockMvc.perform(post("/api/following/toggle-follow/jsmith").session(session)).andExpect(status().isOk());
        
        Long follower = accountRepository.findByUsernameIgnoreCase("tester").getId();
        Long followee = accountRepository.findByUsernameIgnoreCase("jsmith").getId();
        
        Assert.assertTrue(followingRepository.isFollowing(followee, follower));
        
        // tester unfollows jsmith.
        mockMvc.perform(post("/api/following/toggle-follow/jsmith").session(session)).andExpect(status().isOk());
        Assert.assertFalse(followingRepository.isFollowing(followee, follower));
    }
    
    @Test
    public void usersCanBeBlockedAndUnblocked() throws Exception {
        Long followee = accountRepository.findByUsernameIgnoreCase("tester").getId();
        Long follower = accountRepository.findByUsernameIgnoreCase("jsmith").getId();
        
        // Ensure jsmith follows tester
        if(followingRepository.isFollowing(followee, follower) == false) {
            MockHttpSession jsmithSession = TestUtils.loginUser(mockMvc, "jsmith", "password1234");
            mockMvc.perform(post("/api/following/toggle-follow/tester").session(jsmithSession)).andExpect(status().isOk());
        }
        
        // Tester blocks follower jsmith
        MockHttpSession testerSession = TestUtils.loginTestUser(mockMvc);
        mockMvc.perform(post("/api/following/toggle-block/jsmith").session(testerSession)).andExpect(status().isOk());
        Assert.assertTrue(followingRepository.isEitherBlocking(followee, follower));
        
        // Tester unblocks follower jsmith
        mockMvc.perform(post("/api/following/toggle-block/jsmith").session(testerSession)).andExpect(status().isOk());
        Assert.assertFalse(followingRepository.isEitherBlocking(followee, follower));
    }

}
