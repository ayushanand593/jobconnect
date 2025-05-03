// package com.DcoDe.jobconnect.JWT;

// import com.DcoDe.jobconnect.utils.CustomUserDetailsService;
// // import com.DcoDe.jobconnect.JWT.JwtService;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StringUtils;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

// @Autowired
// private final JwtTokenProvider jwtTokenProvider;
// @Autowired
// private final CustomUserDetailsService userDetailsService;

// public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
//     this.jwtTokenProvider = jwtTokenProvider;
//     this.userDetailsService = userDetailsService;
// }

// @Override
// protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//         throws ServletException, IOException {
    
//     try {
//         String token = getJwtFromRequest(request);
        
//         if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
//             String username = jwtTokenProvider.getUsernameFromToken(token);
//             UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
//             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                 userDetails, null, userDetails.getAuthorities());
                
//             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
//             SecurityContextHolder.getContext().setAuthentication(authentication);
//         }
//     } catch (Exception ex) {
//         logger.error("Could not set user authentication in security context", ex);
//     }
    
//     filterChain.doFilter(request, response);
// }

// private String getJwtFromRequest(HttpServletRequest request) {
//     String bearerToken = request.getHeader("Authorization");
//     if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//         return bearerToken.substring(7);
//     }
//     return null;
// }
// }