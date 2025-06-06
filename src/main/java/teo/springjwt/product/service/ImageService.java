
package teo.springjwt.product.service;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import teo.springjwt.product.dto.ResponseImageDto;
import teo.springjwt.product.entity.ImageUrlEntity;
import teo.springjwt.product.entity.SkuEntity;
import teo.springjwt.product.repository.image.ImageUrlRepository;
import teo.springjwt.product.repository.sku.SkuRepository;
import teo.springjwt.util.CloudinaryStorageService;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

  private final ImageUrlRepository imageUrlRepository;
  private final SkuRepository skuRepository;
  private final CloudinaryStorageService fileStorageService;

  public ResponseImageDto addImageToSku(Long skuId, MultipartFile imageFile,  boolean isThumbnail, int displayOrder)
      throws IOException {

    //원본 파일명 가지고 오기.
    String originalFileName = imageFile.getOriginalFilename();

    String imageUrl = fileStorageService.saveFile(imageFile, originalFileName);

    SkuEntity sku = skuRepository.findById(skuId)
                                 .orElseThrow(() -> new EntityNotFoundException("SKU not found with ID: " + skuId));



    // 썸네일로 설정하는 경우 기존 썸네일 해제
    if (isThumbnail) {
      sku.getImages().forEach(image -> image.setAsThumbnail(false));
    }

    ImageUrlEntity imageEntity = new ImageUrlEntity(imageUrl,originalFileName, displayOrder, isThumbnail, sku);
    sku.addImage(imageEntity);

    ImageUrlEntity save = imageUrlRepository.save(imageEntity);
    return  ResponseImageDto.fromEntity(save);
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