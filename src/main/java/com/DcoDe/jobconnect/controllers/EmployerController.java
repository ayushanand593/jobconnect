// package com.DcoDe.jobconnect.controllers;


// import com.DcoDe.jobconnect.dto.EmployerProfileDTO;
// import com.DcoDe.jobconnect.dto.EmployerProfileUpdateDTO;
// import com.DcoDe.jobconnect.entities.User;
// import com.DcoDe.jobconnect.enums.UserRole;
// import com.DcoDe.jobconnect.services.EmployerService;
// import com.DcoDe.jobconnect.utils.CustomUserDetails;
// import com.DcoDe.jobconnect.utils.SecurityUtils;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.multipart.MultipartFile;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/employers")
// @RequiredArgsConstructor
// public class EmployerController {
//     @Autowired
//     private final EmployerService employerService;

// @GetMapping("/profile/{userId}")
// @PreAuthorize("hasRole('EMPLOYER')")
// public ResponseEntity<EmployerProfileDTO> getProfileByUserId(@PathVariable Long userId) {
//     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//     Long authenticatedUserId = getUserIdFromAuthentication(authentication);

//     if (!Objects.equals(authenticatedUserId, userId)) {
//         throw new AccessDeniedException("You are not authorized to view this profile");
//     }

//     EmployerProfileDTO profile = employerService.getEmployerProfileDTOByUserId(userId);
//     return ResponseEntity.ok(profile);
// }

// //    @GetMapping("/my-profile")
// //    @PreAuthorize("hasRole('EMPLOYER')")
// //    public ResponseEntity<EmployerProfileDTO> getMyProfile() {
// //        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// //        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
// //
// //        EmployerProfileDTO profile = employerService.getEmployerProfileDTOByUserId(authenticatedUserId);
// //        return ResponseEntity.ok(profile);
// //    }

    
// @GetMapping("/my-profile")
// @PreAuthorize("hasRole('ROLE_EMPLOYER')")
// public ResponseEntity<EmployerProfileDTO> getMyProfile() {
//     // Get the current authenticated user
//     User currentUser = SecurityUtils.getCurrentUser();
//     if (currentUser == null) {
//         throw new AccessDeniedException("Not authenticated");
//     }

//     // Verify the user is an employer
//     if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
//         throw new AccessDeniedException("Not authorized to access employer profile");
//     }

//     // Call the service method that already exists for getting the current user's profile
//     EmployerProfileDTO profile = employerService.getCurrentEmployerProfile();
//     return ResponseEntity.ok(profile);
// }

//     @PutMapping("/profile/{userId}")
//     @PreAuthorize("hasRole('ROLE_EMPLOYER')")
//     public ResponseEntity<EmployerProfileDTO> updateProfile(
//             @PathVariable Long userId,
//             @Valid @RequestBody EmployerProfileUpdateDTO dto) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         Long authenticatedUserId = getUserIdFromAuthentication(authentication);

//         if (!Objects.equals(authenticatedUserId, userId)) {
//             throw new AccessDeniedException("You are not authorized to update this profile");
//         }

//         EmployerProfileDTO updatedProfile = employerService.updateProfile(dto);
//         return ResponseEntity.ok(updatedProfile);
//     }

//     @PutMapping("/profile/{userId}/picture")
//     @PreAuthorize("hasRole('EMPLOYER')")
//     public ResponseEntity<EmployerProfileDTO> updateProfilePicture(
//             @PathVariable Long userId,
//             @RequestParam("file") MultipartFile file) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         Long authenticatedUserId = getUserIdFromAuthentication(authentication);

//         if (!Objects.equals(authenticatedUserId, userId)) {
//             throw new AccessDeniedException("You are not authorized to update this profile picture");
//         }

//         EmployerProfileDTO updatedProfile = employerService.updateProfilePicture(file);
//         return ResponseEntity.ok(updatedProfile);
//     }

//     @PutMapping("/promote/{employerId}")
//     @PreAuthorize("hasRole('EMPLOYER')")
//     public ResponseEntity<Void> promoteToAdmin(@PathVariable Long employerId) {
//         employerService.promoteToAdmin(employerId);
//         return ResponseEntity.ok().build();
//     }

//     @PutMapping("/demote/{employerId}")
//     @PreAuthorize("hasRole('EMPLOYER')")
//     public ResponseEntity<Void> demoteFromAdmin(@PathVariable Long employerId) {
//         employerService.demoteFromAdmin(employerId);
//         return ResponseEntity.ok().build();
//     }
//     @GetMapping("/auth-debug")
//     @PreAuthorize("hasRole('EMPLOYER')")
//     public ResponseEntity<Map<String, Object>> debugAuthentication() {
//         Map<String, Object> debugInfo = new HashMap<>();

