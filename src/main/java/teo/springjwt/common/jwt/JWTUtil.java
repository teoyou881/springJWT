package teo.springjwt.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

  private final JWTProperties jwtProperties;
  private final SecretKey secretKey;

  // 생성자: JWTProperties를 주입받아 SecretKey를 초기화
  public JWTUtil(JWTProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    // JWTProperties에서 Base64 인코딩된 비밀 키를 가져와 SecretKey로 변환
    // Keys.hmacShaKeyFor()는 Base64 디코딩을 자동으로 처리
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)); // Use UTF-8 for consistency
  }

  // 토큰에서 사용자 이름을 추출
  public String getUsername(String token) {
    try {
      return extractClaim(token).get("username", String.class);
    } catch (Exception e) {
      // JWT 관련 예외 처리: 예를 들어, 로그를 남기고 null 또는 특정 예외를 다시 던질 수 있음
      System.err.println("Error extracting username from token: " + e.getMessage());
      // 클라이언트에게는 유효하지 않은 토큰임을 알리기 위해 null 반환 또는 사용자 정의 예외 처리
      return null;
    }
  }

  // 토큰에서 역할을 추출
  public String getRole(String token) {
    try {
      return extractClaim(token).get("role", String.class);
    } catch (Exception e) {
      System.err.println("Error extracting role from token: " + e.getMessage());
      return null;
    }
  }

  // 토큰 만료 여부 확인
  public Boolean isExpired(String token) {
    try {
      return extractClaim(token).getExpiration().before(new Date());
    } catch (ExpiredJwtException e) {
      // 만료된 토큰인 경우 ExpiredJwtException 발생
      return true;
    } catch (Exception e) {
      System.err.println("Error checking token expiration: " + e.getMessage());
      return true; // 또는 false, 상황에 따라 처리
    }
  }

  // JWT 생성
  public String createJwt(String username, String role, Long expirationMs) {
    long currentTime = System.currentTimeMillis();
    Date issuedAt = new Date(currentTime);
    Date expiration = new Date(currentTime + expirationMs); // 외부에서 받은 만료 시간 사용

    return Jwts.builder()
               .claim("username", username)
               .claim("role", role)
               .issuedAt(issuedAt)
               .expiration(expiration)
               .signWith(secretKey)
               .compact();
  }

  // JWT 파싱 및 클레임 추출을 위한 내부 헬퍼 메서드
  private Claims extractClaim(String token) {
    return Jwts.parser()
               .verifyWith(secretKey)
               .build()
               .parseSignedClaims(token)
               .getPayload();
  }

  // JWT 검증 (추가)
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      // 잘못된 서명, 잘못된 JWT, 지원되지 않는 JWT, 잘못된 인자 등
      System.err.println("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      // 만료된 토큰
      System.err.println("Expired JWT token: " + e.getMessage());
    }
    return false;
  }
}