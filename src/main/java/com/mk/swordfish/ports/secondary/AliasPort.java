package com.mk.swordfish.ports.secondary;

import com.mk.swordfish.core.domain.AliasDo;
import java.util.List;

public interface AliasPort {


  AliasDo createAlias(AliasDo aliasDo);

  boolean existsByAlias(List<String> aliases);

  boolean existsByDocumentValueAndDocumentType(String documentValue, String documentType);
}
