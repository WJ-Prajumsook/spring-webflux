package org.wj.prajumsook.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.wj.prajumsook.domain.User;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private String secret = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String expireTimeInMiliSec = "30000";

    public String generateToken(User user) {
        Date now = new Date();
        Map<String, Object> claim = new HashMap<>();
        claim.put("alg", "HS256");
        claim.put("typ", "JWT");

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Long.parseLong(expireTimeInMiliSec) * 1000))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .setHeaderParams(claim)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDate(token);
        return expirationDate.before(new Date());
    }

    public Boolean isTokenValidated(String token) {
        return !isTokenExpired(token);
    }
}