//         // Get authentication object
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//         if (authentication != null) {
//             debugInfo.put("authenticated", authentication.isAuthenticated());
//             debugInfo.put("principal_type", authentication.getPrincipal().getClass().getName());

//             // Try to extract user details
//             try {
//                 if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                     CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//                     User user = userDetails.getUser();

//                     debugInfo.put("user_id", user.getId());
//                     debugInfo.put("email", user.getEmail());
//                     debugInfo.put("role", user.getRole().toString());

//                     if (user.getCompany() != null) {
//                         debugInfo.put("company_id", user.getCompany().getId());
//                         debugInfo.put("company_name", user.getCompany().getCompanyName());
//                     }
//                 }
//             } catch (Exception e) {
//                 debugInfo.put("error_extracting_details", e.getMessage());
//             }

//             // Get authorities
//             List<String> authorities = authentication.getAuthorities().stream()
//                     .map(GrantedAuthority::getAuthority)
//                     .collect(Collectors.toList());
//             debugInfo.put("authorities", authorities);
//         } else {
//             debugInfo.put("authenticated", false);
//         }

//         return ResponseEntity.ok(debugInfo);
//     }

//     private Long getUserIdFromAuthentication(Authentication authentication) {
//         // Assuming the user ID is stored in the authentication principal
//         // This might vary based on your authentication setup
//         return ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
//     }

// }
// package com.DcoDe.jobconnect.controllers;

// import com.DcoDe.jobconnect.dto.EmployerProfileDTO;
// import com.DcoDe.jobconnect.dto.EmployerProfileUpdateDTO;
// import com.DcoDe.jobconnect.entities.User;
// import com.DcoDe.jobconnect.enums.UserRole;
// import com.DcoDe.jobconnect.services.EmployerService;
// import com.DcoDe.jobconnect.utils.CustomUserDetails;
// import com.DcoDe.jobconnect.utils.SecurityUtils;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.multipart.MultipartFile;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/employers")
// @RequiredArgsConstructor
// public class EmployerController {
//     @Autowired
//     private final EmployerService employerService;

//     @GetMapping("/profile/{userId}")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<EmployerProfileDTO> getProfileByUserId(@PathVariable Long userId) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         Long authenticatedUserId = getUserIdFromAuthentication(authentication);

//         if (!Objects.equals(authenticatedUserId, userId)) {
//             throw new AccessDeniedException("You are not authorized to view this profile");
//         }

//         EmployerProfileDTO profile = employerService.getEmployerProfileDTOByUserId(userId);
//         return ResponseEntity.ok(profile);
//     }

//     @GetMapping("/my-profile")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<EmployerProfileDTO> getMyProfile() {
//         // Get the current authenticated user
//         User currentUser = SecurityUtils.getCurrentUser();
//         if (currentUser == null) {
//             throw new AccessDeniedException("Not authenticated");
//         }

//         // Verify the user is an employer
//         if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
//             throw new AccessDeniedException("Not authorized to access employer profile");
//         }

//         // Call the service method that already exists for getting the current user's profile
//         EmployerProfileDTO profile = employerService.getCurrentEmployerProfile();
//         return ResponseEntity.ok(profile);
//     }

//     @PutMapping("/profile/{userId}")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<EmployerProfileDTO> updateProfile(
//             @PathVariable Long userId,
//             @Valid @RequestBody EmployerProfileUpdateDTO dto) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         Long authenticatedUserId = getUserIdFromAuthentication(authentication);

//         if (!Objects.equals(authenticatedUserId, userId)) {
//             throw new AccessDeniedException("You are not authorized to update this profile");
//         }

//         EmployerProfileDTO updatedProfile = employerService.updateProfile(dto);
//         return ResponseEntity.ok(updatedProfile);
//     }

//     @PutMapping("/profile/{userId}/picture")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<EmployerProfileDTO> updateProfilePicture(
//             @PathVariable Long userId,
//             @RequestParam("file") MultipartFile file) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         Long authenticatedUserId = getUserIdFromAuthentication(authentication);

//         if (!Objects.equals(authenticatedUserId, userId)) {
//             throw new AccessDeniedException("You are not authorized to update this profile picture");
//         }

//         EmployerProfileDTO updatedProfile = employerService.updateProfilePicture(file);
//         return ResponseEntity.ok(updatedProfile);
//     }

//     @PutMapping("/promote/{employerId}")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<Void> promoteToAdmin(@PathVariable Long employerId) {
//         employerService.promoteToAdmin(employerId);
//         return ResponseEntity.ok().build();
//     }

