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
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teo.springjwt.category.CategoryEntity;
import teo.springjwt.common.entity.BaseTimeEntity;
import teo.springjwt.review.ReviewEntity;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProductEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long id;

  @NotBlank(message = "상품명은 필수입니다")
  @Column(name = "product_name", nullable = false)
  private String name;

  @NotBlank(message = "상품 설명은 필수입니다")
  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<ReviewEntity> reviews = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<ProductOptionGroupEntity> productOptionGroups = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<SkuEntity> skus = new ArrayList<>();

  // 생성자 (필수 필드 초기화)
  public ProductEntity(String name, String desc, CategoryEntity category) {
    this.name = name;
    this.description = desc;
    if (category != null) {
      this.category = category;
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof ProductEntity that)) {
      return false;
    }

    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  // 양방향 관계 편의 메서드들
  public void addProductOptionGroup(ProductOptionGroupEntity productOptionGroup) {
    if (productOptionGroup != null && !this.productOptionGroups.contains(productOptionGroup)) {
      this.productOptionGroups.add(productOptionGroup);
      productOptionGroup.setProduct(this);
    }
  }

  public void addSku(SkuEntity sku) {
    if (sku != null && !this.skus.contains(sku)) {
      this.skus.add(sku);
      sku.setProduct(this);
    }
  }

  public void removeSku(SkuEntity sku) {
    if (sku != null && this.skus.remove(sku)) {
      sku.setProduct(null);
    }
  }

  // 비즈니스 메서드
  public void updateProductInfo(String name, String description, CategoryEntity category) {
    if (name != null && !name.trim().isEmpty()) {
      this.name = name;
    }
    if (description != null && !description.trim().isEmpty()) {
      this.description = description;
    }
    if (category != null) {
      this.category = category;
    }
  }
}