package com.maveric.gatewayservice.configuration;

import com.maveric.gatewayservice.filter.AuthenticationPreFilter;
//import com.maveric.gatewayservice.filter.JwtAuthFilter;
import com.maveric.gatewayservice.properties.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, AuthenticationPreFilter authenticationPreFilter,
                                      BalanceProperties balanceProperties,
                                      AccountProperties accountProperties, UserProperties userProperties,
                                      TransactionProperties transactionProperties,
                                      AuthProperties authProperties) {
        return builder.routes()

                .route(routeToBalance -> routeToBalance.path(userProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(userProperties.getUri()))
                .route(routeToBalance -> routeToBalance.path(balanceProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(balanceProperties.getUri()))
                .route(routeToAccount -> routeToAccount.path(accountProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(accountProperties.getUri()))
                .route(routeToBalance -> routeToBalance.path(transactionProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(transactionProperties.getUri()))
                .route(routeToAccount -> routeToAccount.path(authProperties.getPath())
                        .uri(authProperties.getUri()))
                .build();
    }
}