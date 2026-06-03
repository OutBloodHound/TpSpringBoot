package com.conciergerie.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class NotificationDTO {
    private Long id;
    private String type;
    private String message;
    private String lien;
    private boolean lu;
    private LocalDateTime creeLe;
}
