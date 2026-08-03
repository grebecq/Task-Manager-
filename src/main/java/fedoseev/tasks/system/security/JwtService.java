package fedoseev.tasks.system.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "my-super-secret-key-for-jwt-signing-minimum-32-chars"; // password min 32 sumb
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // live time token

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date()) // Time create token
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // time expiration
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {  //read token
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload() // тело токена
                .getSubject();//  username
    }

    public boolean isTokenValid(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}