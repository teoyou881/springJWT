package teo.springjwt.product.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.product.dto.ResponseProductEntity;
import teo.springjwt.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<List<ResponseProductEntity>> getProducts(){
    return productService.getAllProducts();
  }

}
