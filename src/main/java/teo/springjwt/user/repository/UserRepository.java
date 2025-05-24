package teo.springjwt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.springjwt.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>, UserRepositoryCustom {

}
