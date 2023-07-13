package com.mk.swordfish.ports.secondary.jpa.repo;

import com.mk.swordfish.ports.secondary.jpa.entity.AliasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AliasJpaRepository extends JpaRepository<AliasEntity, String> {


  /**
   * Resumen del query.
   *
   * <p>! JPA by default read '?' character as positional query parameter, but
   * ! is not able to skip it all postgres json that use ? are NOT POSIBLE to
   * ! be used
   *
   * <p>Find a key in array if more than one is passed must exist all
   * select * from tx_aliases where details->'aliases' @> '["mike"]'::jsonb;
   * select * from tx_aliases where details->'aliases' ? 'mike'
   *
   * <p>Accept multiple keys evaluate if exist any
   * select * from tx_aliases where details->'aliases' ?| '{mike,gal3}';
   *
   * <p>JPA can not skip de ? operator and without the operator we can not use index :(
   */

  @Query(value = "select count(id) from tx_aliases where details->'aliases'  ?| '{:toFind}' ",
      nativeQuery = true)
  int existsByAlias(@Param("toFind") String toFind);

  boolean existsByDocumentValueAndDocumentType(String documentValue, String documentType);
}
