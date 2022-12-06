package com.mk.swordfish.ports.secondary.jpa.entity;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;
import org.apache.commons.codec.binary.Base32;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.util.StringUtils;

public class ShortUUIDGenerator implements IdentifierGenerator {

  private Base32 encoder = new Base32();

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    UUID uuid = UUID.randomUUID();
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return StringUtils.trimTrailingCharacter(encoder.encodeToString(bb.array()), '=');
  }
}
