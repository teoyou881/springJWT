package teo.springjwt.product.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.product.dto.CreateGroupDTO;
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
  public ResponseEntity<String> createOptionType(
      @Valid @RequestBody
      CreateGroupDTO requestDto) {
    groupService.createGroup(requestDto);
    return ResponseEntity.ok("Group created successfully");
  }

  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteOptionType(@PathVariable Long id) {
    groupService.deleteGroup(id);
    return ResponseEntity.ok("Group deleted successfully");
  }
}
