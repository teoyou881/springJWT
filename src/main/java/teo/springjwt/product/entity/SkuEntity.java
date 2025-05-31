package teo.springjwt.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
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

  // 이 SKU가 속한 상품 (N:1 관계)
  @NotNull(message = "상품 정보는 필수입니다")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false) // 외래 키 컬럼명
  private ProductEntity product; // 연관관계의 주인

  // 첫 번째 옵션 값 (예: 색상) - 필수
  @NotNull(message = "색상 옵션은 필수입니다")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_color_id", nullable = false)
  private OptionValueEntity optionColor;

  // 두 번째 옵션 값 (예: 사이즈) - 선택 사항일 수 있음 (옵션이 하나만 있는 상품)
  @NotNull(message = "색상 옵션은 필수입니다")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_size_id",nullable = false)
  private OptionValueEntity optionSize;

  @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다")
  @Column(name = "stock_quantity", nullable = false)
  private int stockQuantity; // 이 SKU의 실제 재고 수량

  @NotNull(message = "가격은 필수입니다")
  @Min(value = 0, message = "가격은 0 이상이어야 합니다")
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price; // 이 SKU의 최종 판매 가격 (product.basePrice + optionValue.extraPrice 합산된 가격)

  @Builder
  public SkuEntity(ProductEntity product, OptionValueEntity optionColor, OptionValueEntity optionSize, int stockQuantity, BigDecimal price) {
    this.product = product;
    this.optionColor = optionColor;
    this.optionSize = optionSize; // null 허용
    this.stockQuantity = stockQuantity;
    this.price = price;
  }

  // override equals & hashcode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SkuEntity that = (SkuEntity) o;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode(); // 또는 Objects.hash(id);
  }

  // 연관관계 편의 메서드 (양방향 매핑 시)
  public void setProduct(ProductEntity product) {
    this.product = product;
  }

  // 비즈니스 메서드 (재고 변경)
  public void addStock(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("추가할 재고 수량은 0 이상이어야 합니다.");
    }
    this.stockQuantity += quantity;
  }

  public void deductStock(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("차감할 재고 수량은 0 이상이어야 합니다.");
    }
    if (this.stockQuantity < quantity) {
      throw new IllegalStateException("재고가 부족합니다."); // 재고 부족 예외
    }
    this.stockQuantity -= quantity;
  }

  public void updatePrice(BigDecimal newPrice) {
    if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
      this.price = newPrice;
    } else {
      throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
    }
  }
}
