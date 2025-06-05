package teo.springjwt.product.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teo.springjwt.product.dto.ResponseSkuDTO;
import teo.springjwt.product.entity.SkuEntity;
import teo.springjwt.product.repository.sku.SkuOptionValueRepository;
import teo.springjwt.product.repository.sku.SkuRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SkuService {

  private final SkuRepository skuRepository;
  private final SkuOptionValueRepository skuValueRepository;

  public List<ResponseSkuDTO> getAllOptionValueWithProductId(Long id) {
    List<SkuEntity> skuWithProductId = skuRepository.findBySkuWithProductId(id);
    List<ResponseSkuDTO> responseSkus = new ArrayList<>();
    for (SkuEntity skuEntity : skuWithProductId) {
      responseSkus.add(ResponseSkuDTO.fromEntity(skuEntity));
    }
    return responseSkus;
  }

  public ResponseSkuDTO getSkuById(Long skuId) {
    SkuEntity skuEntity = skuRepository.findById(skuId)
                                         .orElseThrow(() -> new IllegalArgumentException("sku not found with ID: " + skuId));
    return ResponseSkuDTO.fromEntity(skuEntity);
  }
}
