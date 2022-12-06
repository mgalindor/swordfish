package com.mk.swordfish.spring.properties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("httpclient")
public class HttpClientProperties {

  private static final int HTTP_MAX_IDLE = 40;
  private static final int HTTP_KEEP_ALIVE = 20;
  private static final int HTTP_CONNECTION_TIMEOUT = 20;
  private static final int HTTP_READ_TIMEOUT = 20;
  private static final int MAX_ROUTE_CONNECTIONS = 40;
  private static final int MAX_TOTAL_CONNECTIONS = 40;

  private OkHttp okHttp = new OkHttp();
  private HttpClient httpClient = new HttpClient();
  private UrlConnection urlConnection = new UrlConnection();

  @Getter
  @Setter
  public static class UrlConnection {

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration maxConnectionTimeout = Duration.ofSeconds(HTTP_CONNECTION_TIMEOUT);
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration maxReadTimeout = Duration.ofSeconds(HTTP_READ_TIMEOUT);
  }

  @Getter
  @Setter
  public static class HttpClient {

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration connectTimeout = Duration.ofSeconds(HTTP_CONNECTION_TIMEOUT);
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration connectionRequestTimeout = Duration.ofSeconds(HTTP_READ_TIMEOUT);
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration socketTimeout = Duration.ofSeconds(HTTP_READ_TIMEOUT);

    private int maxPerRoute = MAX_ROUTE_CONNECTIONS;
    private int maxTotal = MAX_TOTAL_CONNECTIONS;

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration defaultKeepAlive = Duration.ofSeconds(HTTP_KEEP_ALIVE);
    ;

  }

  @Getter
  @Setter
  public static class OkHttp {

    private int poolMaxIdle = HTTP_MAX_IDLE;
    private int poolKeepAlive = HTTP_KEEP_ALIVE;
    private TimeUnit poolTimeUnit = TimeUnit.SECONDS;

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration connectTimeout = Duration.ofSeconds(HTTP_CONNECTION_TIMEOUT);
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration readTimeout = Duration.ofSeconds(HTTP_READ_TIMEOUT);
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration callTimeout = Duration.ofSeconds(HTTP_READ_TIMEOUT);

  }

}
