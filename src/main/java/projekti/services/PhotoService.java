package projekti.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekti.DTO.PhotoDTO;
import projekti.DTO.PhotoLikeResponseDTO;
import projekti.helpers.SecurityHelper;
import projekti.repositories.PhotoRepository;
import projekti.models.Photo;
import projekti.models.Account;
import projekti.models.ResourceLike;
import projekti.repositories.AccountRepository;
import projekti.repositories.LikeRepository;

@Service
public class PhotoService {
    
    Logger logger = LoggerFactory.getLogger(PhotoService.class);
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Value("${projekti.photo.max-count}")
    private long maxPhotoCount;
    
    public void savePhoto(MultipartFile file, String description) throws IOException {
        Account uploader = accountRepository.getOne(SecurityHelper.requesterId());
        
        if(isMaxPhotosReached(description)) {
            throw new RuntimeException("Photo limit reached");
        }
        
        Photo photo = new Photo();
        photo.setDescription(description);
        photo.setContent(file.getBytes());
        photo.setOwner(uploader);
        
        photoRepository.save(photo);
    }
    
    public boolean isMaxPhotosReached(String username) {
        return photoRepository.countByOwner_usernameIgnoreCase(username) >= maxPhotoCount;
    }
    
    public byte[] getPhotoContent(long photoId) {
        return photoRepository.findWithContentById(photoId).getContent();
    }
    
    public List<PhotoDTO> getUserPhotos(String username) {
        List<Long> likedPhotos = likeRepository.findPhotosLikedByUserId(SecurityHelper.requesterId())
                .stream()
                .map(l -> l.getPhotoId())
                .collect(Collectors.toList());
        
        Long profilePictureId = accountRepository.findByUsernameIgnoreCase(username).getProfilePictureId();
        
        return photoRepository.findAllWithLikesByOwner_usernameIgnoreCase(username)
                .stream()
                .map(p -> new PhotoDTO(
                            p.getId(),
                            p.getDescription(), 
                            p.getLikes().size(), 
                            !likedPhotos.contains(p.getId()), 
                            profilePictureId != null && p.getId() != null && profilePictureId.equals(p.getId())
                        )
                )
                .collect(Collectors.toList());
    }
    
    @Transactional
    public long deletePhoto(long photoId) {
        Photo photo = photoRepository.findWithOwnerById(photoId);
        
        if(photo == null) {
            throw new RuntimeException("No photo found with id: " + photoId);
        }
        
        if(!SecurityHelper.accessorIsLoggedInUser(photo.getOwner().getUsername())) {
            throw new RuntimeException("Can only delete own photos");
        }
        
        Account photoUser = accountRepository.findByProfilePicture_id(photoId);
        
        if(photoUser != null) {
            photoUser.setProfilePicture(null);
        } 
       
        photoRepository.delete(photo);

        return photoId;
    }
    
    @Transactional
    public PhotoLikeResponseDTO likePhoto(long photoId) {
        Account liker = accountRepository.findByUsernameIgnoreCase(SecurityHelper.requesterUsername());
        Photo photo = photoRepository.findWithLikesById(photoId);
        
        if(photo == null) {
            throw new RuntimeException("No photo found with id: " + photoId);
        }
        
        if(likeRepository.isPhotoLikedByUserId(photoId, liker.getId()).getIsLiked()) {
            throw new RuntimeException("Photo: " + photoId + " is already liked by " + liker.getUsername());
        }
        
        logger.info(SecurityHelper.requesterUsername() + " likes photo " + photoId);
        
        ResourceLike like = new ResourceLike();
        like.setOwner(liker);
        
        likeRepository.save(like);
        
        photo.getLikes().add(like);
        
        return new PhotoLikeResponseDTO(photoId, liker.getUsername(), photo.getLikes().size());
    }
}
