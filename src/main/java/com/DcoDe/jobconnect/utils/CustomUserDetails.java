package com.DcoDe.jobconnect.utils;




import com.DcoDe.jobconnect.entities.User;
import lombok.Getter;
// import org.springframework.context.annotation.Configuration;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class CustomUserDetails implements UserDetails {

    // private final User user;

    // public CustomUserDetails(User user) {
    //     this.user = user;
    // }

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     // Return the user's role as a GrantedAuthority
    //     String role = "ROLE_" + user.getRole().name(); // Prefix with "ROLE_" as expected by Spring Security
    //     return Collections.singletonList(new SimpleGrantedAuthority(role));
    // }

    // @Override
    // public String getPassword() {
    //     return user.getPassword();
    // }

    // @Override
    // public String getUsername() {
    //     return user.getEmail();
    // }

    // @Override
    // public boolean isAccountNonExpired() {
    //     return true;
    // }

    // @Override
    // public boolean isAccountNonLocked() {
    //     return true;
    // }

    // @Override
    // public boolean isCredentialsNonExpired() {
    //     return true;
    // }

    // @Override
    // public boolean isEnabled() {
    //     return true;
    // }
    private final User user;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     // Add ROLE_ prefix to the user role
    //     return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    // }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
