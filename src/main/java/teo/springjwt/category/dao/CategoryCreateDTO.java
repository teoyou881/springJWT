package teo.springjwt.category.dao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateDTO {
  @NotBlank(message = "카테고리 이름은 필수입니다.") // 이름은 항상 필요
  private String name;

  // 부모 카테고리가 없는 경우 (최상위) null, 있는 경우 부모의 ID
  private Long parentId;

  @Min(value = 0, message = "Display order must be a non-negative number")
  private int displayOrder;

  // 모든 필드를 포함하는 생성자 (테스트 또는 초기화 편의용)
  public CategoryCreateDTO(String name,int displayOrder, Long parentId) {
    this.name = name;
    this.displayOrder = displayOrder;
    this.parentId = parentId;
  }

  // 최상위 카테고리 생성을 위한 팩토리 메서드
  public static CategoryCreateDTO of(String name, int displayOrder) {
    return new CategoryCreateDTO(name,displayOrder, null);
  }

  // 자식 카테고리 생성을 위한 팩토리 메서드
  public static CategoryCreateDTO of(String name,int displayOrder, Long parentId) {
    return new CategoryCreateDTO(name,displayOrder, parentId);
  }
}