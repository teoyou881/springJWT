package teo.springjwt.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teo.springjwt.product.entity.OptionValueEntity;
import teo.springjwt.product.repository.value.OptionValueEntityRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ValueService {

  private final OptionValueEntityRepository valueRepository;

  public List<OptionValueEntity> getValues() {
    return valueRepository.findAll();
  }
}
