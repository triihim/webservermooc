package projekti.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    @Query(value = "select c.id as id, c.content as content, c.created_at as createdAt, c.owner_id as ownerId from comment c\n" +
           "join post_comments pc on pc.post_id = :post_id and c.id = pc.comments_id", nativeQuery = true)
    List<Comment> findByPostId(@Param("post_id") long id, Pageable pageable);
    
    @Query(value = "select c.id as id, c.content as content, c.created_at as createdAt, c.owner_id as ownerId from comment c\n" +
           "join photo_comments pc on pc.photo_id = :photo_id and c.id = pc.comments_id", nativeQuery = true)
    List<Comment> findByPhotoId(@Param("photo_id") long id, Pageable pageable);
    
}
