package teo.springjwt.product.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.product.entity.OptionGroupEntity;
import teo.springjwt.product.service.GroupService;

@RestController
@RequestMapping("/admin/options/type")
@RequiredArgsConstructor
public class GroupController {

  private final GroupService groupService;
  
  @GetMapping
  public List<OptionGroupEntity> getOptionTypes() {
     return groupService.getGroups();
  }

  @PostMapping
  public void createOptionType() {
  }
}
