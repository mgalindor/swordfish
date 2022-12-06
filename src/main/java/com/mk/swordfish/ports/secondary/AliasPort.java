package com.mk.swordfish.ports.secondary;

import com.mk.swordfish.core.domain.AliasDO;
import java.util.List;

public interface AliasPort {


  AliasDO createAlias(AliasDO aliasDO);

  boolean existsByAlias(List<String> aliases);

  boolean existsByDocumentValueAndDocumentType(String documentValue,String documentType);
}
