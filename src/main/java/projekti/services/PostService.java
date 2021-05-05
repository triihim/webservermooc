package projekti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.DTO.PostDTO;
import projekti.models.Post;
import projekti.repositories.PostRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    public void createPost(PostDTO dto) {
        
    }
    
}
