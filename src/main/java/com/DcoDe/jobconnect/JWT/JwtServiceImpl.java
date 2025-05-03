//package com.DcoDe.jobconnect.JWT;
//
//import com.DcoDe.jobconnect.entities.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//import javax.crypto.SecretKey;
//
//@Service
//public class JwtServiceImpl implements JwtService {
//
//    @Value("${application.security.jwt.secret-key}")
//    private String secretKey;
//
//    @Value("${application.security.jwt.expiration}")
//    private long jwtExpiration;
//
//    @Override
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    @Override
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    @Override
//    public String generateToken(User user) {
//        return generateToken(new HashMap<>(), user);
//    }
//
//    @Override
//    public String generateToken(Map<String, Object> extraClaims, User user) {
//        extraClaims.put("role", user.getRole().name());
//
//        if (user.getCompany() != null) {
//            extraClaims.put("companyId", user.getCompany().getId());
//        }
//
//        if (user.getCandidateProfile() != null) {
//            extraClaims.put("candidateId", user.getCandidateProfile().getId());
//        }
//
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(user.getEmail())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    @Override
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()                       // ← builder, not parser()
//                .setSigningKey(getSignInKey())         // ← or .verifyWith(getSignInKey())
//                .build()
//                .parseClaimsJws(token)                // ← parse a signed JWT
//                .getBody();
//
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//    @PostConstruct
//    public void init() {
//        System.out.println("🔑 JWT secret (base64) is: " + secretKey);
//    }
//}