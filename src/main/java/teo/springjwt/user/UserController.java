package teo.springjwt.user;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
