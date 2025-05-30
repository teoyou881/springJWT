package teo.springjwt.user;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teo.springjwt.common.entity.BaseTimeEntity;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id") // 명시적으로 컬럼명 지정
  private Long id;
  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;
  // 일단 옵션으로
  private String username;
  @Column(name = "password", nullable = false, length = 255) // 비밀번호 컬럼, 필수 (암호화된 비밀번호 저장)
  private String password;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Enumerated(value = STRING)
  @Column(name = "role", nullable = false)
  private UserRole role;



}

