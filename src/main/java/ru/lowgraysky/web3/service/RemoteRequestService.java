package ru.lowgraysky.web3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public abstract class RemoteRequestService {

  protected final String BASE_URL;
  private final String REQUEST_SCHEMA = "https";
  private final String REQUST_LOG_TEMPLATE = "Perfrom '{}' request to '{}', with params '{}'";

  protected void logRequest(Logger logger, String method, String path, Map<String, Object> params) {
    logger.info(REQUST_LOG_TEMPLATE, method, path, params);
  }

  protected void logResponse(Logger logger, Object response) {
    logger.info("Response: '{}'", response.toString());
  }

  protected <T> Function<ClientResponse, Mono<T>> responseFunction(Class<T> tClass) {
    return (response) -> response.bodyToMono(tClass);
  }

  protected WebClient webClient(String path, Map<String, Object> params) {
    return WebClient.builder()
            .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder(path, params)))
            .build();
  }

  protected UriComponentsBuilder uriComponentsBuilder(String path, Map<String, Object> params) {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
            .newInstance()
            .scheme(REQUEST_SCHEMA)
            .host(BASE_URL)
            .path(path)
            .uriVariables(params);
    log.info("Created URI: {}", uriComponentsBuilder.toUriString());
    return uriComponentsBuilder;
  }
}
