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
import projekti.exceptions.PhotoNotFoundException;
import projekti.exceptions.PostNotFoundException;
import projekti.helpers.SecurityHelper;
import projekti.helpers.TimestampHelper;
import projekti.models.Comment;
import projekti.models.Account;
import projekti.models.Post;
import projekti.models.Photo;
import projekti.repositories.AccountRepository;
import projekti.repositories.CommentRepository;
import projekti.repositories.PhotoRepository;
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
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Transactional
    @CacheEvict(cacheNames = "post-comments", key="#postId")
    public CommentDTO commentPost(long postId, String comment) {
        Account commenter = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        Post post = postRepository.getOne(postId);
        
        if(post == null) {
            throw new PostNotFoundException("No post found to comment with id: " + postId);
        }
        
        Comment c = new Comment(comment, commenter, TimestampHelper.getCurrentDateTime());
        commentRepository.save(c);
        
        post.getComments().add(c);
        
        logger.info(SecurityHelper.requesterUsername() + " commented on post: " + post.getId());
        
        return new CommentDTO(c.getId(), postId, comment, SecurityHelper.requesterUsername(), c.getCreatedAt());
    }
    
    @Transactional
    @CacheEvict(cacheNames = "photo-comments", key="#photoId")
    public CommentDTO commentPhoto(long photoId, String comment) {
        Account commenter = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        Photo photo = photoRepository.getOne(photoId);
        
        if(photo == null) {
            throw new PhotoNotFoundException("No photo found to comment with id: " + photoId);
        }
        
        Comment c = new Comment(comment, commenter, TimestampHelper.getCurrentDateTime());
        commentRepository.save(c);
        
        photo.getComments().add(c);
        
        logger.info(SecurityHelper.requesterUsername() + " commented on photo: " + photo.getId());
        
        return new CommentDTO(c.getId(), photoId, comment, SecurityHelper.requesterUsername(), c.getCreatedAt());
    }
    
    @Cacheable(cacheNames = {"post-comments"}, key = "#postId")
    public List<CommentDTO> getPostComments(long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable)
                .stream()
                .map(c -> new CommentDTO(c.getId(), postId, c.getContent(), c.getOwner().getUsername(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
    
    @Cacheable(cacheNames = {"photo-comments"}, key = "#photoId")
    public List<CommentDTO> getPhotoComments(long photoId, Pageable pageable) {
        return commentRepository.findByPhotoId(photoId, pageable)
                .stream()
                .map(c -> new CommentDTO(c.getId(), photoId, c.getContent(), c.getOwner().getUsername(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}

