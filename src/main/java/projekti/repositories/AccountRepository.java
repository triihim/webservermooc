package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekti.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    public Account findByUsername(String username);
    
}
