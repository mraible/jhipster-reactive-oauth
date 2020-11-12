package com.mycompany.myapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;


public class CustomClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(CustomClaimConverter.class);

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    private final ReactiveClientRegistrationRepository registrationRepository;
    private final WebClient webClient;

    public CustomClaimConverter(ReactiveClientRegistrationRepository registrationRepository, WebClient webClient) {
        this.registrationRepository = registrationRepository;
        this.webClient = webClient;
    }

    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);
        log.debug("convertedClaims : {} ", convertedClaims);

        Mono.subscriberContext()
            .subscribe(context -> {
                log.debug("CONTEXT SIZE : {}", context.size());
            });

        registrationRepository.findByRegistrationId("oidc").subscribe(registration -> {
            String userInfoUri = registration.getProviderDetails()
                .getUserInfoEndpoint().getUri();
            log.info("userinfo endpoint {}", userInfoUri);

            Mono<Map> userAttributes = webClient.get()
                .uri(userInfoUri)
                .attributes(clientRegistrationId("oidc"))
                .retrieve()
                .bodyToMono(Map.class);

            userAttributes.subscribe(System.out::println);
        });

        return convertedClaims;
    }
}
