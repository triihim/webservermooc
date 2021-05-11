package projekti.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.DTO.AccountDTO;
import projekti.DTO.FollowBlockDTO;
import projekti.DTO.FollowListItemDTO;
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
        Account follower = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        Account followee = accountRepository.findByUsernameIgnoreCase(username);
        
        if(followee == null) throw new RuntimeException("No user found to toggle follow for: " + username);
        
        List<Following> existingFollowings = followingRepository.findByFollowee_username(username);
        if(followingRepository.isFollowing(followee.getId(), follower.getId())) {
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
            following.setFollowee(followee);
            following.setStartedAt(TimestampHelper.getCurrentDateTime());
            followingRepository.save(following);
            existingFollowings.add(following);
            logger.info(follower.getUsername() + " follows " + followee.getUsername());
            return new FollowingDTO(true, followee.getUsername(), existingFollowings.size());
        }
        
    }
    
    public FollowingDTO getFollowingStatus(String username) {
        Account followee = accountRepository.findByUsernameIgnoreCase(username);
        if(followee == null) throw new RuntimeException("No user found to toggle follow for: " + username);
        
        boolean isFollowedByRequester = followingRepository.isFollowing(followee.getId(), SecurityHelper.requesterId());
        
        return new FollowingDTO(isFollowedByRequester, followee.getUsername(), followingRepository.findByFollowee_username(username).size());
    }
    
    public List<AccountDTO> getFollowers(String username) {
        Account a = accountRepository.findByUsernameIgnoreCase(username);
        return accountRepository.findAccountsByFolloweeId(a.getId())
                .stream()
                .map(f -> {
                    return new AccountDTO(f.getFirstName(), f.getLastName(), f.getUsername(), f.getProfilePictureId());
                })
                .collect(Collectors.toList());
    }
    
    public List<AccountDTO> getFollowees(String username) {
        Account a = accountRepository.findByUsernameIgnoreCase(username);
        return accountRepository.findAccountsByFollowerId(a.getId())
                .stream()
                .map(f -> {
                    return new AccountDTO(f.getFirstName(), f.getLastName(), f.getUsername(), f.getProfilePictureId());
                })
                .collect(Collectors.toList());
    }
    
    public List<FollowListItemDTO> getFollowersListing(String followee) {
        Account a = accountRepository.findByUsernameIgnoreCase(followee);
        return followingRepository.findByFollowee_username(followee)
                .stream()
                .map(f -> {
                    return new FollowListItemDTO(
                            f.getFollower().getId(),
                            f.getFollower().getUsername(),
                            f.getFollower().getFirstName(),
                            f.getFollower().getLastName(),
                            f.getStartedAt(),
                            SecurityHelper.requesterUsername().equals(followee) ? f.isFollowerBlocked() : null); // Block-status shown only to the followee.
                })
                .collect(Collectors.toList());
    }
    
    public List<FollowListItemDTO> getFolloweesListing(String username) {
        Account a = accountRepository.findByUsernameIgnoreCase(username);
        return followingRepository.findByFollower_username(username)
                .stream()
                .map(f -> {
                    return new FollowListItemDTO(
                            f.getFollowee().getId(),
                            f.getFollowee().getUsername(),
                            f.getFollowee().getFirstName(),
                            f.getFollowee().getLastName(),
                            null,
                            null);
                })
                .collect(Collectors.toList());
    }
    
    public FollowBlockDTO toggleFollowing(Long userId) {
        Account follower = accountRepository.getOne(userId);
        
        if(follower == null) 
            throw new RuntimeException("No user found with id: " + userId);
        
        
        Following following = followingRepository.findByFollowee_usernameAndFollower_username(SecurityHelper.requesterUsername(), follower.getUsername());
        
        if(following == null) {
            throw new RuntimeException(follower.getUsername() + " is not following " + SecurityHelper.requesterUsername());
        }
        
        if(following.isFollowerBlocked() == true) {
            following.setFollowerBlocked(false);
            logger.info(SecurityHelper.requesterUsername() + " blocks the following of " + follower.getUsername());
        } else {
            following.setFollowerBlocked(true);
            logger.info(SecurityHelper.requesterUsername() + " unblocks the following of " + follower.getUsername());
        }
        
        Following updated = followingRepository.save(following);
        
        return new FollowBlockDTO(follower.getId(), follower.getUsername(), updated.isFollowerBlocked());
    }
}
