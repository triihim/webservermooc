package projekti.services;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.DTO.PostDTO;
import projekti.helpers.SecurityHelper;
import projekti.helpers.TimestampHelper;
import projekti.models.Post;
import projekti.models.Account;
import projekti.models.ResourceLike;
import projekti.repositories.AccountRepository;
import projekti.repositories.LikeRepository;
import projekti.repositories.PostRepository;

@Service
public class PostService {
    
    Logger logger = LoggerFactory.getLogger(PostService.class);
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private FollowingService followingService;
    
    @Autowired
    private LikeRepository likeRepository;
    
    public Post createPost(PostDTO dto) {
        Account owner = accountRepository.findByUsername(SecurityHelper.requesterUsername());
        
        if(owner == null) throw new RuntimeException("No acount found");
        
        Post post = new Post();
        post.setContent(dto.getContent());
        post.setOwner(owner);
        post.setCreatedAt(TimestampHelper.getCurrentDateTime());
        
        logger.info("Post created by: " + owner.getUsername());
        
        return postRepository.save(post);
    }
    
    public Post getPost(long id) {
        return postRepository.getOne(id);
    }
    
    public List<PostDTO> getFeed(String username) {
        Account account = accountRepository.findByUsername(username);
        
        if(account == null) {
            throw new RuntimeException("No account found with username: " + username);
        }
        
        logger.info("Fetching feed for user: " + username);
        
        List<Long> likedPostIds = likeRepository.findPostsLikedByUserId(SecurityHelper.requesterId())
                .stream()
                .map(item -> item.getPostId())
                .collect(Collectors.toList());
        
        List<PostDTO> feed = postRepository.getFeedForUser(account.getUsername(), 1, 25)
                .stream()
                .map(item -> {
                    PostDTO dto = new PostDTO();
                    dto.setId(item.getId());
                    dto.setContent(item.getContent());
                    dto.setCreatedAt(item.getCreatedAt());
                    dto.setOwner(item.getOwner());
                    dto.setLikes(item.getLikes());
                    dto.setCanLike(!likedPostIds.contains(item.getId()));
                    return dto;
                }).collect(Collectors.toList());
                
        return feed;
    }
    
    @Transactional
    public int likePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        
        if(post == null) throw new RuntimeException("No post found with id: " + postId);
        
        Account liker = accountRepository.findByUsername(SecurityHelper.requesterUsername());
        
        if(likeRepository.isPostLikedByUser(post.getId(), liker.getId()).getIsLiked()) {
            logger.info("Post: " + postId + ", unliked by: " + SecurityHelper.requesterUsername());
            throw new RuntimeException("Post already liked by the user");
        } else {
            ResourceLike like = new ResourceLike();
            like.setOwner(liker);
            likeRepository.save(like);
            post.getLikes().add(like);
            logger.info("Post: " + postId + ", liked by: " + SecurityHelper.requesterUsername());
            return post.getLikes().size();
        }
        
    }
   
}

