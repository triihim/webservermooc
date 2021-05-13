package projekti.controllers;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import projekti.services.PhotoService;

@Controller
public class PhotoController {
    
    @Value("${projekti.photo.max-size}")
    private long maxPhotoSize;
    
    @Autowired
    ResourceLoader resourceLoader;
    
    @Autowired
    private PhotoService photoService;
    
    @GetMapping(value = "/photos/{id}", produces = {"image/png", "image/jpeg"})
    @ResponseBody
    public byte[] getPhoto(@PathVariable Long id) {
        return photoService.getPhotoContent(id);
    }
    
    @GetMapping(value = "/photos", produces = "image/png")
    @ResponseBody
    public byte[] handleNoPhotoIdSpecified() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/images/no-image.png");
        return IOUtils.toByteArray(resource.getInputStream());
    }

    @PostMapping("/photos")
    public String photoUpload(@RequestParam MultipartFile file, @RequestParam String description) {
        
        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Supported formats are .png and .jpeg");
        }
        
        if(file.getSize() > maxPhotoSize) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "File is too large. Maximum file size is " + maxPhotoSize + " bytes");
        }
        
        try {
            photoService.savePhoto(file, description);
        } catch(IOException e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Photo upload failed");
        }

        return "redirect:/";
    }
    
}
