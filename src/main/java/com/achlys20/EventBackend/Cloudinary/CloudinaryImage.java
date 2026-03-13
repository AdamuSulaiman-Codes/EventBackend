package com.achlys20.EventBackend.Cloudinary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CloudinaryImage {
    private String url;
    private String publicId;
}