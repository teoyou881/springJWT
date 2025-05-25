package teo.springjwt.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import teo.springjwt.common.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
  private final AuthenticationConfiguration authenticationConfiguration;
  public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
    this.authenticationConfiguration = authenticationConfiguration;
  }

  // 명시적으로 등록해야 한다.
  // security 5.0부터는 명시적으로 passwordEncoder를 빈으로 등록하지 않으면 예외 발생.
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // jwt 사용할 경우, csrf 를 비활성화 한다.
    /*
     *CSRF 공격과 JWT의 작동 방식
1. CSRF (Cross-Site Request Forgery) 공격:

목표: 사용자가 인증된 상태에서, 사용자의 의지와 상관없이 악성 웹사이트를 통해 특정 요청(예: 비밀번호 변경, 송금)을 보내도록 유도하는 공격입니다.
작동 방식: CSRF 공격은 **세션 쿠키(Session Cookies)**에 의존합니다. 사용자가 웹사이트 A에 로그인하여 세션 쿠키를 받으면, 이 쿠키는 브라우저에 저장되고 웹사이트 A로 보내는 모든 요청에 자동으로 포함됩니다. 악성 웹사이트 B가 웹사이트 A로 요청을 보내면, 브라우저는 웹사이트 A의 세션 쿠키를 자동으로 첨부하여 보내므로, 웹사이트 A는 해당 요청이 인증된 사용자로부터 온 것처럼 착각하게 됩니다.
Spring Security의 CSRF 보호: Spring Security는 기본적으로 CSRF 토큰을 사용하여 이를 방어합니다. 서버는 폼(form)이나 헤더에 예측 불가능한 CSRF 토큰을 포함시켜 응답하고, 클라이언트는 이 토큰을 다음 요청에 포함시켜 보냅니다. 서버는 요청이 올바른 토큰을 포함하는지 확인하여 악성 사이트로부터 온 요청을 방지합니다.
2. JWT (JSON Web Token) 인증:

작동 방식: JWT는 클라이언트(브라우저, 모바일 앱 등)가 서버로부터 JWT를 발급받아 **로컬 스토리지(LocalStorage)**나 세션 스토리지(SessionStorage) 또는 **HTTP 헤더(Authorization: Bearer &lt;token>)**에 저장하고, 서버로 요청을 보낼 때마다 이 JWT를 수동으로 HTTP 헤더에 포함시켜 보냅니다. 세션 쿠키처럼 브라우저에 의해 자동으로 첨부되지 않습니다.
CSRF에 대한 내성 (Immunity): JWT는 기본적으로 Stateless(무상태) 인증 메커니즘이며, 세션 쿠키에 의존하지 않습니다. 따라서 악성 웹사이트가 사용자의 브라우저를 통해 서버로 요청을 보내더라도, JWT는 HTTP 헤더에 수동으로 첨부되어야 하므로, 브라우저가 자동으로 JWT를 첨부하여 보내주지 않습니다. 이로 인해 CSRF 공격에 대한 위험이 현저히 낮아지거나 사라집니다.
결론: JWT 사용 시 CSRF 비활성화
JWT를 Authorization 헤더를 통해 사용하는 경우, CSRF 공격이 발생하기 위한 주요 전제 조건인 세션 쿠키의 자동 포함이 일어나지 않습니다. 따라서 Spring Security의 CSRF 보호 기능은 필요 없어지며, http.csrf((auth) -> auth.disable());를 통해 비활성화해도 안전합니다.

일반적인 JWT 기반 RESTful API에서는 CSRF 보호를 비활성화하는 것이 표준적인 접근 방식입니다.

주의사항 (edge cases):

쿠키에 JWT 저장 시: 만약 JWT를 HttpOnly가 아닌 일반 쿠키(JavaScript로 접근 가능)에 저장하거나, 쿠키에 저장하더라도 HttpOnly 쿠키와 함께 CSRF 토큰을 사용하지 않는다면 여전히 CSRF에 취약해질 수 있습니다. 하지만 JWT를 Authorization 헤더에 담아 전송하는 표준 방식은 이 문제를 피합니다.
Spring Security의 기본 인증과 JWT를 혼용하는 경우: 만약 애플리케이션의 일부는 세션 기반 인증을 사용하고 다른 부분은 JWT 인증을 사용한다면, CSRF 보호를 전역적으로 비활성화하는 것은 권장되지 않을 수 있습니다. 이 경우 특정 요청 경로에 대해서만 CSRF 보호를 비활성화하는 등의 세밀한 설정이 필요할 수 있습니다. 하지만 순수 JWT 기반 API라면 걱정할 필요가 없습니다.
요약:
대부분의 JWT 기반 REST API에서는 CSRF 보호가 불필요하며, http.csrf((auth) -> auth.disable());로 비활성화하는 것이 일반적이고 안전합니다.
     * */
    http.csrf((auth) -> auth.disable());
    // From 로그인 방식 disable
    http.formLogin((auth) -> auth.disable());
    // http basic 인증 방식 disable
    http.httpBasic((auth) -> auth.disable());

    // 경로별 인가 작업
    http.authorizeHttpRequests((auth) -> auth
        .requestMatchers("/login", "/", "/join").permitAll()
        .requestMatchers(HttpMethod.POST, "/user").permitAll()
        .requestMatchers("/admin").hasRole("ADMIN")
        .anyRequest().authenticated());

    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

    // 세션 설정
    http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
