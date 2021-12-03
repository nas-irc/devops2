package io.takima.reservation.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionDto {
    private String message;

    public ExceptionDto(Exception e) {
        this.message = e.getMessage();
    }
}
