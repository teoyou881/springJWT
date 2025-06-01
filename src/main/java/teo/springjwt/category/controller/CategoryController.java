package teo.springjwt.category.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.category.dao.CategoryCreateDTO;
import teo.springjwt.category.dao.CategoryResponseDTO;
import teo.springjwt.category.service.CategoryService;

@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<String> createCategory(@Valid CategoryCreateDTO requestDto) {
    // 서비스 계층으로 DTO 전달
    categoryService.createCategory(requestDto);
    return ResponseEntity.ok("Category created successfully");
  }

  @GetMapping
  public ResponseEntity<List<CategoryResponseDTO>> getListAllCategories() {
    List<CategoryResponseDTO> allCategories = categoryService.getFlatListOfAllCategories();
    return ResponseEntity.ok(allCategories);
  }

  @GetMapping("/tree")
  public ResponseEntity<List<CategoryResponseDTO>> getTreeAllCategories() {
    List<CategoryResponseDTO> allCategories = categoryService.getHierarchicalCategories();
    return ResponseEntity.ok(allCategories);
  }


}
