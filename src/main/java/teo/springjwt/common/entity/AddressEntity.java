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
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id") // 명시적으로 컬럼명 지정
    private Long id;

    @Column(name = "country") private String country;
    @Column(name = "city") private String city;
    @Column(name = "state") private String state;
  @Column(name = "addr_line_1") private String addressLine1;
  @Column(name = "addr_line_2") private String addressLine2;
  @Column(name = "zip_code") private String zipCode;
  @Column(name = "is_default") private Boolean isDefault;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // 외래 키
  private UserEntity user;
}
