package com.mk.swordfish.ports.secondary;

import org.javamoney.moneta.Money;

public interface ExchangePort {

  Money changeFromUSDToEUR(Money current);
}
