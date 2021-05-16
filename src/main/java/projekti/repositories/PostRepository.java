package projekti.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import projekti.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>  {
    
    @EntityGraph(attributePaths = {"owner", "likes"})
    public List<Post> findPostsByOwnerUsernameIn(Collection<String> usernames, Pageable pageable);

    @Query("select p from Post p "
         + "where p.owner.id = :feedOwner or p.owner.id in "
            + "(select f.followee.id from Following f where f.follower.id = :feedOwner and f.isFollowerBlocked = false)")
    public List<Post> getFeedPosts(@Param("feedOwner") long feedOwnerId, Pageable pageable);
    
}
