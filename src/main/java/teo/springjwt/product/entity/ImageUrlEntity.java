package teo.springjwt.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teo.springjwt.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrlEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue
  @Column(name = "image_url_id")
  private Long id;

  @NotNull(message="이미지 이름은 필수입니다..")
  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "display_order", nullable = false) // Order is important for display
  private int displayOrder;

  @Column(name = "is_thumbnail", nullable = false) // Is it a thumbnail?
  private boolean isThumbnail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id",nullable = false)
  private ProductEntity product;

  // Constructor for mandatory fields
  public ImageUrlEntity(String imageUrl, int displayOrder, boolean isThumbnail, ProductEntity product) {
    if (imageUrl == null || imageUrl.trim().isEmpty()) throw new IllegalArgumentException("Image URL is required.");
    if (displayOrder < 0) throw new IllegalArgumentException("Display order must be non-negative.");
    if (product == null) throw new IllegalArgumentException("Product is required for image.");

    this.imageUrl = imageUrl;
    this.displayOrder = displayOrder;
    this.isThumbnail = isThumbnail;
    this.product = product;
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
  public void setProduct(ProductEntity product) {
    this.product = product;
  }
}