package projekti.services;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projekti.DTO.CommentDTO;
import projekti.helpers.SecurityHelper;
import projekti.helpers.TimestampHelper;
import projekti.models.Comment;
import projekti.models.Account;
import projekti.models.Post;
import projekti.repositories.AccountRepository;
import projekti.repositories.CommentRepository;
import projekti.repositories.PostRepository;

@Service
public class CommentService {
    
    Logger logger = LoggerFactory.getLogger(CommentService.class);
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Transactional
    @CacheEvict(cacheNames = "post-comments", key="#postId")
    public CommentDTO comment(Long postId, String comment) {
        Account commenter = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        Post post = postRepository.getOne(postId);
        
        if(post == null) {
            throw new RuntimeException("No post found to comment with id: " + postId);
        }
        
        Comment c = new Comment(comment, commenter, TimestampHelper.getCurrentDateTime(), post);
        commentRepository.save(c);
        
        post.getComments().add(c);
        
        logger.info(SecurityHelper.requesterUsername() + " commented on post: " + post.getId());
        
        return new CommentDTO(c.getId(), postId, comment, SecurityHelper.requesterUsername(), post.getCreatedAt());
    }
    
    
    @Cacheable(cacheNames = {"post-comments"}, key = "#postId")
    public List<CommentDTO> getPostComments(long postId, Pageable pageable) {
        return commentRepository.findByPost_id(postId, pageable)
                .stream()
                .map(c -> new CommentDTO(c.getId(), postId, c.getContent(), c.getOwner().getUsername(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}

