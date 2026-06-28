package com.example.ems.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String msg;
    private LocalDateTime timeStamp;

    public ErrorResponse(String msg, LocalDateTime timeStamp) {
        this.msg = msg;
        this.timeStamp = timeStamp;
    }

}
