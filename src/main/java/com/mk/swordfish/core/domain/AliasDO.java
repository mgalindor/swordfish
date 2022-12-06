package com.mk.swordfish.core.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
public class AliasDO {

  private String id;
  private String firstname;
  private String lastname;
  private LocalDate dob;
  private IdentityDocumentDO identityDocument;
  private List<String> aliases;
  private List<AccountDO> accounts;
  private List<ChannelDO> channels;


  @Data
  public static class AccountDO {

    private String number;
    private String type;
    private YearMonth exp;
    private Money limit;
  }

  @Data
  public static class ChannelDO {

    private String value;
    private String type;
  }

  @Data
  public static class IdentityDocumentDO {

    private String value;
    private String type;
  }
}
