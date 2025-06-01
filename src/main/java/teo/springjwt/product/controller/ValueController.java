package teo.springjwt.product.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.product.dto.CreateGroupDTO;
import teo.springjwt.product.entity.OptionValueEntity;
import teo.springjwt.product.service.ValueService;

@RestController
@RequestMapping("/admin/options/value")
@RequiredArgsConstructor
public class ValueController {

  private final ValueService valueService;

  @GetMapping
  public List<OptionValueEntity> getOptionValues(@Valid @RequestBody CreateGroupDTO requestDto) {
    return valueService.getValues();
  }
}
