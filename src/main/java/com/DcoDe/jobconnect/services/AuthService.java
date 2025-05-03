package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.entities.User;
//import org.springframework.stereotype.Service;


public interface AuthService {
    User  login(String email, String password);
    User getCurrentUser();
}