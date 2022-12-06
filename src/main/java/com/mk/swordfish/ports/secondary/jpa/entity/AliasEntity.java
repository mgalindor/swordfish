package com.mk.swordfish.ports.secondary.jpa.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.javamoney.moneta.Money;

@Entity
@Getter
@Setter
@Table(name = "tx_aliases")
@TypeDef(name = "json", typeClass = JsonType.class)
public class AliasEntity {

  @Id
  //@GenericGenerator(name = "uuid", strategy = "uuid")
  //@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
  @GeneratedValue(generator = "generator")
  @GenericGenerator(name = "generator", strategy = "com.mk.swordfish.ports.secondary.jpa.entity.XidGenerator")
  private String id;
  private String firstname;
  private String lastname;
  private LocalDate dob;
  private String documentValue;
  private String documentType;
  @Type(type = "json")
  @Column(columnDefinition = "jsonb")
  private AliasDetailsEntity details;

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class AliasDetailsEntity {

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<String> aliases;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<AccountEntity> accounts;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<ChannelEntity> channels;
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class AccountEntity {

    private String number;
    private String type;
    private YearMonth exp;
    private Money limit;
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class ChannelEntity {

    private String value;
    private String type;
  }

}
