package com.example.demo.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T body;
    private boolean ok;

    public static ApiResponse<?> getDefaultErrorResponse() {
        return ApiResponse.builder()
                .ok(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
    }
}
