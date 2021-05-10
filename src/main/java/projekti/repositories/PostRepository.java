package projekti.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import projekti.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>  {
    
    @EntityGraph(attributePaths = {"owner", "likes"})
    public List<Post> findPostsByOwnerUsernameIn(Collection<String> usernames, Pageable pageable);
    
}
