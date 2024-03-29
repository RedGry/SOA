package se.ifmo.ru.firstservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import se.ifmo.ru.firstservice.web.model.Error;

import java.time.Instant;


@Component
public class ResponseUtils {
    public ResponseEntity<Error> buildResponseWithMessage(HttpStatus status, String message){
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Error.builder()
                        .message(message)
                        .code(String.valueOf(status.value()))
                        .date(Instant.now().toString())
                        .build()
                );
    }
}
