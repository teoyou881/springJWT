package teo.springjwt.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import teo.springjwt.user.UserRole;

@Data
public class RegisterDTO {

  @Size(min = 2, max = 100, message = "사용자 이메일은 2자 이상 100자 이하여야 합니다.")
  @NotBlank(message = "사용자 이름은 필수입니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 6, max = 50, message = "비밀번호는 6자 이상 50자 이하여야 합니다.")
  private String password;

  @NotNull(message = "사용자 권한은 필수입니다.")
  private UserRole role;
}
