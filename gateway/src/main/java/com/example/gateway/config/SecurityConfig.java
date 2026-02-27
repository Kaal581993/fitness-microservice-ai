package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeExchange(exchanges -> exchanges
                            .pathMatchers("/actuator/**").permitAll()
                            .pathMatchers("/api/auth/**").permitAll()
                            .pathMatchers("/api/users/**").permitAll()
                            .pathMatchers("/api/activities/**").permitAll()
                            .pathMatchers("/api/recommendation/**").permitAll()
                            .anyExchange().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> {})
                    );
            return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            //source.registerCorsConfiguration("/api/**", configuration);
            return source;
        }
    }



    /**
     *
     * SecurityWebFilterChain vs SecurityFilterChain
     * SecurityFilterChain (Servlet/MVC)
     * Used with Spring MVC applications (traditional web apps)
     * Works with spring-boot-starter-web (Servlet-based)
     * Processes requests in a blocking/synchronous manner
     * Part of org.springframework.security.web package
     * // For Spring MVC applications
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     *     http.authorizeHttpRequests(...)
     *     return http.build();
     * }
     * SecurityWebFilterChain (Reactive/WebFlux)
     * Used with Spring WebFlux reactive applications
     * Works with spring-boot-starter-webflux (non-blocking)
     * Processes requests asynchronously using Project Reactor
     * Part of org.springframework.security.web.server package
     * // For Spring WebFlux/Gateway applications
     * @Bean
     * public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
     *     http.authorizeExchange(...)
     *     return http.build();
     * }
     * Which is Better for API Gateway?
     * SecurityWebFilterChain is the correct choice for your Gateway because:
     *
     * Spring Cloud Gateway is built on WebFlux - It's a reactive, non-blocking framework
     * Performance - Non-blocking handles high concurrency better
     * Consistency - Uses the same reactive stack as the gateway
     * If you use SecurityFilterChain (MVC) with a WebFlux application, you'll get conflicts and errors because they use different request processing models.
     *
     * Your current configuration using SecurityWebFilterChain with @EnableWebFluxSecurity is correct for the API Gateway
     *
     *
     *Detailed Analysis: SecurityWebFilterChain Implementations
     * Implementation 1 (Current - More Secure)
     * @Bean
     * public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
     *     http
     *         .authorizeExchange(exchanges -> exchanges
     *             .pathMatchers("/actuator/**").permitAll()
     *             .pathMatchers("/api/auth/**").permitAll()
     *             .anyExchange().authenticated()
     *         )
     *         .oauth2ResourceServer(oauth2 -> oauth2
     *             .jwt(jwt -> {})
     *         );
     *     return http.build();
     * }
     * Implementation 2 (Less Secure)
     * @Bean
     * public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
     *     return http
     *         .csrf(ServerHttpSecurity.CsrfSpec::disable)
     *         .authorizeExchange(exchanges -> exchanges
     *             .anyExchange().authenticated()
     *         )
     *         .oauth2ResourceServer(oauth2 -> oauth2
     *             .jwt(Customizer.withDefaults())
     *         )
     *         .build();
     * }
     * Key Differences
     * Aspect	        Implementation 1	                Implementation 2
     * CSRF Protection	Enabled (default)	                  Disabled explicitly
     * Public Endpoints	/actuator/**, /api/auth/**	          None
     * JWT Configuration	Empty jwt -> {}	                  Customizer.withDefaults()
     * Security Level	    ✅ Higher	                          ⚠️ Lower
     *
     *
     * Detailed Breakdown
     * 1. CSRF Protection
     * Impl 1: Uses default CSRF protection (recommended for browser clients with sessions)
     * Impl 2: Explicitly disables CSRF with .csrf(ServerHttpSecurity.CsrfSpec::disable)
     * Analysis: For stateless API/Microservices using JWT, CSRF is less critical but disabling removes a security layer
     * 2. Public Access Paths
     * Impl 1: Explicitly permits:
     * /actuator/** - Health endpoints for monitoring
     * /api/auth/** - Login/registration endpoints
     * Impl 2: Requires authentication for ALL requests
     * Analysis: Impl 1 is more practical - health checks and auth endpoints need to be public
     * 3. JWT Configuration
     * Impl 1: jwt(jwt -> {}) - Empty configuration, relies on application.yaml properties
     * Impl 2: jwt(Customizer.withDefaults()) - Uses default JWT decoder configuration
     * Analysis: Both work similarly, but Impl 2 is more explicit about using defaults
     * Security Recommendation
     * Implementation 1 is recommended because:
     *
     * ✅ Provides public access to health endpoints (required for k8s load balancers)
     * ✅ Allows auth endpoints to be accessible without tokens
     * ✅ Maintains CSRF protection (more secure)
     * ✅ Clean JWT configuration via YAML
     * Only use Implementation 2 if:
     *
     * You have a separate authentication service handling all auth
     * You're using mTLS or another authentication mechanism
     * All endpoints require authentication (including health checks)
     *
     *
     * See
     *
     * */

