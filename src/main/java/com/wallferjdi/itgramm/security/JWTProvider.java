package com.wallferjdi.itgramm.security;

import com.wallferjdi.itgramm.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTProvider.class);

    public String generateToken(Authentication authentication){
        User user =(User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireTime = new Date(now.getTime()+SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());
        Map<String,Object> claimsMap = new HashMap<>();
        claimsMap.put("id",user.getId());
        claimsMap.put("username",user.getEmail());
        claimsMap.put("firstname",user.getName());
        claimsMap.put("lastname",user.getLastname());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET).compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (SignatureException |
                MalformedJwtException|
                ExpiredJwtException|
                UnsupportedJwtException|
                IllegalArgumentException ex){
            LOG.error(ex.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token){
      Claims claims =  Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token).getBody();

      return Long.parseLong((String) claims.get("id"));
    }

}
