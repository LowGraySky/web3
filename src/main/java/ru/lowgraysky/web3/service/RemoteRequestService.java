package ru.lowgraysky.web3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class RemoteRequestService {

  private static final String SIGNATURE_ALGORITHM = "HmacSHA256";
  private final String REQUEST_SCHEMA = "https";
  private final String REQUEST_LOG_TEMPLATE = "Perform '{}' request to '{}', with params '{}'";
  protected final String BASE_URL;

  protected void logRequest(Logger logger, String method, String path, Map<String, List<String>> params) {
    logger.info(REQUEST_LOG_TEMPLATE, method, path, params);
  }

  protected void logResponse(Logger logger, Object response) {
    logger.info("Response: '{}'", response.toString());
  }

  protected <T> Function<ClientResponse, Mono<T>> responseFunction(Class<T> tClass) {
    return (response) -> response.bodyToMono(tClass);
  }

  protected WebClient webClient(String path, MultiValueMap<String, String> params) {
    return WebClient.builder()
            .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder(path, params)))
            .build();
  }

  protected String signature(String data, String secret) {
    return new HmacUtils(SIGNATURE_ALGORITHM, secret).hmacHex(data);
  }

  protected MultiValueMap<String, String> addSignatureToParameters(
          MultiValueMap<String, String> params, String secret
  ) {
    List<NameValuePair> valuePairs = params.toSingleValueMap()
            .entrySet()
            .stream()
            .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    String data = URLEncodedUtils.format(valuePairs, Charset.defaultCharset());
    String sign = signature(data, secret);
    params.add("signature", sign);
    return params;
  }

  protected UriComponentsBuilder uriComponentsBuilder(String path, MultiValueMap<String, String> params) {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
            .newInstance()
            .scheme(REQUEST_SCHEMA)
            .host(BASE_URL)
            .path(path)
            .queryParams(params);
    log.info("Created URI: {}", uriComponentsBuilder.toUriString());
    return uriComponentsBuilder;
  }
}
