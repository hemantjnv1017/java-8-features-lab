package com.interview.java8.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI: http://localhost:8081/swagger-ui/index.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI java8LabOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Java 8 Features Lab API")
                        .description("""
                                Hands-on Java 8 revision for ~3–4 YOE interviews.

                                1. GET /api/modules — catalog
                                2. Run a module or single demo
                                3. Read interviewTip (30–60s answer)

                                Question bank: INTERVIEW-QUESTIONS-3-4YOE.md
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("Interview Prep Lab")));
    }
}
