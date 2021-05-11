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
            + "join Account a on a.id = p.owner.id "
            + "where (p.owner.id in "
                + "(select u.id from Account u "
                + "join Following f on (f.followee.id = u.id and f.follower.id = :feedOwner) or u.id = :feedOwner) "
            + "or p.owner.id = :feedOwner) "
            + "and (0 = (select count(b) from Following b where "
                    + "((b.followee.id = :requester or b.follower.id = :requester) "
                    + "and (b.followee.id = p.owner.id or b.follower.id = p.owner.id) "
                    + "and b.isFollowerBlocked = true) ) "
            + "or (:requester = :feedOwner and p.owner.id = :requester))")
    public List<Post> findFeedPosts(@Param("feedOwner") long feedOwnerId,
                                    @Param("requester") long requesterId,
                                    Pageable pageable);
    
}
