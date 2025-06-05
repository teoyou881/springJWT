
package teo.springjwt.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import teo.springjwt.product.entity.ImageUrlEntity;
import teo.springjwt.product.entity.SkuEntity;
import teo.springjwt.product.repository.image.ImageUrlRepository;
import teo.springjwt.product.repository.sku.SkuRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

  private final ImageUrlRepository imageUrlRepository;
  private final SkuRepository skuRepository;

  public ImageUrlEntity addImageToSku(Long skuId, MultipartFile imageFile,  boolean isThumbnail, int displayOrder) {





    SkuEntity sku = skuRepository.findById(skuId)
                                 .orElseThrow(() -> new EntityNotFoundException("SKU not found with ID: " + skuId));

    // 썸네일로 설정하는 경우 기존 썸네일 해제
    if (isThumbnail) {
      sku.getImages().forEach(image -> image.setAsThumbnail(false));
    }

    ImageUrlEntity imageEntity = new ImageUrlEntity(imageUrl, displayOrder, isThumbnail, sku);
    sku.addImage(imageEntity);

    return imageUrlRepository.save(imageEntity);
  }

  public void removeImageFromSku(Long imageId) {
    ImageUrlEntity image = imageUrlRepository.findById(imageId)
                                             .orElseThrow(() -> new EntityNotFoundException("Image not found with ID: " + imageId));

    SkuEntity sku = image.getSku();
    sku.removeImage(image);
    imageUrlRepository.delete(image);
  }

  public void setAsThumbnail(Long imageId) {
    ImageUrlEntity image = imageUrlRepository.findById(imageId)
                                             .orElseThrow(() -> new EntityNotFoundException("Image not found with ID: " + imageId));

    SkuEntity sku = image.getSku();
    sku.setThumbnail(image);
    imageUrlRepository.save(image);
  }
}