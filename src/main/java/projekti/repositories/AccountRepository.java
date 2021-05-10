package projekti.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekti.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    public Account findByUsernameIgnoreCase(String username);
    
    @Query("select a from Account a " +
           "where lower(a.username) like lower(concat('%', :partial, '%')) " +
           "or lower(a.firstName) like lower(concat('%', :partial, '%')) " +
           "or lower(a.lastName) like lower(concat('%', :partial, '%'))")
    public List<Account> findByPartialNameOrUsername(@Param("partial") String partial, Pageable pageable);
    
    public List<Account> findAccountsByFollowings_follower_username(String username);
    
    @Query(value = "select a.* from account_followings af\n" +
            "join following f on f.id = af.followings_id\n" +
            "join account a on a.id = f.follower_id\n" +
            "where af.account_id = (select id from account where username = :username)",
            nativeQuery = true)
    public List<Account> findFollowers(@Param("username") String username);
}
