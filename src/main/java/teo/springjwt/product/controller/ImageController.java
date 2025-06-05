package teo.springjwt.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teo.springjwt.product.dto.ResponseImageDto;
import teo.springjwt.product.service.ImageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sku")
public class ImageController {

  private final ImageService imageService;

  @PostMapping("/addImage")
  public ResponseEntity<String> uploadSingleFile(
      Long skuId,
      @RequestParam("imageFile") MultipartFile imageFile,
      boolean isThumbnail,
      int displayOrder
  ) {
    if (imageFile.isEmpty()) {
      return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
    }
    try {
      ResponseImageDto uploadedImage = imageService.addImageToSku(skuId, imageFile, isThumbnail, displayOrder);
      return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED); // 201 Created
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 유효성 검사 실패 시
    } catch (Exception e) {
      // 로깅: e.getMessage()
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
