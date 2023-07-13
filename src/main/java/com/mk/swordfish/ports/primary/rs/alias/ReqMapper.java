package com.mk.swordfish.ports.primary.rs.alias;

import static com.mk.swordfish.ports.primary.rs.alias.ReqResp.CreateAlias;

import com.mk.swordfish.core.domain.AliasDo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReqMapper {

  @Mapping(source = "personal.firstname", target = "firstname")
  @Mapping(source = "personal.lastname", target = "lastname")
  @Mapping(source = "personal.dob", target = "dob")
  @Mapping(source = "personal.identityDocument", target = "identityDocument")
  AliasDo fromRequest(CreateAlias createAlias);
}
