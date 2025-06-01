package teo.springjwt.category.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import teo.springjwt.category.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  // 특정 이름의 카테고리가 존재하는지 확인
  boolean existsByName(String name);

  List<CategoryEntity> findByParentCategoryIsNull();
}
