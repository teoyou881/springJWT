package teo.springjwt.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import teo.springjwt.user.UserEntity;

@Entity
@Getter
@Setter
public class CardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "card_id") // 명시적으로 컬럼명 지정
  private Long id;

  @Column(name = "card_number", nullable = false, length = 20)
  private String cardNumber;

  @Column(name = "card_type", nullable = false, length = 30)
  private String cardType;

  @Column(name = "owner_name", nullable = false, length = 50)
  private String ownerName;

  @Column(name = "expiry_date", nullable = false, length = 5)
  private String expiryDate; // 예: "12/26"

  @Column(name = "cvv", nullable = false, length = 4)
  private String cvv;

  @Column(name = "is_default", nullable = false)
  private boolean isDefault;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // 외래 키
  private UserEntity user;
}
