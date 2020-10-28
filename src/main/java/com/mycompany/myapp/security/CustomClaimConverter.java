package com.mycompany.myapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

public class CustomClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(CustomClaimConverter.class);

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    private final ClientRegistration registration;

    public CustomClaimConverter(ClientRegistration registration) {
        this.registration = registration;
    }

    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);

        Mono.subscriberContext()
            .subscribe(context -> {
                log.debug("CONTEXT SIZE : {}", context.size());
            });

        log.debug("USER INFO : {}", registration.getProviderDetails().getUserInfoEndpoint());

        return convertedClaims;
    }
}
