package com.amenity_reservation_system.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Objects;

@Service
public class PhotoStorageCloudinaryService {

    private final Cloudinary cloudinary;

    public PhotoStorageCloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String getUrlPhoto(MultipartFile photo) throws IOException {

        if (!Objects.equals(photo.getContentType(), "image/png") &&
                !Objects.equals(photo.getContentType(), "image/jpeg"))
            throw new IOException("It is necessary to use a photo with the PNG or JPG extension");

        File file = new File("my_image.jpg");

        BufferedInputStream inputStream = new BufferedInputStream(photo.getInputStream());
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

        int i;
        while ((i = inputStream.read()) != -1) {
            outputStream.write(i);
        }

        Map uploadResult = cloudinary.uploader()
                .upload(file, ObjectUtils.asMap("folder", "App Amenity Reservation System"));
        file.delete();

        return String.valueOf(uploadResult.get("url"));
    }
}
