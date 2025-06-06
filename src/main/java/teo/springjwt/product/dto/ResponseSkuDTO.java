package teo.springjwt.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import teo.springjwt.product.entity.SkuEntity;

@Data
@Builder
public class ResponseSkuDTO {
  private Long id;
  private String skuCode;
  private String description;
  private BigDecimal price;
  private int stock;
  private String thumbnailUrl; // 썸네일 이미지 URL
  private List<ResponseImageDto> images; // 모든 이미지 URL 목록 --> responseImageDto로 변경
  private List<ResponseSkuOptionValueDTO> optionValues;

  // Entity에서 DTO로 변환
  public static ResponseSkuDTO fromEntity(SkuEntity skuEntity) {
    // 이미지 URL 목록 생성
    List<ResponseImageDto> images = skuEntity.getImages().stream().map(ResponseImageDto::fromEntity).toList();

    // 썸네일 URL 찾기
    String thumbnailUrl = skuEntity.getThumbnailUrl();

    // 옵션 값들 변환
    List<ResponseSkuOptionValueDTO> optionValues = skuEntity.getSkuOptionValues().stream()
        .map(ResponseSkuOptionValueDTO::fromEntity)
        .collect(Collectors.toList());

    return ResponseSkuDTO.builder()
        .id(skuEntity.getId())
        .skuCode(skuEntity.getName())
        .description(skuEntity.getDescription())
        .price(skuEntity.getPrice())
        .stock(skuEntity.getStock())
        .thumbnailUrl(thumbnailUrl)
        .images(images)
        .optionValues(optionValues)
        .build();
  }
}
