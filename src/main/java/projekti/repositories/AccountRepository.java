package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    public Account findByUsername(String username);
    
    @Query("select a from Account a " +
           "where lower(a.username) like lower(concat('%', :partial, '%')) " +
           "or lower(a.firstName) like lower(concat('%', :partial, '%')) " +
           "or lower(a.lastName) like lower(concat('%', :partial, '%'))")
    public List<Account> findByPartialNameOrUsername(@Param("partial") String partial);
   
}
