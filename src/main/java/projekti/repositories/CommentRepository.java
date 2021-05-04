package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekti.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
