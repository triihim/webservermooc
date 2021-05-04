package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekti.models.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    
}
