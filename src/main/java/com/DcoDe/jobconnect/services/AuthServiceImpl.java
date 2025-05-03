package com.DcoDe.jobconnect.services;


//import com.DcoDe.jobconnect.JWT.JwtService;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.repositories.UserRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;

//    @Autowired
//    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService,  PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//        this.passwordEncoder=passwordEncoder;
//    }

    @Override
    public User  login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return user;
    }

    @Override
    public User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }
}
