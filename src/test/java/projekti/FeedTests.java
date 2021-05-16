package projekti;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.hamcrest.Matchers.*;
import org.junit.AfterClass;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import projekti.DTO.PostDTO;
import projekti.repositories.FollowingRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FeedTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FollowingRepository followingRepository;
    
    private static boolean isInitialised = false; // Flag to run setup only once. @BeforeClass proved cumbersome
    
    private MockHttpSession currentSession;
    
    @Before
    public void setup() throws Exception {
        
        if(isInitialised) return;
        
        // Setup users and posts
        TestUtils.populateMockAccounts();
        
        currentSession = TestUtils.loginUser(mockMvc, "bmacy", "password1234");
        mockMvc.perform(post("/api/posts").session(currentSession)
            .contentType(MediaType.APPLICATION_JSON).content("{ \"content\": \"post by jsmith\" }"))
                .andExpect(status().isOk());

        currentSession = TestUtils.loginUser(mockMvc, "mcurrey", "password1234");
        mockMvc.perform(post("/api/posts").session(currentSession)
            .contentType(MediaType.APPLICATION_JSON).content("{ \"content\": \"post by mcurrey\" }"))
                .andExpect(status().isOk());
        
        currentSession = TestUtils.loginUser(mockMvc, "pdavis", "password1234");
        mockMvc.perform(post("/api/posts").session(currentSession)
            .contentType(MediaType.APPLICATION_JSON).content("{ \"content\": \"post by pdavis\" }"))
                .andExpect(status().isOk());
        
        isInitialised = true;
    }
    
    @Test
    public void feedShowsOnlyOwnPostsWhenNoFollowees() throws Exception {
        
        currentSession = TestUtils.loginUser(mockMvc, "mcurrey", "password1234");
        
        // Ensure no followees.
        setFollow("bmacy", "mcurrey", false);
        setFollow("pdavis", "mcurrey", false);
        
        mockMvc.perform(get("/api/test/feed/mcurrey").session(currentSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].owner", equalToIgnoringCase("mcurrey")));
    }
    
    @Test
    public void feedShowsOwnAndFolloweePosts() throws Exception {
        
        // Ensure followings and no blocks
        currentSession = TestUtils.loginUser(mockMvc, "mcurrey", "password1234");
        setFollow("bmacy", "mcurrey", true);
        setFollow("pdavis", "mcurrey", true);
        
        currentSession = TestUtils.loginUser(mockMvc, "pdavis", "password1234");
        setBlock("pdavis", "mcurrey", false);
        
        currentSession = TestUtils.loginUser(mockMvc, "bmacy", "password1234");
        setBlock("bmacy", "mcurrey", false);
        
        currentSession = TestUtils.loginUser(mockMvc, "mcurrey", "password1234");
        
        // Posts initialised in setup() are expected to be ordered from latest to oldest.
        mockMvc.perform(get("/api/test/feed/mcurrey").session(currentSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].owner", equalToIgnoringCase("pdavis")))
                .andExpect(jsonPath("$[1].owner", equalToIgnoringCase("mcurrey")))
                .andExpect(jsonPath("$[2].owner", equalToIgnoringCase("bmacy")));
    }
    
    @Test
    public void feedDoesNotShowFolloweePostsToBlockedFollowers() throws Exception {
        
        currentSession = TestUtils.loginUser(mockMvc, "mcurrey", "password1234");
        setFollow("bmacy", "mcurrey", true);
        setFollow("pdavis", "mcurrey", true);
        
        currentSession = TestUtils.loginUser(mockMvc, "bmacy", "password1234");
        setBlock("bmacy", "mcurrey", true);
        
        currentSession = TestUtils.loginUser(mockMvc, "pdavis", "password1234");
        setBlock("pdavis", "mcurrey", false);
        
        currentSession = TestUtils.loginUser(mockMvc, "mcurrey", "password1234");
        
        // Expect bmacy's post to be missing from mcurrey's feed since the bmacy blocked mcurrey.
        mockMvc.perform(get("/api/test/feed/mcurrey").session(currentSession))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].owner", equalToIgnoringCase("pdavis")))
                .andExpect(jsonPath("$[1].owner", equalToIgnoringCase("mcurrey")));
    }
    
    private void setFollow(String followee, String follower, boolean shouldFollow) throws Exception {
        boolean isFollowing = followingRepository.isFollowing(followee, follower);
        
        if((shouldFollow && isFollowing) || (!shouldFollow && !isFollowing)) return;
        
        mockMvc.perform(post("/api/following/toggle-follow/" + followee).session(currentSession)).andExpect(status().isOk());
    }
    
    private void setBlock(String followee, String follower, boolean shouldBlock) throws Exception {
        boolean isBlocking = followingRepository.isFolloweeBlockingFollower(followee, follower);
        if((shouldBlock && isBlocking) || (!shouldBlock && !isBlocking)) return;
        mockMvc.perform(post("/api/following/toggle-block/" + follower).session(currentSession)).andExpect(status().isOk());
    }
    
}
