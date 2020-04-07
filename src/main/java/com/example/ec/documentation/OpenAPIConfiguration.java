package com.example.ec.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Explore California API's").description(
                        "API's for Explore California Travel Service").version("1.0.0-SNAPSHOT")
                        .contact(new Contact().name("Learning")
                                .url("learning.com").email("me@learning.com")));
    }

    // with Swagger, it looked like as below

//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.ec")).paths(any()).build()
//                .apiInfo(new ApiInfo("Explore California API's", "API's for Californoia Travek Service",
//                        "1.0", null, new Contact("Learning", "learning.com", "me@learning.com"),
//                        null, null, new ArrayList<>()));
//    }
}
