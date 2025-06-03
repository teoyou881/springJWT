package teo.springjwt.product.repository.sku;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teo.springjwt.product.entity.SkuEntity;

@Repository
public interface SkuRepository extends JpaRepository<SkuEntity, Long> {
}