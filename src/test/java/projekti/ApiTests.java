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
import org.springframework.http.MediaType;
import projekti.repositories.FollowingRepository;
import projekti.repositories.PostRepository;
import projekti.services.PostService;
import projekti.models.Post;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PostRepository postRepository;
    
    private MockHttpSession session;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Before
    public void setup() throws Exception {
        TestUtils.registerTestUser();
        TestUtils.populateMockAccounts();
        session = TestUtils.loginTestUser(mockMvc);
    }
    
    @Test
    public void postCreationSucceedsThroughApi() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/posts").session(session)
            .contentType(MediaType.APPLICATION_JSON).content("{ \"content\": \"test content\" }"))
                .andExpect(status().isOk())
                .andReturn();
        
        String resultStr = result.getResponse().getContentAsString();
        Assert.assertTrue(resultStr.contains("test content"));
        Assert.assertTrue(resultStr.contains("tester"));
    }
    
    @Test
    public void postWithoutContentFailsThroughApi() throws Exception {
        mockMvc.perform(post("/api/posts").session(session)
            .contentType(MediaType.APPLICATION_JSON).content("{ \"content\": \"\" }"))
                .andExpect(status().is4xxClientError());
    }
 
    @Test
    public void usersCanToggleFollowing() throws Exception {
        // tester follows jsmith.
        mockMvc.perform(post("/api/toggle-follow/jsmith").session(session)).andExpect(status().isOk());
        Assert.assertTrue(followingRepository.isFollowing("jsmith", "tester"));
        
        // tester unfollows jsmith.
        mockMvc.perform(post("/api/toggle-follow/jsmith").session(session)).andExpect(status().isOk());
        Assert.assertFalse(followingRepository.isFollowing("jsmith", "tester"));
    }
    
    @Test
    public void postCannotBeLikedTwice() throws Exception {
        // TODO
    }
}
