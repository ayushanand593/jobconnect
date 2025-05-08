// package com.DcoDe.jobconnect.controllers;

// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.DcoDe.jobconnect.services.FirebaseStorageService;

// @RestController
// @RequestMapping("/api/files")
// public class FileUploadController {

//     @Autowired
//     private FirebaseStorageService firebaseStorageService;
    
//     // Additional services for candidate and company management
//     // @Autowired private CandidateService candidateService;
//     // @Autowired private CompanyService companyService;
    
//     @PostMapping("/resume")
//     public ResponseEntity<Map<String, String>> uploadResume(
//             @RequestParam("file") MultipartFile file,
//             @RequestParam("candidateId") Long candidateId) {
        
//         try {
//             // Upload file to Firebase Storage
//             String fileUrl = firebaseStorageService.uploadFile(file, "resumes");
            
//             // Update candidate record with resume URL
//             // candidateService.updateResumeUrl(candidateId, fileUrl);
            
//             Map<String, String> response = new HashMap<>();
//             response.put("fileUrl", fileUrl);
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body(Collections.singletonMap("error", e.getMessage()));
//         }
//     }
    
//     @PostMapping("/logo")
//     public ResponseEntity<Map<String, String>> uploadCompanyLogo(
//             @RequestParam("file") MultipartFile file,
//             @RequestParam("companyId") Long companyId) {
        
//         try {
//             // Upload file to Firebase Storage
//             String fileUrl = firebaseStorageService.uploadFile(file, "logos");
            
//             // Update company record with logo URL
//             // companyService.updateLogoUrl(companyId, fileUrl);
            
//             Map<String, String> response = new HashMap<>();
//             response.put("fileUrl", fileUrl);
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body(Collections.singletonMap("error", e.getMessage()));
//         }
//     }
    
//     @DeleteMapping
//     public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("fileUrl") String fileUrl) {
//         try {
//             firebaseStorageService.deleteFile(fileUrl);
//             return ResponseEntity.ok(Collections.singletonMap("message", "File deleted successfully"));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body(Collections.singletonMap("error", e.getMessage()));
//         }
//     }
// }
