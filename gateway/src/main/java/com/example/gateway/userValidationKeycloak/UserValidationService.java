package com.example.gateway.userValidationKeycloak;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId){

        log.info("Calling User Registraion for{}", userId);

        try {
            return userServiceWebClient.get()
                    .uri("api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class, e ->{
                        if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                            return Mono.error(new RuntimeException("User not found : "+userId));
                        else if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                            return Mono.error(new RuntimeException("Invalid UserId:"+userId));

                        return Mono.error(new RuntimeException("Unexpected error with:"+userId));
                    });
            } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
            log.info("Calling User Registration for {}", registerRequest.getEmail());

        try {
            return userServiceWebClient.post()
                    .uri("api/users/register")
                    .bodyValue(registerRequest)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .onErrorResume(WebClientResponseException.class, e ->{
                        if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                            return Mono.error(new RuntimeException("Bad Request:"+e.getMessage()));

                        return Mono.error(new RuntimeException("Unexpected error with:"+e.getMessage()));
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
