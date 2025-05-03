package com.DcoDe.jobconnect.utils;




import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // private final UserRepository userRepository;

    // public UserDetailsServiceImpl(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }

    // // @Override
    // // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // //     User user = userRepository.findByEmail(email)
    // //             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
    // //     return new CustomUserDetails(user);
    // // }
    // @Override
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //     User user = userRepository.findByEmail(email)
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    //     return new CustomUserDetails(user, getAuthorities(user));
    // }
    // private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    //     List<GrantedAuthority> authorities = new ArrayList<>();
        
    //     // Add both formats of the role: with and without the ROLE_ prefix
    //     authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
    //     authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        
    //     return authorities;
    // }
     @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user, getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add both formats of the role: with and without the ROLE_ prefix
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        
        return authorities;
    }
}
