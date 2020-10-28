package com.mycompany.myapp.config;

import com.mycompany.myapp.GeneratedByJHipster;
import io.github.jhipster.config.JHipsterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Hooks;

@Configuration
@Profile("!" + JHipsterConstants.SPRING_PROFILE_PRODUCTION)
@GeneratedByJHipster
public class ReactorConfiguration {

    public ReactorConfiguration() {
        Hooks.onOperatorDebug();
    }
}
