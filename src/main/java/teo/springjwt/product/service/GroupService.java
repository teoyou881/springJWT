package teo.springjwt.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teo.springjwt.product.entity.OptionGroupEntity;
import teo.springjwt.product.repository.group.OptionGroupEntityRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

  private final OptionGroupEntityRepository groupRepository;


  public List<OptionGroupEntity> getGroups() {
    return groupRepository.findAll();
  }
}
