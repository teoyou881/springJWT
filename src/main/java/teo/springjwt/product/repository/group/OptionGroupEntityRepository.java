package teo.springjwt.product.repository.group;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.springjwt.product.entity.OptionGroupEntity;

public interface OptionGroupEntityRepository extends JpaRepository<OptionGroupEntity, Long>, OptionGroupEntityRepositoryCustom{

}
