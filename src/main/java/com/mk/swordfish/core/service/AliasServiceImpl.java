package com.mk.swordfish.core.service;

import com.mk.swordfish.core.annotations.Behavior;
import com.mk.swordfish.core.domain.AliasDO;
import com.mk.swordfish.core.exceptions.BusinessErrorException;
import com.mk.swordfish.core.exceptions.ErrorCodes;
import com.mk.swordfish.ports.secondary.AliasPort;
import com.mk.swordfish.ports.secondary.ExchangePort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.cloud.sleuth.annotation.NewSpan;

@Behavior
@RequiredArgsConstructor
public class AliasServiceImpl implements AliasService {

  private final AliasPort aliasPort;
  private final ExchangePort exchangePort;

  @Override
  @NewSpan
  public AliasDO createAlias(AliasDO alias) {

    if (aliasPort.existsByDocumentValueAndDocumentType(alias.getIdentityDocument().getValue(),
        alias.getIdentityDocument().getType())) {
      throw new BusinessErrorException(ErrorCodes.DOCUMENT_EXIST);
    }

    alias.getAccounts().stream().forEach(accountDO -> {
      Money eur = Optional.ofNullable(accountDO.getLimit()).map(exchangePort::changeFromUSDToEUR)
          .orElse(Money.of(100, "EUR"));
      accountDO.setLimit(eur);
    });
    // ! Because this method use PG ?| operator is not posible to be used with JPA nor native query
//    if (aliasPort.existsByAlias(alias.getAliases())) {
//      throw new BusinessErrorException(ErrorCodes.ALIAS_EXIST);
//    }

    return aliasPort.createAlias(alias);
  }
}
