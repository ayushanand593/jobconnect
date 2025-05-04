package com.DcoDe.jobconnect.services;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// @Service
// public class FileStorageServiceImpl implements FileStorageService {

//     @Value("${file.upload-dir:uploads}")
//     private String uploadDir;

//     @Override
//     public String uploadFile(MultipartFile file) {
//         try {
//             // Create the upload directory if it doesn't exist
//             Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
//             if (!Files.exists(uploadPath)) {
//                 Files.createDirectories(uploadPath);
//             }

//             // Generate unique filename
//             String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

//             // Copy file to the upload directory
//             Path targetLocation = uploadPath.resolve(filename);
//             Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

//             return filename;
//         } catch (IOException ex) {
//             throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
//         }
//     }

//     @Override
//     public void deleteFile(String fileUrl) {
//         try {
//             Path filePath = Paths.get(uploadDir).resolve(fileUrl).normalize();
//             Files.deleteIfExists(filePath);
//         } catch (IOException ex) {
//             throw new RuntimeException("Could not delete file " + fileUrl, ex);
//         }
//     }
// }
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String uploadDir = "uploads/profile-pictures/";

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // Ensure the upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Save the file to the upload directory
            Files.copy(file.getInputStream(), filePath);

            // Return the file URL (assuming the server serves files from the upload directory)
            return "/uploads/profile-pictures/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
