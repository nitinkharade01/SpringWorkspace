package com.nitin.payment.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private static final List<String> PUBLIC_PATHS = List.of("/api/auth/login", "/api/auth/register", "/swagger-ui", "/v3/api-docs", "/actuator");
    private static final Map<String, List<String>> ROLE_RULES = Map.of(
            "/api/auth/users", List.of("ADMIN"),
            "/api/transactions", List.of("ADMIN", "FINANCE_USER"),
            "/api/fraud-alerts", List.of("ADMIN", "AUDITOR"),
            "/api/reconciliation", List.of("ADMIN", "FINANCE_USER"),
            "/api/reports", List.of("ADMIN", "AUDITOR"),
            "/api/notifications", List.of("ADMIN", "FINANCE_USER", "AUDITOR")
    );
    private final SecretKey secretKey;

    public JwtAuthenticationFilter(@Value("${security.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS || PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(header.substring(7)).getPayload();
            List<String> roles = extractRoles(claims.get("roles"));
            List<String> requiredRoles = requiredRoles(path);
            if (!requiredRoles.isEmpty() && roles.stream().noneMatch(requiredRoles::contains)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            ServerWebExchange authenticatedExchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-Authenticated-User", claims.getSubject())
                            .header("X-User-Id", String.valueOf(claims.get("userId")))
                            .header("X-User-Roles", String.join(",", roles)))
                    .build();
            return chain.filter(authenticatedExchange);
        } catch (ResponseStatusException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(ex.getStatusCode().value()));
            return exchange.getResponse().setComplete();
        } catch (RuntimeException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private List<String> requiredRoles(String path) {
        return ROLE_RULES.entrySet().stream()
                .filter(entry -> path.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(List.of());
    }

    private List<String> extractRoles(Object rolesClaim) {
        if (rolesClaim instanceof Collection<?> roles) {
            return roles.stream().map(String::valueOf).toList();
        }
        if (rolesClaim instanceof String roles) {
            return List.of(roles.split(","));
        }
        return List.of();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
