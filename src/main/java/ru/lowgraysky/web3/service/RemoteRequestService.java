package ru.lowgraysky.web3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class RemoteRequestService {

  protected final String BASE_URL;

  protected <T> Function<ClientResponse, Mono<T>> responseFunction(Class<T> tClass) {
    return (response) -> response.bodyToMono(tClass);
  }

  protected WebClient webClient(String path, Map<String, Object> params){
    return WebClient.builder()
            .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder(path, params)))
            .build();
  }

  protected UriComponentsBuilder uriComponentsBuilder(String path, Map<String, Object> params){
    return UriComponentsBuilder
            .newInstance()
            .host(BASE_URL)
            .path(path)
            .uriVariables(params);
  }
}
