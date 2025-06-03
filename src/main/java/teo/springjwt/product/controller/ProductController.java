package teo.springjwt.product.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.product.dto.ResponseProductEntity;
import teo.springjwt.product.dto.request.ProductCreateRequest;
import teo.springjwt.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

  private final ProductService productService;
  @GetMapping
  public ResponseEntity<List<ResponseProductEntity>> getProduct() {
    List<ResponseProductEntity> allProducts = productService.getAllProducts();
    return ResponseEntity.ok(allProducts);
  }

  @PostMapping
  public ResponseEntity<String> createProduct(@Valid @RequestBody ProductCreateRequest dto) {

   productService.createProduct(dto);


    return ResponseEntity.ok("Product created successfully");
  }
}
