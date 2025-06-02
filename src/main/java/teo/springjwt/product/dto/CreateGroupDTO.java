package teo.springjwt.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGroupDTO {
  @NotBlank(message = "옵션 이름은 필수입니다")
  private String name;

  @NotNull(message = "순서는 필수입니다.")
  private int displayOrder;
}
