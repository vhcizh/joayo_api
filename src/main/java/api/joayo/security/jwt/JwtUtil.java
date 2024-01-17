package api.joayo.security.jwt;

import api.joayo.security.SecurityMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

//    // 임의로 만들기
//    public JwtUtil() {
//        secretKey = Jwts.SIG.HS256.key().build();
//    }

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        // application.properties에 지정해둔 key 받아오기
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private static final long TOKEN_VALID_TIME = 1000 * 60 * 10L; // 토큰 유효시간 10분


    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_VALID_TIME);

        SecurityMember member = (SecurityMember) authentication.getPrincipal();
        String nickname = member.getNickname();
        String username = member.getUsername();
        String role = member.getAuthorities().iterator().next().getAuthority();
//        String role = String.valueOf(member.getAuthorities().stream().findFirst().get());

        return Jwts.builder()
                .subject(username)
                .claim("email", username)
                .claim("nickname",nickname)
                .claim("role",role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();

    }


    public String getUsername(String token) {

        return getClaims(token)
                .get("username", String.class);
    }

    public String getRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    public String getNickname(String token) {

        return getClaims(token)
                .get("nickname", String.class);
    }

    public Boolean isExpired(String token) throws ExpiredJwtException {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}