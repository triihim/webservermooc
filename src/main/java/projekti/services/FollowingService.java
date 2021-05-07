package projekti.services;

import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.helpers.SecurityHelper;
import projekti.helpers.TimestampHelper;
import projekti.models.Account;
import projekti.models.Following;
import projekti.repositories.AccountRepository;
import projekti.repositories.FollowingRepository;
import projekti.DTO.FollowingDTO;

@Service
public class FollowingService {
    
    Logger logger = LoggerFactory.getLogger(FollowingService.class);
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    @Transactional
    public FollowingDTO toggleFollowing(String username) {
        Account follower = accountRepository.findByUsername(SecurityHelper.requesterUsername());
        Account followee = accountRepository.findByUsername(username);
        
        if(followee == null) throw new RuntimeException("No user found to toggle follow for: " + username);
        
        List<Following> existingFollowings = followee.getFollowings();
        if(followingRepository.isFollowing(followee.getUsername(), follower.getUsername())) {
            Following existing = existingFollowings
                    .stream()
                    .filter(following -> following.getFollower().getUsername().equals(follower.getUsername()))
                    .findFirst()
                    .orElse(null);
            
            if(existing == null) throw new RuntimeException("Failed to find existing following");

            existingFollowings.remove(existing);
            followingRepository.delete(existing);
            logger.info(follower.getUsername() + " unfollows " + followee.getUsername());
            return new FollowingDTO(false, followee.getUsername(), existingFollowings.size());
        } else {
            Following following = new Following();
            following.setFollower(follower);
            following.setStartedAt(TimestampHelper.getCurrentDateTime());
            followingRepository.save(following);
            existingFollowings.add(following);
            logger.info(follower.getUsername() + " follows " + followee.getUsername());
            return new FollowingDTO(true, followee.getUsername(), existingFollowings.size());
        }
        
    }
    
    public FollowingDTO getFollowingStatus(String username) {
        Account followee = accountRepository.findByUsername(username);
        if(followee == null) throw new RuntimeException("No user found to toggle follow for: " + username);
        
        boolean isFollowedByRequester = followingRepository.isFollowing(followee.getUsername(), SecurityHelper.requesterUsername());
        
        return new FollowingDTO(isFollowedByRequester, followee.getUsername(), followee.getFollowings().size());
    }
    
    
}
