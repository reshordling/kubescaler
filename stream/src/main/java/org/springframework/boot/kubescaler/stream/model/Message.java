package org.springframework.boot.kubescaler.stream.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String host;
    private String message;
    private String timestamp;
}