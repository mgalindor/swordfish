package com.mk.swordfish.ports.secondary.jpa;

import com.mk.swordfish.core.domain.AliasDo;
import com.mk.swordfish.ports.secondary.AliasPort;
import com.mk.swordfish.ports.secondary.jpa.entity.AliasEntity;
import com.mk.swordfish.ports.secondary.jpa.mapper.AliasJpaMapper;
import com.mk.swordfish.ports.secondary.jpa.repo.AliasJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AliasJpaAdapter implements AliasPort {

  private final AliasJpaRepository aliasJpaRepository;
  private final AliasJpaMapper aliasJpaMapper;

  @Override
  @NewSpan
  public AliasDo createAlias(AliasDo aliasDo) {
    AliasEntity alias = aliasJpaRepository.save(aliasJpaMapper.toEntity(aliasDo));
    return aliasJpaMapper.toDomain(alias);
  }

  @Override
  @NewSpan
  public boolean existsByAlias(List<String> aliases) {
    return aliasJpaRepository.existsByAlias(aliases.stream().collect(Collectors.joining(","))) > 0;
  }

  @Override
  @NewSpan
  public boolean existsByDocumentValueAndDocumentType(String documentValue, String documentType) {
    return aliasJpaRepository.existsByDocumentValueAndDocumentType(documentValue, documentType);
  }
}
