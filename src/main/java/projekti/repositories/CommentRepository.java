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
    
    @Query("select c from Comment c "
         + "join Post p on p.id = :post_id "
         + "join p.comments pc on pc.id = c.id")
    List<Comment> findByPostId(@Param("post_id") long id, Pageable pageable);
    
    @Query("select c from Comment c "
         + "join Photo p on p.id = :photo_id "
         + "join p.comments pc on pc.id = c.id")
    List<Comment> findByPhotoId(@Param("photo_id") long id, Pageable pageable);
 
    
}
