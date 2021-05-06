package projekti.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.DTO.PostDTO;
import projekti.helpers.SecurityHelper;
import projekti.helpers.TimestampHelper;
import projekti.models.Post;
import projekti.models.Account;
import projekti.repositories.AccountRepository;
import projekti.repositories.PostRepository;

@Service
public class PostService {
    
    Logger logger = LoggerFactory.getLogger(PostService.class);
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Post createPost(PostDTO dto) {
        Account owner = accountRepository.findByUsername(SecurityHelper.requesterUsername());
        
        Post post = new Post();
        post.setContent(dto.getContent());
        post.setOwner(owner);
        post.setCreatedAt(TimestampHelper.getCurrentDateTime());
        
        logger.info("Post created by: " + owner.getUsername());
        
        return postRepository.save(post);
    }
    
    public List<PostDTO> getRequesterFeed() {
        logger.info("Fetching feed for user: " + SecurityHelper.requesterUsername());
        
        List<PostDTO> feed = postRepository.getFeedForUser(SecurityHelper.requesterUsername(), 1, 25)
                .stream()
                .map(p -> new PostDTO(p.getContent(), p.getCreatedAt(), p.getOwnerUsername()))
                .collect(Collectors.toList());
        
        logger.info("Fetched a feed of " + feed.size() + " posts");
        
        return feed;
    }
    
}
