package com.mk.swordfish.ports.secondary.jpa.mapper;

import com.mk.swordfish.core.domain.AliasDO;
import com.mk.swordfish.ports.secondary.jpa.entity.AliasEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AliasJpaMapper {

  @Mapping(source = "identityDocument.value", target = "documentValue")
  @Mapping(source = "identityDocument.type", target = "documentType")

  @Mapping(source = "aliases", target = "details.aliases")
  @Mapping(source = "accounts", target = "details.accounts")
  @Mapping(source = "channels", target = "details.channels")
  AliasEntity toEntity(AliasDO aliasDO);

  @InheritInverseConfiguration
  AliasDO toDomain(AliasEntity aliasEntity);
}
