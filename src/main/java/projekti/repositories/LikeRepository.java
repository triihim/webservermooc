package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.ResourceLike;
import projekti.projections.IPostLike;
import projekti.projections.IIsLiked;
import projekti.projections.IPhotoLike;

@Repository
public interface LikeRepository extends JpaRepository<ResourceLike, Long> {
    
    @Query(value = "select count(rl.id) > 0 as isLiked from resource_like rl\n" +
                    "join post_likes pl on pl.likes_id = rl.id\n" +
                    "where pl.post_id = :post_id and rl.owner_id = :user_id",
            nativeQuery = true)
    public IIsLiked isPostLikedByUser(@Param("post_id") long postId, @Param("user_id") long id);
    
    
    @Query(value = "select pl.post_id as postId from resource_like rl\n" +
                    "join post_likes pl on pl.likes_id = rl.id\n" +
                    "where rl.owner_id = :user_id",
            nativeQuery = true)
    public List<IPostLike> findPostsLikedByUserId(@Param("user_id") long userId);
    
    
    @Query(value = "select count(l.id) > 0 as isLiked from resource_like l\n" +
                   "join photo_likes pl on pl.photo_id = :photo_id and pl.likes_id = l.id\n" +
                   "where l.owner_id = :user_id", nativeQuery = true)
    public IIsLiked isPhotoLikedByUserId(@Param("photo_id") long photoId, @Param("user_id") long userId);
    
    @Query(value = "select p.id as photoId from photo p\n" +
                   "join photo_likes pl on pl.photo_id = p.id\n" +
                   "join resource_like l on l.id = pl.likes_id\n" +
                   "where l.owner_id = :user_id", 
            nativeQuery = true)
    public List<IPhotoLike> findPhotosLikedByUserId(@Param("user_id") long userId);
}
