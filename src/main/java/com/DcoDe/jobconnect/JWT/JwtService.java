//package com.DcoDe.jobconnect.JWT;
//
//
//import com.DcoDe.jobconnect.entities.User;
//import io.jsonwebtoken.Claims;
//import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.function.Function;
//
//
//public interface JwtService {
//    String extractUsername(String token);
//    <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver);
//    String generateToken(User user);
//    String generateToken(Map<String, Object> extraClaims, User user);
//    boolean isTokenValid(String token, UserDetails userDetails);
//}
