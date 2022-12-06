package com.mk.swordfish.spring.config;

import com.mk.swordfish.spring.properties.HttpClientProperties;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Slf4j
@Profile("!webclient")
@Configuration
public class RestTemplateConfiguration {

  @Bean
  @ConditionalOnProperty(name = "httpclient.type", havingValue = "java", matchIfMissing = true)
  RestTemplateCustomizer javaRestTemplateCustomizer(HttpClientProperties httpClientProperties) {
    log.info("####################  UrlConnection rest template configuration  ####################");
    HttpClientProperties.UrlConnection config = httpClientProperties.getUrlConnection();

    SimpleClientHttpRequestFactory clientFactory = new SimpleClientHttpRequestFactory();
    clientFactory.setConnectTimeout(config.getMaxConnectionTimeout().toMillisPart());
    clientFactory.setReadTimeout(config.getMaxReadTimeout().toMillisPart());
    return restTemplate -> restTemplate.setRequestFactory(clientFactory);
  }

  @Bean
  @ConditionalOnProperty(name = "httpclient.type", havingValue = "basicApache")
  RestTemplateCustomizer basicApacheRestTemplateCustomizer(HttpClientProperties httpClientProperties) {
    log.info("####################  Basic Apache rest template configuration  ####################");

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    return restTemplate -> restTemplate.setRequestFactory(
        requestFactory);
  }

  /**
   * https://springframework.guru/using-resttemplate-with-apaches-httpclient/
   */
  @Bean
  @ConditionalOnProperty(name = "httpclient.type", havingValue = "apache")
  RestTemplateCustomizer apacheRestTemplateCustomizer(HttpClientProperties httpClientProperties) {
    log.info("####################  Apache rest template configuration  ####################");
    HttpClientProperties.HttpClient config = httpClientProperties.getHttpClient();

    RequestConfig requestConfig = RequestConfig.custom()
        .setConnectTimeout(config.getConnectTimeout().toMillisPart())
        .setConnectionRequestTimeout(config.getConnectionRequestTimeout().toMillisPart())
        .setSocketTimeout(config.getSocketTimeout().toMillisPart())
        .build();

    HttpClient client = HttpClients.custom()
        .setDefaultRequestConfig(requestConfig)
        .setConnectionManager(poolingConnectionManager(httpClientProperties))
        .setKeepAliveStrategy(connectionKeepAliveStrategy(httpClientProperties))
        .build();

    return restTemplate -> restTemplate.setRequestFactory(
        new HttpComponentsClientHttpRequestFactory(client));
  }

  @Bean
  PoolingHttpClientConnectionManager poolingConnectionManager(
      HttpClientProperties httpClientProperties) {
    HttpClientProperties.HttpClient config = httpClientProperties.getHttpClient();

    PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(
        config.getDefaultKeepAlive().toSeconds(), TimeUnit.SECONDS);
    poolingConnectionManager.setDefaultMaxPerRoute(config.getMaxPerRoute());
    poolingConnectionManager.setMaxTotal(config.getMaxTotal());
    return poolingConnectionManager;
  }

  @Bean
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(
      HttpClientProperties httpClientProperties) {
    HttpClientProperties.HttpClient config = httpClientProperties.getHttpClient();
    return (httpResponse, httpContext) -> {
      HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
      HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

      while (elementIterator.hasNext()) {
        HeaderElement element = elementIterator.nextElement();
        String param = element.getName();
        String value = element.getValue();
        if (value != null && param.equalsIgnoreCase("timeout")) {
          return Long.parseLong(value) * 1000; // convert to ms
        }
      }

      return config.getDefaultKeepAlive().toMillisPart();
    };
  }

  @Bean
  @ConditionalOnProperty(name = "httpclient.type", havingValue = "okhttp")
  RestTemplateCustomizer okHttpRestTemplateCustomizer(HttpClientProperties httpClientProperties) {
    log.info("####################  OkHttp rest template configuration  ####################");

    HttpClientProperties.OkHttp config = httpClientProperties.getOkHttp();

    ConnectionPool okHttpConnectionPool = new ConnectionPool(
        config.getPoolMaxIdle(),
        config.getPoolKeepAlive(),
        config.getPoolTimeUnit());

    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .connectionPool(okHttpConnectionPool)
        .connectTimeout(config.getConnectTimeout())
        .readTimeout(config.getReadTimeout())
        .callTimeout(config.getReadTimeout())
        .retryOnConnectionFailure(false);

    return restTemplate -> restTemplate.setRequestFactory(
        new OkHttp3ClientHttpRequestFactory(builder.build()));
  }


}
