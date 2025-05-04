package com.DcoDe.jobconnect.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3FileStorageService {
    String uploadFile(MultipartFile file);
}