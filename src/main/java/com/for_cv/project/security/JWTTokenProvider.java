package com.for_cv.project.security;

import com.for_cv.project.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.for_cv.project.security.SecurityConstants.*;

@Component
@Log4j2
public class JWTTokenProvider {

    /**
     * Method for generate JWT token.
     * @param authentication
     * @return JWT token;
     */
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date nowDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(nowDate.getTime() + EXPIRATION_TIME);

        String userId = String.valueOf(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.ES512, SECRET)
                .compact();
    }

    /**
     * Method for validation JWT token to correct.
     * (Set sign in key in our JWT and parse claim which we have added method generateToken as claimsMap.)
     * @param token
     * @return boolean (true - correct | false - incorrect)
     */
    public boolean validationToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Method gives to us userId from JWT token.
     * @param token
     * @return userId -- type Long
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        String userId = (String) claims.get("id"); // Take userId from claimsMap (WARNING: don't use getId() it's not a same)
        return Long.parseLong(userId);
    }

}
