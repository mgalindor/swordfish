package com.mk.swordfish.core.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
public class AliasDo {

  private String id;
  private String firstname;
  private String lastname;
  private LocalDate dob;
  private IdentityDocumentDo identityDocument;
  private List<String> aliases;
  private List<AccountDo> accounts;
  private List<ChannelDo> channels;


  @Data
  public static class AccountDo {

    private String number;
    private String type;
    private YearMonth exp;
    private Money limit;
  }

  @Data
  public static class ChannelDo {

    private String value;
    private String type;
  }

  @Data
  public static class IdentityDocumentDo {

    private String value;
    private String type;
  }
}
