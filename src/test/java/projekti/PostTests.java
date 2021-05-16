package projekti;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.Assert;
import org.springframework.http.MediaType;
import projekti.repositories.PostRepository;
import projekti.models.Post;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private ObjectMapper mapper;
    
    private MockHttpSession session;
    
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
    public void postCannotBeLikedMoreThanOnce() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/posts").session(session)
            .contentType(MediaType.APPLICATION_JSON).content("{ \"content\": \"test content\" }"))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseJson = result.getResponse().getContentAsString();
        Map<String, Object> parsedResponse = mapper.readValue(responseJson, new TypeReference<Map<String, Object>>(){});

        long id = ((Number)parsedResponse.get("id")).longValue();
        
        Post createdPost = postRepository.findById(id).orElse(null);
        
        long likes = createdPost.getLikeCount();
        
        // Like for the first time.
        mockMvc.perform(post("/api/posts/" + id + "/like").session(session)).andExpect(status().isOk());
        Assert.assertTrue(postRepository.findById(id).orElse(null).getLikeCount() == likes + 1);
        
        // Attempt to like again. Expect to fail.
        mockMvc.perform(post("/api/posts/" + id + "/like").session(session)).andExpect(status().is4xxClientError());
        Assert.assertTrue(postRepository.findById(id).orElse(null).getLikeCount() == likes + 1);
    }
}
