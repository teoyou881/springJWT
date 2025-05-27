package teo.springjwt.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import teo.springjwt.user.dto.CustomUserDetails;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  // 이미 준비된 manager를 주입받아서 사용하자.
  private final AuthenticationManager authenticationManager;

  private final JWTUtil jwtUtil;


  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    // username, password 획득
    String username = obtainUsername(request);
    String password = obtainPassword(request);

    log.info("LoginFilter attemptAuthentication username: {}, password: {}", username, password);
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
    return authenticationManager.authenticate(authToken);
  }

  // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authentication) {

    //UserDetailsS
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String username = customUserDetails.getUsername();

    // 부여받은 role이 2개 이상일 수 있기 때문에,
    // Collection으로 반환.
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Collection<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();


    String token = jwtUtil.createJwt(username, roles, 60*60*10L);

    response.addHeader(jwtUtil.getJwtProperties().getHeaderString(), jwtUtil.getJwtProperties().getTokenPrefix() + " " + token);
    response.setStatus(HttpServletResponse.SC_OK); // 200 OK
  }

  // 로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) {
    // 실패시 401 응답 코드
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
