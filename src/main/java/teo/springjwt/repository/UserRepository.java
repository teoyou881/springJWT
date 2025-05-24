package teo.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teo.springjwt.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
