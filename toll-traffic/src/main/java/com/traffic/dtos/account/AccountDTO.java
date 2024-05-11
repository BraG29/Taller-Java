package com.traffic.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public abstract class AccountDTO {

    protected Long id;
    protected Integer accountNumber;
    protected LocalDate creationDate;

}
