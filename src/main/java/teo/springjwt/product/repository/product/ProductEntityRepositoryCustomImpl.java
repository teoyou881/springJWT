package teo.springjwt.product.repository.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class ProductEntityRepositoryCustomImpl implements ProductEntityRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  public ProductEntityRepositoryCustomImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

}
