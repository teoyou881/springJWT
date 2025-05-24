package teo.springjwt.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/admin")
  public String adminP() {

    return "admin Controller";
  }
}
