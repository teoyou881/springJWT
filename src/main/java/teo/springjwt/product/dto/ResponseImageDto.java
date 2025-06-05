package teo.springjwt.product.dto; // DTO 패키지에 맞게 조정하세요.

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teo.springjwt.product.entity.ImageUrlEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseImageDto {
  private Long id; // image_url_id
  private String imageUrl; // 이미지 URL
  private int displayOrder; // 표시 순서
  private boolean isThumbnail; // 썸네일 여부

  private Long skuId;

  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

  /**
   * ImageUrlEntity로부터 ResponseImageDto를 생성하는 팩토리 메서드.
   * Product 및 SKU 정보를 포함합니다.
   *
   * @param entity 변환할 ImageUrlEntity 객체
   * @return 생성된 ResponseImageDto 객체
   */
  public static ResponseImageDto fromEntity(ImageUrlEntity entity) {
    if (entity == null) {
      return null;
    }
    Long skuId = null;
    if (entity.getSku() != null) {
      skuId = entity.getSku().getId();
    }

    return ResponseImageDto.builder()
                           .id(entity.getId())
                           .imageUrl(entity.getImageUrl())
                           .displayOrder(entity.getDisplayOrder())
                           .isThumbnail(entity.isThumbnail())
                           .skuId(skuId)
                           .createdDate(entity.getCreatedDate())
                           .lastModifiedDate(entity.getLastModifiedDate())
                           .build();
  }
}