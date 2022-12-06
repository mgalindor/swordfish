package com.mk.swordfish.ports.primary.rs.alias;

import static com.mk.swordfish.ports.primary.rs.alias.ReqResp.CreateAlias;

import com.mk.swordfish.core.domain.AliasDO;
import com.mk.swordfish.core.service.AliasService;
import io.micrometer.core.annotation.Timed;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Timed
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/aliases")
public class AliasController {

  private final AliasService aliasService;
  private final ReqMapper reqMapper;

  @PostMapping
  public ReqResp.ResponseId createAlias(@RequestBody @Valid CreateAlias createAlias) {
    AliasDO alias = aliasService.createAlias(reqMapper.fromRequest(createAlias));
    return new ReqResp.ResponseId(alias.getId());
  }

}
