package com.traffic.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AccountDTO {

    protected Integer accountNumber;
    protected LocalDate creationDate;

}
