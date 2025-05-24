package teo.springjwt.user.dto;

import lombok.Data;
import teo.springjwt.user.UserRole;

@Data
public class RegisterDTO {

  private String username;
  private String password;
  private UserRole role;
}
