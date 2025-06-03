package teo.springjwt.product.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import teo.springjwt.product.entity.ProductEntity;

@Data
@Builder
public class ResponseProductEntity {
  private Long id;
  private String name;
  private String description;
  private Long categoryId;
  private String categoryName;
  private String thumbnailUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Entity to DTO 변환 메서드
  public static ResponseProductEntity from(ProductEntity productEntity) {
    return ResponseProductEntity.builder()
                          .id(productEntity.getId())
                          .name(productEntity.getName())
                          .description(productEntity.getDescription())
                          .categoryId(productEntity.getCategory().getId())
                          .categoryName(productEntity.getCategory().getName())
                          .thumbnailUrl(productEntity.getThumbnailUrl())
                          .createdAt(productEntity.getCreatedDate())
                          .updatedAt(productEntity.getLastModifiedDate())
                          .build();
  }
}
