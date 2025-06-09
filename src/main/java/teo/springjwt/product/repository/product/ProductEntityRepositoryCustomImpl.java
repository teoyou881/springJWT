package teo.springjwt.product.repository.product;

import static teo.springjwt.category.QCategoryEntity.categoryEntity;
import static teo.springjwt.product.entity.QImageUrlEntity.imageUrlEntity;
import static teo.springjwt.product.entity.QProductEntity.productEntity;
import static teo.springjwt.product.entity.QSkuEntity.skuEntity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import teo.springjwt.product.dto.QResponseProductEntity;
import teo.springjwt.product.dto.ResponseProductEntity;

public class ProductEntityRepositoryCustomImpl implements ProductEntityRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  public ProductEntityRepositoryCustomImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public Page<ResponseProductEntity> findAllProductsWithMinPriceAndMaxPrice(String name, String skuCode,
      Pageable pageable) {
    // 메인 쿼리: ResponseProductEntity로 프로젝션
    JPAQuery<ResponseProductEntity> query = queryFactory
        .select(new QResponseProductEntity(
            productEntity.id,
            productEntity.name,
            productEntity.description,
            categoryEntity.id,
            categoryEntity.name,
            // 썸네일 제외, 나중에 채울거다.
            Expressions.nullExpression(),
            skuEntity.price.min(), // SKU 최소 가격
            skuEntity.price.max(), // SKU 최대 가격
            productEntity.createdDate,
            productEntity.lastModifiedDate
        ))
        .from(productEntity)
        .leftJoin(productEntity.category, categoryEntity)
        .leftJoin(productEntity.skus, skuEntity)
        .groupBy(
            productEntity.id,
            productEntity.name,
            productEntity.description,
            categoryEntity.id,
            categoryEntity.name,
            productEntity.createdDate,
            productEntity.lastModifiedDate
        );

    // 검색 조건 동적으로 추가
    BooleanBuilder whereCondition = new BooleanBuilder();

    if (StringUtils.hasText(name)) {
      whereCondition.and(productEntity.name.lower().like("%" + name.toLowerCase() + "%"));
    }

    if (StringUtils.hasText(skuCode)) {
      whereCondition.and(
          JPAExpressions.selectOne()
                        .from(skuEntity)
                        .where(skuEntity.product.eq(productEntity)
                                                .and(skuEntity.name.lower().like("%" + skuCode.toLowerCase() + "%")))
                        .exists()
      );
    }

    // 검색 조건 적용
    query.where(whereCondition);

    // 페이징 적용
    query.offset(pageable.getOffset());
    query.limit(pageable.getPageSize());

    // 정렬 적용
    pageable.getSort().forEach(order -> {
      com.querydsl.core.types.Order direction = order.isAscending() ?
                                                com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
      if ("createdDate".equals(order.getProperty())) {
        query.orderBy(new com.querydsl.core.types.OrderSpecifier<>(direction, productEntity.createdDate));
      } else if ("name".equals(order.getProperty())) {
        query.orderBy(new com.querydsl.core.types.OrderSpecifier<>(direction, productEntity.name));
      }
    });

    // 쿼리 실행
    List<ResponseProductEntity> content = query.fetch();


    // productId 리스트로 얻어오기
    List<Long> ids = content.stream().map(ResponseProductEntity::getId).toList();

    //sku 상품 아아디, 이미지 url 리스트로 얻어오기
    List<Tuple> productIdToThumbnail = queryFactory
        .select(imageUrlEntity.sku.product.id, imageUrlEntity.imageUrl)
        .from(imageUrlEntity)
        .where(imageUrlEntity.isThumbnail.eq(true).and(imageUrlEntity.sku.product.id.in(ids)))
        .fetch()
        .stream()
        .toList();


    content.forEach(productEntity -> {
      String imageUrl = String.valueOf(productIdToThumbnail.get(Math.toIntExact(productEntity.getId())));
      productEntity.setThumbnailUrl(imageUrl);  // setter 필요
    });


    // 총 카운트 쿼리
    JPAQuery<Long> countQuery = queryFactory
        .select(productEntity.countDistinct())
        .from(productEntity)
        .leftJoin(productEntity.category, categoryEntity)
        .leftJoin(productEntity.skus, skuEntity)
        .where(whereCondition);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }
}