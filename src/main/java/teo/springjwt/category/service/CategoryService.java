package teo.springjwt.category.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teo.springjwt.category.CategoryEntity;
import teo.springjwt.category.dao.CategoryCreateDTO;
import teo.springjwt.category.dao.CategoryResponseDTO;
import teo.springjwt.category.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }


  /**
   * 계층 구조 (Tree) 형태의 카테고리 목록을 반환합니다.
   * - 최상위 카테고리만 조회하여 시작하고, DTO 변환 시 재귀적으로 자식들을 포함합니다.
   * - 프론트엔드에서 트리 뷰를 렌더링하는 데 적합합니다.
   *
   * @return 계층 구조의 CategoryResponseDTO 리스트
   */
  @Transactional(readOnly = true)
  public List<CategoryResponseDTO> getHierarchicalCategories() {
    // 1. 데이터베이스에서 parentCategory가 null인 최상위 카테고리만 가져옵니다.
    List<CategoryEntity> rootCategories = categoryRepository.findByParentCategoryIsNull();

    // 2. 각 최상위 CategoryEntity를 CategoryResponseDTO.fromEntityTree()를 사용하여 계층 구조 DTO로 변환합니다.
    return rootCategories.stream()
                         .map(CategoryResponseDTO::fromEntityTree) // 계층 DTO 팩토리 메서드 호출
                         .sorted(Comparator.comparingInt(CategoryResponseDTO::getDisplayOrder)) // 필요한 경우 정렬
                         .toList();
  }

  /**
   * 모든 카테고리를 포함하는 단일 평면 리스트 (Flat List) 형태의 카테고리 목록을 반환합니다.
   * - 데이터베이스의 모든 카테고리를 가져와 재귀적으로 평면화하고 중복을 제거합니다.
   * - childCategories 필드는 DTO 변환 시 항상 빈 리스트로 설정됩니다.
   * - 검색, 필터링, 드롭다운 목록 등 전체 카테고리를 한 번에 보여줄 때 적합합니다.
   *
   * @return 평면 리스트 형태의 CategoryResponseDTO 리스트
   */
  @Transactional(readOnly = true)
  public List<CategoryResponseDTO> getFlatListOfAllCategories() {
    // 1. 데이터베이스의 모든 CategoryEntity를 가져옵니다.
    List<CategoryEntity> allCategories = categoryRepository.findAll();

    // 2. Set을 사용하여 중복을 제거하면서 모든 카테고리를 모읍니다.
    //    CategoryEntity의 equals/hashCode가 id를 기준으로 잘 구현되어 있어야 합니다.
    Set<CategoryEntity> uniqueCategories = new HashSet<>();

    // 3. 재귀적으로 모든 카테고리를 평면화하여 Set에 추가합니다.
    allCategories.stream()
                 .flatMap(this::flattenCategoryTree) // 재귀적으로 모든 하위 카테고리까지 Stream으로 변환
                 .forEach(uniqueCategories::add);

    // 4. Set에 있는 고유한 CategoryEntity를 CategoryResponseDTO.fromEntityFlat()을 사용하여 DTO로 변환합니다.
    //    이때 fromEntityFlat 메서드가 childCategories를 빈 리스트로 설정합니다.
    return uniqueCategories.stream()
                           .map(CategoryResponseDTO::fromEntityFlat) // 평면 DTO 팩토리 메서드 호출
                           .sorted(Comparator.comparing(CategoryResponseDTO::getParentId, Comparator.nullsFirst(Comparator.naturalOrder()))
                                       .thenComparing(CategoryResponseDTO::getId)) // 예시: ID 순으로 정렬
                           .toList(); // Java 16+ .toList()
  }

  //create
  public CategoryEntity createCategory(CategoryCreateDTO request) {
    if (categoryRepository.existsByName(request.getName())) {
      throw new IllegalArgumentException("Category with name '" + request.getName() + "' already exists.");
    }



    CategoryEntity parentCategory = null;
    // request.getParentId()가 null이 아닌 경우, 부모 카테고리 조회
    if (request.getParentId() != null) {
      // findById는 Optional을 반환하므로, orElseThrow를 사용하여 부모가 없으면 예외 발생
      parentCategory = categoryRepository.findById(request.getParentId())
                                         .orElseThrow(() -> new IllegalArgumentException("Parent category not found with ID: " + request.getParentId()));
    }

    Integer maxDisplayOrder = categoryRepository.findMaxDisplayOrderByParentId(request.getParentId()).orElseGet(() -> 0);

    // CategoryEntity 생성자 사용 (CategoryEntity 코드에 생성자가 정의되어 있어야 함)
    // 1. 최상위 카테고리 생성 (parentCategory가 null)
    // 2. 자식 카테고리 생성 (parentCategory가 존재)
    CategoryEntity newCategory;
    if(parentCategory == null) {
      newCategory = new CategoryEntity(request.getName(),maxDisplayOrder+1);
    }else {
      newCategory= new CategoryEntity(request.getName(),maxDisplayOrder+1, parentCategory);
    }


    System.out.println("newCategory = " + newCategory);

    return categoryRepository.save(newCategory);
  }

  public CategoryEntity updateCategory(CategoryEntity category) {
    return null;
  }

  public CategoryEntity deleteCategory(Long id) {
    CategoryEntity category = categoryRepository.findById(id)
                                             .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    if (category.getParentCategory() != null) {
      category.getParentCategory().removeChildCategory(category);
    }
    categoryRepository.delete(category);
    return category;
  }


  // 헬퍼 메서드
  /**
   * CategoryEntity와 그 모든 자식들을 하나의 Stream으로 평면화하는 헬퍼 메서드.
   * (getFlatListOfAllCategories()에서 사용됨)
   *
   * @param category 현재 처리할 카테고리 엔티티
   * @return 현재 카테고리와 그 모든 하위 자식 카테고리를 포함하는 Stream
   */
  private Stream<CategoryEntity> flattenCategoryTree(CategoryEntity category) {
    Stream<CategoryEntity> current = Stream.of(category);
    Stream<CategoryEntity> children = (category.getChildCategories() != null && !category.getChildCategories().isEmpty())
                                      ? category.getChildCategories().stream().flatMap(this::flattenCategoryTree)
                                      : Stream.empty();
    return Stream.concat(current, children);
  }
}
