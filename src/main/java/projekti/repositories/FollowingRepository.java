package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.Following;
import projekti.models.Account;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {
    
    @Query(value = "select count(af.account_id) > 0 from account_followings af\n" +
                    "join following f on f.id = af.followings_id\n" +
                    "join account a1 on a1.id = af.account_id\n" +
                    "join account a2 on a2.id = f.follower_id\n" +
                    "where a1.username = :followee AND a2.username = :follower",
            nativeQuery = true)
    public boolean isFollowing(@Param("followee") String followeeUsername, 
                               @Param("follower") String followerUsername);
    
}
