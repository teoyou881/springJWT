package teo.springjwt.product.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teo.springjwt.product.entity.OptionValueEntity;
import teo.springjwt.product.service.ValueService;

@RestController
@RequestMapping("/admin/options/value")
@RequiredArgsConstructor
public class ValueController {

  private final ValueService valueService;

  @GetMapping
  public List<OptionValueEntity> getOptionValues() {
    return valueService.getValues();
  }
}
