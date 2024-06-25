package com.traffic.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private LocalDate date;
    private String message;

}