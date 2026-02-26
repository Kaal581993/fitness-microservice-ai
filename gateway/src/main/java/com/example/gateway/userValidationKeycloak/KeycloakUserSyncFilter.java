package com.example.gateway.userValidationKeycloak;


import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Calendar;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {


    private final UserValidationService userValidationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest registerRequest = getUserDetails(token);

        if(userId == null){
            userId = registerRequest.getKeyCloakId();
        }

        if(userId != null && token !=null){
            String finalUserId = userId;
            return userValidationService.validateUser(userId)
                    .flatMap(exist ->{
                        if(!exist){
                            if(registerRequest != null){
                                return userValidationService.registerUser(registerRequest)
                                        .then(Mono.empty());
                            }else{
                                return Mono.empty();
                            }

                        } else{
                            log.info("User already exist, Skipping sync");
                            return Mono.empty();
                        }
                    }).then(Mono.defer(() ->{
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-ID", finalUserId)
                        .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }));
        }


        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {

        try{
            String tokenWithoutBearer = token.replace("Bearer", "").trim();

            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest request = new RegisterRequest();
            request.setEmail(claims.getStringClaim("email"));
            request.setKeyCloakId(claims.getStringClaim("sub"));
            request.setPassword("1stLUV@152991");
            request.setFirstName(claims.getStringClaim("given_name"));
            request.setLastName(claims.getStringClaim("family_name"));
            request.setUsername(claims.getStringClaim("preferred_username"));

            // ✅ ADD: Set a default DOB (e.g., 2000-01-01 as placeholder)
            // Users can update their DOB later through profile settings
            Calendar cal = Calendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1); // Default: Jan 1, 2000
            request.setDOB(cal.getTime());

            return request;
        }catch(ParseException e){
            throw new RuntimeException(e);
        }

    }
}
