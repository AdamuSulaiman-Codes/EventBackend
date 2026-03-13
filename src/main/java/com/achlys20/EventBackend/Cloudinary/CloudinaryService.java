package com.achlys20.EventBackend.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryImage uploadImage(MultipartFile file) {
        try {

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "events")
            );

            return new CloudinaryImage(
                    uploadResult.get("secure_url").toString(),
                    uploadResult.get("public_id").toString()
            );

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }


    public void deleteImage(String publicId) {

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}