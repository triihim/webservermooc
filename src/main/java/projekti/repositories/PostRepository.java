package projekti.repositories;

import java.util.List;
import org.springframework.stereotype.Repository;
import projekti.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>  {
    
    @Query(value = "with accounts_of_interest as (\n" +
                        "select a.id from account a where username = :username\n" +
                        "union\n" +
                        "select af.account_id from account_followings af\n" +
                        "join following f on af.followings_id = f.id " + 
                        "and f.follower_id = (select a.id from account a where username = :username) and f.is_blocked = false\n" +
                    ") select p.* from accounts_of_interest aoi\n" +
                    "join post p on p.owner_id = aoi.id\n" +
                    "order by p.created_at desc\n" +
                    "limit :pagesize offset ((:page - 1) * :pagesize)",
            nativeQuery = true)
    public List<Post> getFeedForUser(@Param("username") String username,
                                     @Param("page") int page, 
                                     @Param("pagesize") int pageSize);
    
}
