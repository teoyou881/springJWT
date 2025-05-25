package teo.springjwt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.springjwt.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>, UserRepositoryCustom {

  Boolean existsByUsername(String username);
  UserEntity findByUsername(String username);
}
