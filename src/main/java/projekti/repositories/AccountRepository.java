package projekti.repositories;

import java.util.Collection;
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
    
    @Query("select a from Account a "
            + "join Following f on f.follower.id = :id "
            + "and f.followee.id = a.id")
    public List<Account> findAccountsByFollowerId(@Param("id") long followerId);
    
    @Query("select a from Account a "
            + "join Following f on f.followee.id = :id "
            + "and f.follower.id = a.id")
    public List<Account> findAccountsByFolloweeId(@Param("id") long followeeId);

    @Query("select a from Account a "
         + "join Following f on f.followee.id = a.id "
         + "where f.isFollowerBlocked = true and f.follower.username = :user")
    public List<Account> findBlockersOf(@Param("user") String username);
    
    @Query("select a from Account a "
         + "join Following f on f.followee.id = a.id "
         + "where f.isFollowerBlocked = true and (f.follower.username = :user or f.followee.username = :user)")
    public List<Account> findBlockedBothways(@Param("user") String username);
    
    public List<Account> findByUsernameIn(Collection<String> usernames);

}
