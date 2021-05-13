package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.Following;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {
    
    @Query("select count(f) > 0 from Following f where f.followee.id = :followee and f.follower.id = :follower")
    public boolean isFollowing(@Param("followee") long followeeId, 
                               @Param("follower") long followerId);
    
    @EntityGraph(attributePaths = {"follower", "followee"})
    public List<Following> findByFollowee_username(String username);
    
    @EntityGraph(attributePaths = {"follower", "followee"})
    public List<Following> findByFollower_username(String username);
    
    public Following findByFollowee_usernameAndFollower_username(String followee, String follower);
    
    @Query("select count(f) > 0 from Following f where ((f.followee.id = :user1 and f.follower.id = :user2) or "
         + "(f.followee.id = :user2 and f.follower.id = :user1)) "
         + "and f.isFollowerBlocked = true")
    public boolean isBlocked(@Param("user1") long user1, @Param("user2") long user2);
}
