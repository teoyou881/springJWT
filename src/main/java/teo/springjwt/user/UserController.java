package teo.springjwt.user;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.user.dto.RegisterDTO;
import teo.springjwt.user.service.SignUpService;

@RestController
@RequiredArgsConstructor
public class UserController {

 private final  SignUpService signUpService;

  @GetMapping("/admin")
  public String adminP() {

    return "admin Controller";
  }

  @PostMapping("/user")
  public String signUp(RegisterDTO registerDTO) {
    signUpService.signUpProcess(registerDTO);
    return "ok";
  }

  @GetMapping("/user")
  public String user(Principal principal) {
    System.out.println(principal);
    return "admin Controller";
  }

  // 이렇게 조회할 경우, 트랜잭션 범위 밖에서 user를 찾는다.
  // 따라서, 영속성 컨텍스트에서 관리되지 않는 상태. detached.
  // dirty checking 되지 않는다.
  @GetMapping("/user/{id}")
  public String findUser(@PathVariable("id") UserEntity user) {
    return user.getUsername();
  }
}
