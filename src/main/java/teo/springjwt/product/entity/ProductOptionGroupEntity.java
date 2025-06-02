package teo.springjwt.product.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teo.springjwt.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_option_group") // 조인 테이블의 이름을 명시
public class ProductOptionGroupEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_option_group_id")
  private Long id;

  // Product와의 ManyToOne 관계: 여러 ProductOptionGroup이 하나의 Product에 속할 수 있음
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private ProductEntity product;

  // OptionGroup과의 ManyToOne 관계: 여러 ProductOptionGroup이 하나의 OptionGroup에 속할 수 있음
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "option_group_id", nullable = false)
  private OptionGroupEntity optionGroup;

  // 추가 속성 (예: 이 옵션 그룹이 상품에서 어떤 순서로 보여질지)
  @Column(name = "display_order", nullable = false)
  private int displayOrder; // 상품 내에서 이 옵션 그룹이 보여지는 순서

  @Builder
  public ProductOptionGroupEntity(ProductEntity product, OptionGroupEntity optionGroup, int displayOrder) {
    this.product = product;
    this.optionGroup = optionGroup;
    this.displayOrder = displayOrder;
  }

  // 연관관계 편의 메서드 (선택 사항, 양방향 매핑 시 유용)
  public void setProduct(ProductEntity product) {
    this.product = product;
  }

  public void setOptionGroup(OptionGroupEntity optionGroup) {
    this.optionGroup = optionGroup;
  }

  public void updateDisplayOrder(int displayOrder) {
    this.displayOrder = displayOrder;
  }
}