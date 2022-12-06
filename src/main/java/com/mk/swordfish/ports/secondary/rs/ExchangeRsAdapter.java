package com.mk.swordfish.ports.secondary.rs;

import com.mk.swordfish.ports.secondary.ExchangePort;
import com.mk.swordfish.spring.properties.ExchangeProperties;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExchangeRsAdapter implements ExchangePort {

  private RestTemplate restTemplate;
  private ExchangeProperties properties;

  public ExchangeRsAdapter(RestTemplateBuilder builder, ExchangeProperties properties) {
    this.restTemplate = builder.build();
    this.properties = properties;
  }

  record ExchangeRequest(Money from, String to) { }

  record ExchangeResponse(Money exchange, Double rate, Money origin) {  }

  @Override
  @NewSpan
  public Money changeFromUSDToEUR(Money current) {
    return restTemplate.postForObject(properties.getUrl(),
        new ExchangeRequest(current, "EUR"),
        ExchangeResponse.class)
        .exchange;
  }
}