//     @PutMapping("/demote/{employerId}")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<Void> demoteFromAdmin(@PathVariable Long employerId) {
//         employerService.demoteFromAdmin(employerId);
//         return ResponseEntity.ok().build();
//     }

//     @GetMapping("/auth-debug")
//     @PreAuthorize("hasAuthority('ROLE_EMPLOYER')") // Changed from hasRole to hasAuthority with ROLE_ prefix
//     public ResponseEntity<Map<String, Object>> debugAuthentication() {
//         Map<String, Object> debugInfo = new HashMap<>();

//         // Get authentication object
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//         if (authentication != null) {
//             debugInfo.put("authenticated", authentication.isAuthenticated());
//             debugInfo.put("principal_type", authentication.getPrincipal().getClass().getName());

//             // Try to extract user details
//             try {
//                 if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                     CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//                     User user = userDetails.getUser();

//                     debugInfo.put("user_id", user.getId());
//                     debugInfo.put("email", user.getEmail());
//                     debugInfo.put("role", user.getRole().toString());

//                     if (user.getCompany() != null) {
//                         debugInfo.put("company_id", user.getCompany().getId());
//                         debugInfo.put("company_name", user.getCompany().getCompanyName());
//                     }
//                 }
//             } catch (Exception e) {
//                 debugInfo.put("error_extracting_details", e.getMessage());
//             }

//             // Get authorities
//             List<String> authorities = authentication.getAuthorities().stream()
//                     .map(GrantedAuthority::getAuthority)
//                     .collect(Collectors.toList());
//             debugInfo.put("authorities", authorities);
//         } else {
//             debugInfo.put("authenticated", false);
//         }

//         return ResponseEntity.ok(debugInfo);
//     }

//     private Long getUserIdFromAuthentication(Authentication authentication) {
//         // Assuming the user ID is stored in the authentication principal
//         // This might vary based on your authentication setup
//         return ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
//     }
// }
package com.DcoDe.jobconnect.controllers;

import com.DcoDe.jobconnect.dto.EmployerProfileDTO;
import com.DcoDe.jobconnect.dto.EmployerProfileUpdateDTO;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.UserRole;
import com.DcoDe.jobconnect.services.EmployerService;
import com.DcoDe.jobconnect.utils.CustomUserDetails;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerController {
    @Autowired
    private final EmployerService employerService;

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<EmployerProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);

        if (!Objects.equals(authenticatedUserId, userId)) {
            throw new AccessDeniedException("You are not authorized to view this profile");
        }

        EmployerProfileDTO profile = employerService.getEmployerProfileDTOByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<EmployerProfileDTO> getMyProfile() {
        // Get the current authenticated user
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        // Verify the user is an employer
        if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new AccessDeniedException("Not authorized to access employer profile");
        }

        // Call the service method that already exists for getting the current user's profile
        EmployerProfileDTO profile = employerService.getCurrentEmployerProfile();
        return ResponseEntity.ok(profile);
    }

    // @PutMapping("/profile/{userId}")
    // @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    // public ResponseEntity<EmployerProfileDTO> updateProfile(
    //         @PathVariable Long userId,
    //         @Valid @RequestBody EmployerProfileUpdateDTO dto) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     Long authenticatedUserId = getUserIdFromAuthentication(authentication);

    //     if (!Objects.equals(authenticatedUserId, userId)) {
    //         throw new AccessDeniedException("You are not authorized to update this profile");
    //     }

    //     EmployerProfileDTO updatedProfile = employerService.updateProfile(dto);
    //     return ResponseEntity.ok(updatedProfile);
    // }
    @PutMapping("/profile-update")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<EmployerProfileDTO> updateProfile(
            @Valid @RequestBody EmployerProfileUpdateDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        EmployerProfileDTO updatedProfile = employerService.updateProfile(dto);
        return ResponseEntity.ok(updatedProfile);
    }
  
   

    @PutMapping("/profile/{userId}/picture")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<EmployerProfileDTO> updateProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);

        if (!Objects.equals(authenticatedUserId, userId)) {
            throw new AccessDeniedException("You are not authorized to update this profile picture");
        }

        EmployerProfileDTO updatedProfile = employerService.updateProfilePicture(file);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/promote/{employerId}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable Long employerId) {
        employerService.promoteToAdmin(employerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/demote/{employerId}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<Void> demoteFromAdmin(@PathVariable Long employerId) {
        employerService.demoteFromAdmin(employerId);
        return ResponseEntity.ok().build();
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        // Assuming the user ID is stored in the authentication principal
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        }
        throw new AccessDeniedException("Invalid authentication");
    }
}