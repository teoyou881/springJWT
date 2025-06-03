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
  @Column(name = "product_id") // 명시적으로 컬럼명 지정
  private Long id;

  @NotBlank(message = "상품명은 필수입니다")
  @Column(name = "product_name", nullable = false)
  private String name;

  @NotBlank(message = "상품 설명은 필수입니다")
  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "category_id", nullable = false) // A product must belong to a category
  private CategoryEntity category;

  //생성은 이미지와 따로 하고, 나중에 이미지를 등록할 때, sku를 가지고 와서 등록하는 방식으로.
  //null 을 허용해주자.
  @Column(nullable = true)
  @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<ImageUrlEntity> images = new ArrayList<>(); // 이미지 엔티티 리스트

  // 간단하게 URL 하나만 저장하거나, 여러 URL을 쉼표로 구분한 문자열로 저장하는 것을 고려
  @Column(name = "thumbnail_url", nullable = true) // 대표 이미지 URL
  private String thumbnailUrl;

  @OneToMany(mappedBy = "product",  cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<ReviewEntity> reviews = new ArrayList<>();

  /*
   * groups, sku 둘다 필요하다.
   * groups는 선택가능한 옵션을 보여주고,
   * sku는 실제 고객이 구매 가능한 재고, 가격등을 보여준다.
   * */
  @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<ProductOptionGroupEntity> productOptionGroups = new ArrayList<>();

  // Sku와 1:N 관계 (양방향 매핑)
  // cascade = CascadeType.ALL, orphanRemoval = true: Product 삭제 시 연관된 Sku도 함께 삭제
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

  //양방향
  public void addProductOptionGroup(ProductOptionGroupEntity productOptionGroup) {
    if (productOptionGroup != null && !this.productOptionGroups.contains(productOptionGroup)) {
      this.productOptionGroups.add(productOptionGroup);
      productOptionGroup.setProduct(this); // ProductOptionGroupEntity에 setProduct 메서드 호출
    }
  }

  // 비즈니스 메서드 (setter 대신)
  public void updateProductInfo(String name, String description, String thumbnailUrl, CategoryEntity category) {
    if (name != null && !name.trim().isEmpty()) {
      this.name = name;
    }
    if (description != null && !description.trim().isEmpty()) {
      this.description = description;
    }
    this.thumbnailUrl = thumbnailUrl; // Nullable
    if (category != null) {
      this.category = category;
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

  public void addImage(ImageUrlEntity image) {
    if (image != null && !this.images.contains(image)) {
      this.images.add(image);
      image.setProduct(this);
    }
  }

  public void removeImage(ImageUrlEntity image) {
    if (image != null && this.images.remove(image)) {
      image.setProduct(null);
    }
  }

  //todo
  //상품 등록 서비스 메서드 내에 추가해주자.
  public void updateThumbnailUrlFromImages() {
    this.thumbnailUrl = this.images.stream()
                                   .filter(ImageUrlEntity::isThumbnail)
                                   .map(ImageUrlEntity::getImageUrl)
                                   .findFirst()
                                   .orElse(null);
  }
}
