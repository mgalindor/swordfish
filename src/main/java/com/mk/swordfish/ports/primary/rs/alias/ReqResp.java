package com.mk.swordfish.ports.primary.rs.alias;

import com.mk.swordfish.core.enums.ChannelType;
import com.mk.swordfish.core.enums.IdentityDocumentType;
import com.mk.swordfish.ports.primary.rs.constraints.ValueOfEnum;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import org.javamoney.moneta.Money;

public class ReqResp {

  record IdentityDocument(@NotBlank @Size(max = 20) String value,
                          @ValueOfEnum(IdentityDocumentType.class) @NotNull String type) {

  }

  record Channels(@NotBlank @Size(max = 20) String value,
                  @ValueOfEnum(ChannelType.class) @NotNull String type) {

  }

  record PersonalInformation(@NotBlank @Size(max = 50) String firstname,
                             @Size(max = 50) String lastname,
                             @NotNull @Past LocalDate dob,
                             @NotNull @Valid IdentityDocument identityDocument) {

  }

  record Account(@NotBlank @Size(max = 20) String number, @NotBlank @Size(max = 20) String type,
                 @Future YearMonth exp, Money limit) {

  }

  record CreateAlias(
      @NotEmpty @Size(min = 1, max = 2) List<@NotEmpty @Size(max = 20) String> aliases,
      @Valid @NotNull PersonalInformation personal,
      @NotEmpty @Size(min = 1, max = 2) List<@Valid Account> accounts,
      @NotEmpty @Size(min = 1, max = 2) List<@Valid Channels> channels) {

  }

  record ResponseId(String id) {

  }
}
