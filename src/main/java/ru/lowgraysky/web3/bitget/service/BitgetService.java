package ru.lowgraysky.web3.bitget.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.lowgraysky.web3.bitget.config.BitgetCoreProperties;
import ru.lowgraysky.web3.bitget.config.BitgetPathProperties;
import ru.lowgraysky.web3.service.RemoteRequestService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Map;

@Service
public class BitgetService extends RemoteRequestService {

    private final BitgetCoreProperties bitgetCoreProperties;
    private final BitgetPathProperties bitgetPathProperties;

    public BitgetService(
            String BASE_URL,
            BitgetCoreProperties coreProperties,
            BitgetPathProperties pathProperties
    ) {
        super(BASE_URL);
        this.bitgetCoreProperties = coreProperties;
        this.bitgetPathProperties = pathProperties;
    }

    private WebClient webClientWithAuth(String path, MultiValueMap<String, String> params) {
        Map<String, List<String>> headers = Map.of(
                "ACCESS-KEY", List.of(bitgetCoreProperties.getApiKey()),
                "ACCESS-SIGN", List.of(),
                "ACCESS-TIMESTAMP", List.of(Long.toString(System.currentTimeMillis())),
                "ACCESS-PASSPHRASE", List.of(bitgetCoreProperties.getPassphrase()),
                HttpHeaders.CONTENT_TYPE, List.of(ContentType.APPLICATION_JSON.toString())
        );
        return WebClient.builder()
                .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder(path, params)))
                .defaultHeaders(httpHeaders -> HttpHeaders.formatHeaders(new MultiValueMapAdapter<>(headers)))
                .build();
    }

    @SneakyThrows
    private String signature(String requestPath, String queryString, String body, String secretKey) {
//        Mac mac = Mac.getInstance("HmacSHA256");
//        String timestamp =
//        String queryString = StringUtils.isBlank(queryString) ? StringUtils.EMPTY : "?" + queryString;
//        String preHash = timestamp + method + requestPath + queryString + body;
//        byte[] secretKeyBytes = secretKey.getBytes("UTF-8");
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
//        mac.init(secretKeySpec);
//        return Base64.getEncoder().encodeToString(mac.doFinal(preHash.getBytes("UTF-8")));
    }
}
