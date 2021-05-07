package projekti.repositories;

import java.util.List;
import org.springframework.stereotype.Repository;
import projekti.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projekti.projections.IUserFeedItem;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>  {
    
    /**
    * Fetches the posts of the given user and his/hers followees.
    * @param username username of the user whose feed is to be fetched.
    * @param page page to be retrieved. I.e. query limit offset. Minimum 1.
    * @param pageSize specifies the row count per page.
    * @return a list of posts.
    */
    @Query(value = "with accounts_of_interest as (\n" +
                        "select a.id from account a where username = :username\n" +
                        "union\n" +
                        "select af.account_id from account_followings af\n" +
                        "join following f on af.followings_id = f.id " + 
                        "and f.follower_id = (select a.id from account a where username = :username) and f.is_blocked = false\n" +
                    ") select p.id as id, p.content as content, p.created_at as createdAt, p.owner_username as owner, count(pl.likes_id) as likes\n" +
                    "from accounts_of_interest aoi\n" +
                    "join post p on p.owner_id = aoi.id\n" +
                    "left join post_likes pl on pl.post_id = p.id\n" + 
                    "group by p.id\n" +
                    "order by p.created_at desc\n" +
                    "limit :pagesize offset ((:page - 1) * :pagesize)",
            nativeQuery = true)
    public List<IUserFeedItem> getFeedForUser(@Param("username") String username,
                                     @Param("page") int page, 
                                     @Param("pagesize") int pageSize);
    
}
