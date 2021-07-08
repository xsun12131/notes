package com.fatpanda.notes.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author fatpanda
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger.enable}")
    private Boolean enableFlag;

    @Value("${swagger.loginUrl}")
    private String loginUrl;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                //设置Controller层所在的包名
                .apis(RequestHandlerSelectors.basePackage("com.fatpanda.notes"))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("notes api infos")
                .version("1.0")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    // private List<SecurityContext> securityContexts() {
    //     List<SecurityContext> securityContexts = new ArrayList<>();
    //     securityContexts.add(
    //             SecurityContext.builder()
    //                     .securityReferences(defaultAuth())
    //                     .forPaths(PathSelectors.regex("^(?!auth).*$"))
    //                     .build());
    //     return securityContexts;
    // }

    // private List<SecurityReference> defaultAuth() {
    //     AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    //     AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    //     authorizationScopes[0] = authorizationScope;
    //     List<SecurityReference> securityReferences = new ArrayList<>();
    //     securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
    //     return securityReferences;
    // }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header");
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder().securityReferences(defaultAuth())
                //过滤要验证的路径
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        securityContexts.add(securityContext());
        return securityContexts;
    }

    //增加全局认证
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
        return securityReferences;
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("login", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(loginUrl);

        return new OAuthBuilder()
                .name("login")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("all", "All scope is trusted!")
        };
    }

}
