package projekti.services;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import projekti.DTO.PostDTO;
import projekti.helpers.SecurityHelper;
import projekti.helpers.TimestampHelper;
import projekti.models.Post;
import projekti.models.Account;
import projekti.models.ResourceLike;
import projekti.repositories.AccountRepository;
import projekti.repositories.FollowingRepository;
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
    private CommentService commentService;
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    public Post createPost(PostDTO dto) {
        Account owner = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        
        if(owner == null) throw new RuntimeException("No acount found");
        
        Post post = new Post();
        post.setContent(dto.getContent());
        post.setOwner(owner);
        post.setCreatedAt(TimestampHelper.getCurrentDateTime());
        
        logger.info("Post created by: " + owner.getUsername());
        
        return postRepository.save(post);
    }
    
    @Transactional
    public int likePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        
        if(post == null) throw new RuntimeException("No post found with id: " + postId);
        
        Account liker = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        
        if(likeRepository.isPostLikedByUser(post.getId(), liker.getId()).getIsLiked()) {
            logger.info("Post: " + postId + ", unliked by: " + SecurityHelper.requesterUsername());
            throw new RuntimeException("Post already liked by the user");
        } else {
            ResourceLike like = new ResourceLike();
            like.setOwner(liker);
            likeRepository.save(like);
            post.getLikes().add(like);
            post.setLikeCount(post.getLikeCount() + 1);
            logger.info("Post: " + postId + ", liked by: " + SecurityHelper.requesterUsername());
            return post.getLikes().size();
        }
    }
    
    public List<PostDTO> getUserFeed(String username) {
        Account account = accountRepository.findByUsernameIgnoreCase(username);
        
        // Used to set canLike-flag.
        List<Long> likedByRequester = likeRepository.findPostsLikedByUserId(SecurityHelper.requesterId())
                .stream()
                .map(item -> item.getPostId())
                .collect(Collectors.toList());
        
        // Used to set canComment-flag.
        List<String> followedByRequester = accountRepository.findAccountsByFollowerId(SecurityHelper.requesterId())
                .stream()
                .map(f -> f.getUsername())
                .collect(Collectors.toList());
        
        Pageable postPageable = PageRequest.of(0, 25, Sort.by("createdAt").descending());
        Pageable commentPageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        
        return postRepository.findFeedPosts(account.getId(), SecurityHelper.requesterId(), postPageable)
                .stream()
                .map(p -> {
                    PostDTO dto = new PostDTO();
                    dto.setCanLike(!likedByRequester.contains(p.getId()));
                    dto.setCanComment(followedByRequester.contains(p.getOwnerUsername()) || p.getOwnerUsername().equals(SecurityHelper.requesterUsername()));
                    dto.setContent(p.getContent());
                    dto.setCreatedAt(p.getCreatedAt());
                    dto.setId(p.getId());
                    dto.setLikes(p.getLikeCount());
                    dto.setOwner(p.getOwnerUsername());
                    dto.setComments(commentService.getPostComments(p.getId(), commentPageable));
                    return dto;
                })
                .collect(Collectors.toList());
        
    }

    
}

