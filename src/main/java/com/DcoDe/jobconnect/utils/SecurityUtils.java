package com.DcoDe.jobconnect.utils;

import com.DcoDe.jobconnect.entities.User;
//import com.DcoDe.jobconnect.security.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // public static User getCurrentUser() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //     if (authentication == null || !authentication.isAuthenticated() ||
    //             authentication instanceof AnonymousAuthenticationToken) {
    //         return null;
    //     }

    //     Object principal = authentication.getPrincipal();
    //     if (principal instanceof CustomUserDetails) {
    //         return ((CustomUserDetails) principal).getUser();
    //     } else if (principal instanceof User) {
    //         return (User) principal;
    //     }

    //     return null;
    // }

    // public static boolean hasAuthority(String authority) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //     if (authentication == null || !authentication.isAuthenticated()) {
    //         return false;
    //     }

    //     return authentication.getAuthorities().stream()
    //             .map(GrantedAuthority::getAuthority)
    //             .anyMatch(authority::equals);
    // }

    // /**
    //  * Check if the current user has a specific role.
    //  *
    //  * @param role The role to check
    //  * @return true if the current user has the role, false otherwise
    //  */
    // public static boolean hasRole(String role) {
    //     return hasAuthority("ROLE_" + role);
    // }

    // public static Long getCurrentUserId() {
    //     User user = getCurrentUser();
    //     return user != null ? user.getId() : null;
    // }
    
    /**
     * Get the currently authenticated user
     * @return The User entity of the authenticated user, or null if not authenticated
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser();
        }
        return null;
    }
    
    /**
     * Check if the current user has a specific authority
     * @param authority The authority to check for
     * @return true if the user has the authority, false otherwise
     */
    public static boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(authority));
        }
        return false;
    }
    
    /**
     * Check if the current user has any of the specified authorities
     * @param authorities The authorities to check for
     * @return true if the user has any of the authorities, false otherwise
     */
    public static boolean hasAnyAuthority(String... authorities) {
        for (String authority : authorities) {
            if (hasAuthority(authority)) {
                return true;
            }
        }
        return false;
    }
}