package teo.springjwt.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

  @NotBlank(message = "상품명은 필수입니다.")
  private String name;

  @NotBlank(message = "상품 설명은 필수입니다")
  private String description; // 설명은 필수가 아님

  @NotNull(message = "카테고리 ID는 필수입니다.")
  private Long categoryId; // 상품이 속할 카테고리 ID

  private List<String> imageUrls; // 상품 이미지 URL 리스트 (간단하게 String으로)
}