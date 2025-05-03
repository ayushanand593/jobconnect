package com.DcoDe.jobconnect.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
public class AuthDebugController {

    // @GetMapping("/auth")
    // public ResponseEntity<Map<String, Object>> debugAuthentication() {
    //     Map<String, Object> debugInfo = new HashMap<>();

    //     // Get authentication object
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //     if (authentication != null) {
    //         debugInfo.put("authenticated", authentication.isAuthenticated());
    //         debugInfo.put("principal_type", authentication.getPrincipal().getClass().getName());
    //         debugInfo.put("principal", authentication.getPrincipal().toString());
    //         debugInfo.put("name", authentication.getName());

    //         // Get authorities
    //         List<String> authorities = authentication.getAuthorities().stream()
    //                 .map(GrantedAuthority::getAuthority)
    //                 .collect(Collectors.toList());
    //         debugInfo.put("authorities", authorities);
    //     } else {
    //         debugInfo.put("authenticated", false);
    //     }

    //     return ResponseEntity.ok(debugInfo);
    // }
    
    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> debugAuthentication() {
        Map<String, Object> debugInfo = new HashMap<>();

        // Get authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            debugInfo.put("authenticated", authentication.isAuthenticated());
            debugInfo.put("principal_type", authentication.getPrincipal().getClass().getName());
            debugInfo.put("name", authentication.getName());

            // Get authorities
            List<String> authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            debugInfo.put("authorities", authorities);
        } else {
            debugInfo.put("authenticated", false);
        }

        return ResponseEntity.ok(debugInfo);
    }

    @GetMapping("/employer-only")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<String> employerOnly() {
        return ResponseEntity.ok("If you can see this, you have EMPLOYER role access");
    }
}