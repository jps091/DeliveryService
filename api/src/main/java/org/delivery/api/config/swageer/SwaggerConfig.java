package org.delivery.api.config.swageer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "DeliveryService Api 명세서",
                description = "Spring Boot 기반 RESTful Api",
                version = "v1.0.0")
)
public class SwaggerConfig {
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper){
        return new ModelResolver(objectMapper);
    }
    @Bean
    public GroupedOpenApi customDeliveryOpenApi(){
        String[] paths = {"/open-api/**","/api/**"};
        return GroupedOpenApi.builder()
                .group("일반 사용자를 위한 Delivery Service 도메인 API")
                .pathsToMatch(paths)
                .build();
    }
}
