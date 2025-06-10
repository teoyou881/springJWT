package teo.springjwt.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import teo.springjwt.product.entity.ImageUrlEntity;
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
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

  // QueryDSL용 생성자
  @QueryProjection
  public ResponseProductEntity(Long id, String name, String description, Long categoryId, String categoryName,
      String thumbnailUrl, BigDecimal minPrice, BigDecimal maxPrice, LocalDateTime createdDate,
      LocalDateTime lastModifiedDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.thumbnailUrl = thumbnailUrl;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.createdDate = createdDate;
    this.lastModifiedDate = lastModifiedDate;
  }

  // Entity to DTO 변환 메서드 (SKU에서 썸네일 URL 가져오도록 수정)
  public static ResponseProductEntity from(ProductEntity productEntity, BigDecimal minPrice, BigDecimal maxPrice) {
    // SKU들 중에서 썸네일 이미지 찾기
    String thumbnailUrl = productEntity.getSkus().stream()
        .flatMap(sku -> sku.getImages().stream())
        .filter(ImageUrlEntity::isThumbnail)
        .map(ImageUrlEntity::getImageUrl)
        .findFirst()
        .orElse(null);

    return ResponseProductEntity.builder()
        .id(productEntity.getId())
        .name(productEntity.getName())
        .description(productEntity.getDescription())
        .categoryId(productEntity.getCategory().getId())
        .categoryName(productEntity.getCategory().getName())
        .thumbnailUrl(thumbnailUrl) // SKU에서 가져온 썸네일
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .createdDate(productEntity.getCreatedDate())
        .lastModifiedDate(productEntity.getLastModifiedDate())
        .build();
  }

  public static ResponseProductEntity from(ProductEntity productEntity) {
    // SKU들 중에서 썸네일 이미지 찾기
    String thumbnailUrl = productEntity.getSkus().stream()
                                       .flatMap(sku -> sku.getImages().stream())
                                       .filter(ImageUrlEntity::isThumbnail)
                                       .map(ImageUrlEntity::getImageUrl)
                                       .findFirst()
                                       .orElse(null);

    return ResponseProductEntity.builder()
                                .id(productEntity.getId())
                                .name(productEntity.getName())
                                .description(productEntity.getDescription())
                                .categoryId(productEntity.getCategory().getId())
                                .categoryName(productEntity.getCategory().getName())
                                .thumbnailUrl(thumbnailUrl) // SKU에서 가져온 썸네일
                                .createdDate(productEntity.getCreatedDate())
                                .lastModifiedDate(productEntity.getLastModifiedDate())
                                .build();
  }


  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public void setMinPrice(BigDecimal minPrice) {
    this.minPrice = minPrice;
  }

  public void setMaxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }

}