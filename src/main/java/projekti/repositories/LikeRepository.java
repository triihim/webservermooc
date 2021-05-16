package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.ResourceLike;

@Repository
public interface LikeRepository extends JpaRepository<ResourceLike, Long> {
    
    @Query("select count(rl.id) > 0 from ResourceLike rl "
         + "join Post p on p.id = :post_id and p.owner.id = :user_id "
         + "join p.likes pl on pl.id = rl.id")
    public boolean isPostLikedByUser(@Param("post_id") long postId, @Param("user_id") long id);
    
    @Query("select p.id from Post p "
         + "join ResourceLike rl on rl.owner.id = :user_id and rl.id in "
         + "(select id from p.likes)")
    public List<Long> findIdsOfLikedPostsByUserId(@Param("user_id") long userId);
    
    @Query("select count(rl.id) > 0 from ResourceLike rl "
         + "join Photo p on p.id = :photo_id "
         + "join p.likes pl on pl.id = rl.id "
         + "where rl.owner.id = :user_id")
    public boolean isPhotoLikedByUserId(@Param("photo_id") long photoId, @Param("user_id") long userId);
    
    @Query("select p.id from Photo p "
         + "join ResourceLike rl on rl.owner.id = :user_id and rl.id in "
         + "(select id from p.likes)")
    public List<Long> findIdsOfLikedPhotosByUserId(@Param("user_id") long userId);
}
