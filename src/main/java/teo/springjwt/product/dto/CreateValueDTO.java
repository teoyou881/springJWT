package teo.springjwt.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import teo.springjwt.product.entity.OptionGroupEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor  // JSON 바인딩을 위한 기본 생성자
public class CreateValueDTO {
  @NotBlank(message = "옵션 값은 필수입니다")
  private String valueName;

  @NotNull(message = "옵션 그룹 정보는 필수입니다")
  private OptionGroupEntity optionGroup; // 연관관계의 주인

}
