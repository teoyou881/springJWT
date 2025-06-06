package teo.springjwt.product.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teo.springjwt.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = { // Sku의 유일성을 위한 복합 유니크 제약 조건
    // 하나의 상품 내에서 옵션 조합은 유일해야 함
    @UniqueConstraint(columnNames = {"product_id", "option_color_id", "option_size_id"})
    // 옵션이 3개 이상이라면 여기에 추가 (option_value3_id 등)
})
public class SkuEntity extends BaseTimeEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sku_id")
  private Long id;

  @NotNull(message = "상품 정보는 필수입니다")
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private ProductEntity product;

  @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다")
  @Column(name = "stock", nullable = false)
  private int stock;

  @Column(nullable = false)
  private String name;

  @Column(nullable = true, length = 500)
  private String description;

  @NotNull(message = "가격은 필수입니다")
  @Min(value = 0, message = "가격은 0 이상이어야 합니다")
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @OneToMany(mappedBy = "sku", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<SkuOptionValueEntity> skuOptionValues = new ArrayList<>();

  // 이미지와의 1:N 관계 추가
  @OneToMany(mappedBy = "sku", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<ImageUrlEntity> images = new ArrayList<>();

  @Builder
  public SkuEntity(ProductEntity product, BigDecimal price, int stock, String name, String description) {
    this.product = product;
    this.price = price;
    this.stock = stock;
    this.name = name;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SkuEntity)) return false;
    SkuEntity skuEntity = (SkuEntity) o;
    return Objects.equals(id, skuEntity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  // 연관관계 편의 메서드들
  public void setProduct(ProductEntity product) {
    this.product = product;
  }

  public void addSkuOptionValue(SkuOptionValueEntity skuOptionValue) {
    if (skuOptionValue != null && !this.skuOptionValues.contains(skuOptionValue)) {
      this.skuOptionValues.add(skuOptionValue);
      skuOptionValue.setSku(this);
    }
  }

  // 이미지 관련 편의 메서드들
  public void addImage(ImageUrlEntity image) {
    if (image != null && !this.images.contains(image)) {
      this.images.add(image);
      image.setSku(this);
    }
  }

  public void removeImage(ImageUrlEntity image) {
    if (image != null && this.images.remove(image)) {
      image.setSku(null);
    }
  }

  // 썸네일 이미지 URL 가져오기
  public String getThumbnailUrl() {
    return this.images.stream()
                     .filter(ImageUrlEntity::isThumbnail)
                     .map(ImageUrlEntity::getImageUrl)
                     .findFirst()
                     .orElse(null);
  }

  // 썸네일 설정 (기존 썸네일 해제 후 새로운 썸네일 설정)
  public void setThumbnail(ImageUrlEntity newThumbnail) {
    // 기존 썸네일 해제
    this.images.forEach(image -> image.setAsThumbnail(false));
    // 새로운 썸네일 설정
    if (newThumbnail != null && this.images.contains(newThumbnail)) {
      newThumbnail.setAsThumbnail(true);
    }
  }

  // 비즈니스 메서드들
  public void addStock(int quantity) {
    if (quantity > 0) {
      this.stock += quantity;
    }
  }

  public void deductStock(int quantity) {
    if (quantity > 0 && this.stock >= quantity) {
      this.stock -= quantity;
    } else {
      throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.stock);
    }
  }

  public void updatePrice(BigDecimal newPrice) {
    if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
      this.price = newPrice;
    }
  }
}