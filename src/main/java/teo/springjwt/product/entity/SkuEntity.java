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
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_id", nullable = false) // 외래 키 컬럼명
  private ProductEntity product; // 연관관계의 주인

  @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다")
  @Column(name = "stock", nullable = false)
  private int stock; // 이 SKU의 실제 재고 수량

  @Column(nullable = false)
  private String name; // SKU 이름 (예: "상품명 - 빨강 - M")

  @Column(nullable = true, length = 500)
  private String description; // SKU 설명 (예: "색상: 빨강, 사이즈: M")

  @NotNull(message = "가격은 필수입니다")
  @Min(value = 0, message = "가격은 0 이상이어야 합니다")
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price; // 이 SKU의 최종 판매 가격 (product.basePrice + optionValue.extraPrice 합산된 가격)

  // SkuOptionValue와의 1:N 관계 (SKU가 어떤 ProductOptionValue들로 구성되었는지)
  // Sku와 ProductOptionValue의 다대다 관계를 해소하는 조인 엔티티
  @OneToMany(mappedBy = "sku", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<SkuOptionValueEntity> skuOptionValues = new ArrayList<>();

  @Builder
  public SkuEntity(ProductEntity product, BigDecimal price, int stock, String name, String description) {
    this.product = product;
    this.price = price;
    this.stock = stock;
    this.name = name;
    this.description = description;
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

  public void addSkuOptionValue(SkuOptionValueEntity skuOptionValue) {
    if(skuOptionValue!=null && !skuOptionValues.contains(skuOptionValue)) {
      skuOptionValues.add(skuOptionValue);
      skuOptionValue.setSku(this);
    }
  }

  // 비즈니스 메서드 (재고 변경)
  public void addStock(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("추가할 재고 수량은 0 이상이어야 합니다.");
    }
    this.stock += quantity;
  }

  public void deductStock(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("차감할 재고 수량은 0 이상이어야 합니다.");
    }
    if (this.stock < quantity) {
      throw new IllegalStateException("재고가 부족합니다."); // 재고 부족 예외
    }
    this.stock -= quantity;
  }

  public void updatePrice(BigDecimal newPrice) {
    if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) >= 0) {
      this.price = newPrice;
    } else {
      throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
    }
  }
}
