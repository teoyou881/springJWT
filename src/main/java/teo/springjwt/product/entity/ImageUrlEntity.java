package teo.springjwt.product.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teo.springjwt.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrlEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_url_id")
  private Long id;

  @NotNull(message = "이미지 URL은 필수입니다")
  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @NotNull(message = "이미지 이름은 필수입니다.")
  @Column(length = 255)
  private String originalFileName;

  @Column(name = "display_order", nullable = false)
  private int displayOrder;

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof ImageUrlEntity that)) {
      return false;
    }

    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Column(name = "is_thumbnail", nullable = false)
  private boolean isThumbnail;

  // ProductEntity에서 SkuEntity로 연관관계 변경
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "sku_id", nullable = false)
  private SkuEntity sku;

  // Constructor for mandatory fields
  public ImageUrlEntity(String imageUrl, String originalFileName, int displayOrder, boolean isThumbnail, SkuEntity sku) {
    this.imageUrl = imageUrl;
    this.originalFileName = originalFileName; // 원본 파일명 포함
    this.displayOrder = displayOrder;
    this.isThumbnail = isThumbnail;
    this.sku = sku;
  }

  // Business methods
  public void updateImageUrl(String newImageUrl) {
    if (newImageUrl != null && !newImageUrl.trim().isEmpty()) {
      this.imageUrl = newImageUrl;
    }
  }

  public void updateDisplayOrder(int newOrder) {
    if (newOrder >= 0) {
      this.displayOrder = newOrder;
    }
  }

  public void setAsThumbnail(boolean isThumbnail) {
    this.isThumbnail = isThumbnail;
  }

  // Bidirectional link helper
  public void setSku(SkuEntity sku) {
    this.sku = sku;
  }
}