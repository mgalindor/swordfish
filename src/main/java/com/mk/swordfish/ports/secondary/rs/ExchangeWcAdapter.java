package com.mk.swordfish.ports.secondary.rs;

import com.mk.swordfish.ports.secondary.ExchangePort;
import com.mk.swordfish.ports.secondary.rs.ExchangeRsAdapter.ExchangeRequest;
import com.mk.swordfish.ports.secondary.rs.ExchangeRsAdapter.ExchangeResponse;
import com.mk.swordfish.spring.properties.ExchangeProperties;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Primary
@Profile("webclient")
public class ExchangeWcAdapter implements ExchangePort {

  private WebClient webClient;
  private ExchangeProperties properties;


  public ExchangeWcAdapter(WebClient.Builder builder, ExchangeProperties properties) {
    this.webClient = builder.build();
    this.properties = properties;
  }

  @Override
  @NewSpan
  public Money changeFromUSDToEUR(Money current) {
    return webClient.post()
        .uri(properties.getUrl())
        .body(Mono.just(new ExchangeRequest(current, "EUR")), ExchangeRequest.class)
        .retrieve()
        .bodyToMono(ExchangeResponse.class)
        .block()
        .exchange();
  }
}
